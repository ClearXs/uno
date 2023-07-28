package cc.allio.uno.core.util;

import cc.allio.uno.core.StringPool;
import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * 文件相关的工具集合
 *
 * @author jw
 * @date 2021/12/4 22:54
 */
public class FileUtils {

    private FileUtils() {

    }

    private static final String CLASS_PATH = "classpath:";
    private static final ClassLoader CLASS_LOADER = FileUtils.class.getClassLoader();

    /**
     * 读取类路径下的文件为字符串数据
     *
     * @param classPath 类路径下的文件路径，如test.json /t1/test.json
     * @return 读取的文件字符串
     */
    public static String readSingleClassFileForceToString(String classPath) {
        AtomicReference<String> ref = new AtomicReference<>();
        readSingleFileForceToString(CLASS_PATH + classPath, ref::set);
        return ref.get();
    }

    /**
     * 读取文件并以字符串输出
     *
     * @param path 文件路径，可以是以classpath:开头的文件
     * @param out  输出后文件进行消费
     */
    public static void readSingleFileForceToString(String path, Consumer<String> out) {
        AtomicReference<String> ref = new AtomicReference<>();
        readSingleFile(path).map(FileReadResult::getContent).subscribe(ref::set);
        out.accept(ref.get());
    }

    /**
     * 读取类路径下的文件内容
     *
     * @param classpath 类路径
     * @return FileReadResult or null
     */
    public static FileReadResult readSingleClassFileForce(String classpath) {
        AtomicReference<FileReadResult> ref = new AtomicReference<>();
        readSingleClassFile(classpath).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 读取文件路径的文件内容
     *
     * @param path 文件路径
     * @return FileReadResult or null
     */
    public static FileReadResult readSingleFileForce(String path) {
        AtomicReference<FileReadResult> ref = new AtomicReference<>();
        readSingleFile(path).subscribe(ref::set);
        return ref.get();
    }

    /**
     * 给定一个文件路径（可以是文件目录），返回 FileReadResult数据
     *
     * @param classpath 类/类文件夹路径
     * @return list
     */
    public static List<FileReadResult> readClassFileForce(String classpath) {
        AtomicReference<List<FileReadResult>> ref = new AtomicReference<>();
        readClassFile(classpath).collectList().subscribe(ref::set);
        return ref.get();
    }

    /**
     * 给定一个文件路径（可以是文件目录），返回 FileReadResult数据
     *
     * @param path 路径/文件夹路径
     * @return list
     */
    public static List<FileReadResult> readFileForce(String path) {
        AtomicReference<List<FileReadResult>> ref = new AtomicReference<>();
        readFile(path).collectList().subscribe(ref::set);
        return ref.get();
    }

    /**
     * 给定一个具体的文件路径,获取其文件内容
     *
     * @param classPath 类文件路径
     * @return FileReadResult mono
     * @throws FileNotFoundException 文件找不到时抛出
     */
    public static Mono<FileReadResult> readSingleClassFile(String classPath) {
        return readSingleFile(CLASS_PATH + classPath);
    }

    /**
     * 给定一个具体的文件路径，获取其文件内容
     *
     * @param path 文件路径
     * @return FileReadResult mono
     * @throws FileNotFoundException 文件找不到时抛出
     */
    public static Mono<FileReadResult> readSingleFile(String path) {
        // 判断是否包含
        if (!StringUtils.hasText(StringPool.ORIGIN_DOT)) {
            return Mono.error(new FileNotFoundException(String.format("file path must '.' file, but now is %s", path)));
        }
        return readFile(path).single();
    }

    /**
     * 给定一个类文件路径读取该路径下的文件内容
     *
     * @param classPath 类/类文件夹路径
     * @return FileReadResult flux
     */
    public static Flux<FileReadResult> readClassFile(String classPath) {
        return readFile(CLASS_PATH + classPath);
    }

    /**
     * 给定一个文件路径（可以是文件目录），返回读取的结果数据{@link FileReadResult}。
     *
     * @param path 文件/文件夹路径
     * @return FileReadResult flux
     * @see FileReadResult
     */
    public static Flux<FileReadResult> readFile(String path) {
        return filepath(path)
                .filter(Objects::nonNull)
                .flatMapMany(realPath -> {
                    File sourceFile = new File(realPath);
                    // 展开文件信息
                    return Mono.just(sourceFile)
                            .expand(f -> {
                                if (f.isDirectory()) {
                                    File[] children = f.listFiles();
                                    if (children == null) {
                                        return Flux.empty();
                                    }
                                    return Flux.fromArray(children);
                                }
                                return Flux.empty();
                            })
                            .filter(File::isFile)
                            .flatMap(f -> {
                                BufferedReader io;
                                try {
                                    io = new BufferedReader(new FileReader(f));
                                    return Flux.fromStream(io.lines())
                                            .reduce((o, n) -> o + StringPool.NEWLINE + n)
                                            .publishOn(Schedulers.boundedElastic())
                                            .flatMap(content -> {
                                                try {
                                                    io.close();
                                                    return Mono.just(FileReadResult.builder()
                                                            .filename(f.getName())
                                                            .file(f)
                                                            .content(content)
                                                            .build());
                                                } catch (IOException ex) {
                                                    return Mono.error(ex);
                                                }
                                            });
                                } catch (IOException ex) {
                                    return Mono.error(ex);
                                }
                            });
                });
    }

    /**
     * path解析
     *
     * @param path 文件路径，如果是classpath路径，则使用类加载器加载文件的具体位置
     */
    private static Mono<String> filepath(@Nonnull String path) {
        URL resource = null;
        if (path.startsWith(CLASS_PATH)) {
            path = path.substring(CLASS_PATH.length());
            resource = CLASS_LOADER.getResource(path);
        } else {
            try {
                resource = new URL(path);
            } catch (MalformedURLException e) {
                return Mono.error(e);
            }
        }
        return Mono.justOrEmpty(Optional.ofNullable(resource)).map(URL::getPath);
    }

    /**
     * 文件读取结果
     */
    @Data
    @Builder
    public static class FileReadResult {

        /**
         * 文件名称
         */
        private String filename;

        /**
         * 文件实例
         */
        private File file;

        /**
         * 文件内容
         */
        private String content;

    }
}
