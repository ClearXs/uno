package cc.allio.uno.core.util;

import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * 文件相关的工具集合
 *
 * @author jw
 * @date 2021/12/4 22:54
 */
public class FileUtil {

    private FileUtil() {

    }

    private static final String CLASS_PATH = "classpath:";


    /**
     * 读取文件并以字符串输出
     *
     * @param path 文件路径，可以是以classpath:开头的文件
     * @param out  输出后文件进行消费
     */
    public static void readFileToString(String path, Consumer<String> out) {
        readFileToString(path)
                .subscribe(out);
    }

    public static Mono<String> readFileToString(String path) {
        return filepath(path).filter(Objects::nonNull)
                .flatMap(realPath -> {
                    FileInputStream in;
                    String output = null;
                    try {
                        String resolvedPath = URLDecoder.decode(realPath, StandardCharsets.UTF_8.name());
                        in = new FileInputStream(resolvedPath);
                        byte[] bytes = new byte[in.available()];
                        while (in.read(bytes) > 0) {
                        }
                        output = new String(bytes, StandardCharsets.UTF_8);
                    } catch (IOException error) {
                        Mono.error(error);
                    }
                    return Mono.justOrEmpty(output);
                });
    }


    /**
     * path解析
     *
     * @param path 文件路径，如果是classpath路径，则使用类加载器加载文件的具体位置
     */
    static Mono<String> filepath(@Nonnull String path) {
        URL resource = null;
        if (path.startsWith(CLASS_PATH)) {
            path = path.substring(CLASS_PATH.length());
            resource = Thread.currentThread().getContextClassLoader().getResource(path);
        } else {
            try {
                resource = new URL(path);
            } catch (MalformedURLException e) {
                return Mono.error(e);
            }
        }
        return Mono
                .justOrEmpty(Optional.ofNullable(resource))
                .map(URL::getPath);
    }

}
