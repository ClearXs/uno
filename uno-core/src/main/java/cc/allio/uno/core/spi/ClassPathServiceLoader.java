package cc.allio.uno.core.spi;

import cc.allio.uno.core.util.ClassUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * copy from jdk {@link ServiceLoader}.
 * <p>because jdk supplies class path load by default constructor, if service contains arguments, it's throws error.</p>
 *
 * <b>enhanced create instance by constructor create service instance</b>
 *
 * @author j.x
 * @date 2024/3/15 08:54
 * @since 1.1.7
 */
public class ClassPathServiceLoader<T> implements Iterator<ClassPathServiceLoader.Provider<T>> {

    static final String PREFIX = "META-INF/services/";

    private final Class<T> service;
    @SuppressWarnings("removal")
    private final AccessControlContext acc;
    private final ClassLoader loader;

    // use when create service instance
    private Object[] createdArgs;

    Set<String> providerNames = new HashSet<>();  // to avoid duplicates
    Enumeration<URL> configs;
    Iterator<String> pending;

    ClassPathServiceLoader.Provider<T> nextProvider;
    ServiceConfigurationError nextError;

    ClassPathServiceLoader(Class<T> service, ClassLoader loader, Object... createArgs) {
        this.service = service;
        this.loader = loader;
        this.acc = (System.getSecurityManager() != null)
                ? AccessController.getContext()
                : null;
        if (createArgs == null || createArgs.length == 0) {
            this.createdArgs = new Object[]{};
        } else {
            this.createdArgs = createArgs;
        }
    }

    /**
     * Parse a single line from the given configuration file, adding the
     * name on the line to set of names if not already seen.
     */
    private int parseLine(URL u, BufferedReader r, int lc, Set<String> names)
            throws IOException {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) ln = ln.substring(0, ci);
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                fail(service, u, lc, "Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                fail(service, u, lc, "Illegal provider-class name: " + ln);
            int start = Character.charCount(cp);
            for (int i = start; i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
            if (providerNames.add(ln)) {
                names.add(ln);
            }
        }
        return lc + 1;
    }

    /**
     * Parse the content of the given URL as a provider-configuration file.
     */
    private Iterator<String> parse(URL u) {
        Set<String> names = new LinkedHashSet<>(); // preserve insertion order
        try {
            URLConnection uc = u.openConnection();
            uc.setUseCaches(false);
            try (InputStream in = uc.getInputStream();
                 BufferedReader r
                         = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                int lc = 1;
                while ((lc = parseLine(u, r, lc, names)) >= 0) ;
            }
        } catch (IOException x) {
            fail(service, "Error accessing configuration file", x);
        }
        return names.iterator();
    }

    /**
     * Loads and returns the next provider class.
     */
    private Class<?> nextProviderClass() {
        if (configs == null) {
            try {
                String fullName = PREFIX + service.getName();
                if (loader == null) {
                    configs = ClassLoader.getSystemResources(fullName);
                } else {
                    configs = loader.getResources(fullName);
                }
            } catch (IOException x) {
                fail(service, "Error locating configuration files", x);
            }
        }
        while ((pending == null) || !pending.hasNext()) {
            if (!configs.hasMoreElements()) {
                return null;
            }
            pending = parse(configs.nextElement());
        }
        String cn = pending.next();
        try {
            return Class.forName(cn, false, loader);
        } catch (ClassNotFoundException x) {
            fail(service, "Provider " + cn + " not found");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private boolean hasNextService() {
        while (nextProvider == null && nextError == null) {
            try {
                Class<?> clazz = nextProviderClass();
                if (clazz == null)
                    return false;

                if (service.isAssignableFrom(clazz)) {
                    Class<? extends T> type = (Class<? extends T>) clazz;
                    nextProvider = new ProviderImpl<>(service, type, null, acc, createdArgs);
                } else {
                    fail(service, clazz.getName() + " not a subtype");
                }
            } catch (ServiceConfigurationError e) {
                nextError = e;
            }
        }
        return true;
    }

    private ClassPathServiceLoader.Provider<T> nextService() {
        if (!hasNextService())
            throw new NoSuchElementException();

        ClassPathServiceLoader.Provider<T> provider = nextProvider;
        if (provider != null) {
            nextProvider = null;
            return provider;
        } else {
            ServiceConfigurationError e = nextError;
            assert e != null;
            nextError = null;
            throw e;
        }
    }

    @SuppressWarnings("removal")
    @Override
    public boolean hasNext() {
        if (acc == null) {
            return hasNextService();
        } else {
            PrivilegedAction<Boolean> action = this::hasNextService;
            return AccessController.doPrivileged(action, acc);
        }
    }

    @SuppressWarnings("removal")
    @Override
    public Provider<T> next() {
        if (acc == null) {
            return nextService();
        } else {
            PrivilegedAction<ClassPathServiceLoader.Provider<T>> action = this::nextService;
            return AccessController.doPrivileged(action, acc);
        }
    }

    /**
     * A Provider implementation that supports invoking, with reduced
     * permissions, the static factory to obtain the provider or the
     * provider's no-arg constructor.
     */
    private static class ProviderImpl<S> implements ClassPathServiceLoader.Provider<S> {
        final Class<S> service;
        final Class<? extends S> type;
        final Method factoryMethod;  // factory method or null
        final Object[] createdArgs;
        @SuppressWarnings("removal")
        final AccessControlContext acc;

        ProviderImpl(Class<S> service,
                     Class<? extends S> type,
                     Method factoryMethod,
                     @SuppressWarnings("removal") AccessControlContext acc,
                     Object[] createdArgs) {
            this.service = service;
            this.type = type;
            this.factoryMethod = factoryMethod;
            this.acc = acc;
            this.createdArgs = createdArgs;
        }

        @Override
        public Class<? extends S> type() {
            return type;
        }

        @Override
        public S get() {
            if (factoryMethod != null) {
                return invokeFactoryMethod();
            } else {
                return newInstance();
            }
        }

        /**
         * Invokes the provider's "provider" method to instantiate a provider.
         * When running with a security manager then the method runs with
         * permissions that are restricted by the security context of whatever
         * created this loader.
         */
        private S invokeFactoryMethod() {
            Object result = null;
            Throwable exc = null;
            if (acc == null) {
                try {
                    result = factoryMethod.invoke(null);
                } catch (Throwable x) {
                    exc = x;
                }
            } else {
                PrivilegedExceptionAction<?> pa = (PrivilegedExceptionAction<Object>) () -> factoryMethod.invoke(null);
                // invoke factory method with permissions restricted by acc
                try {
                    result = AccessController.doPrivileged(pa, acc);
                } catch (Throwable x) {
                    if (x instanceof PrivilegedActionException)
                        x = x.getCause();
                    exc = x;
                }
            }
            if (exc != null) {
                if (exc instanceof InvocationTargetException)
                    exc = exc.getCause();
                fail(service, factoryMethod + " failed", exc);
            }
            if (result == null) {
                fail(service, factoryMethod + " returned null");
            }
            @SuppressWarnings("unchecked")
            S p = (S) result;
            return p;
        }

        /**
         * Invokes Constructor::newInstance to instantiate a provider. When running
         * with a security manager then the constructor runs with permissions that
         * are restricted by the security context of whatever created this loader.
         */
        private S newInstance() {
            return ClassUtils.newInstance(type, createdArgs);
        }

        // For now, equals/hashCode uses the access control context to ensure
        // that two Providers created with different contexts are not equal
        // when running with a security manager.

        @Override
        public int hashCode() {
            return Objects.hash(service, type, acc);
        }

        @Override
        public boolean equals(Object ob) {
            if (ob instanceof ProviderImpl<?>) {
                ProviderImpl<?> that = (ProviderImpl<?>) ob;
                return this.service == that.service
                        && this.type == that.type
                        && Objects.equals(this.acc, that.acc);
            } else {
                return false;
            }
        }
    }


    private static void fail(Class<?> service, String msg, Throwable cause)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg,
                cause);
    }

    private static void fail(Class<?> service, String msg)
            throws ServiceConfigurationError {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class<?> service, URL u, int line, String msg)
            throws ServiceConfigurationError {
        fail(service, u + ":" + line + ": " + msg);
    }

    public static <S> ClassPathServiceLoader<S> load(Class<S> service, Object... createdArgs) {
        return load(service, ClassLoader.getSystemClassLoader(), createdArgs);
    }


    public static <S> ClassPathServiceLoader<S> load(Class<S> service, ClassLoader classLoader, Object... createdArgs) {
        return new ClassPathServiceLoader<>(service, classLoader, createdArgs);
    }

    public interface Provider<S> extends Supplier<S> {
        /**
         * Returns the provider type. There is no guarantee that this type is
         * accessible or that it has a public no-args constructor. The {@link
         * #get() get()} method should be used to obtain the provider instance.
         *
         * <p> When a module declares that the provider class is created by a
         * provider factory then this method returns the return type of its
         * public static "{@code provider()}" method.
         *
         * @return The provider type
         */
        Class<? extends S> type();

        /**
         * Returns an instance of the provider.
         *
         * @return An instance of the provider.
         *
         * @throws ServiceConfigurationError
         *         If the service provider cannot be instantiated, or in the
         *         case of a provider factory, the public static
         *         "{@code provider()}" method returns {@code null} or throws
         *         an error or exception. The {@code ServiceConfigurationError}
         *         will carry an appropriate cause where possible.
         */
        @Override
        S get();
    }
}
