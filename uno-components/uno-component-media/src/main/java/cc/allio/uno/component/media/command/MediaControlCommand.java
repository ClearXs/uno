package cc.allio.uno.component.media.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 多媒体控制指令接口
 *
 * @author jiangwei
 * @date 2022/4/20 16:47
 * @see BaseMediaControlCommand
 * @since 1.0.6
 */
public interface MediaControlCommand extends Command {

    /**
     * 标识Media实体
     */
    String MEDIA_CONTROL_TAG = "MEDIA_CONTROL";

    /**
     * 控制指令集合
     *
     * @author jiangwei
     * @date 2022/7/16 10:12
     * @since 1.0
     */
    @Getter
    @AllArgsConstructor
    enum ControlCommandSet {
        LEFT("left", "左转"),
        RIGHT("right", "右转"),
        UP("up", "上转"),
        DOWN("down", "下转"),
        ZOOM_IN("zoomin", "放大"),
        ZOOM_OUT("zoomout", "缩小"),
        STOP("stop", "停止");

        /**
         * 控制命令名称
         */
        private final String name;

        private final String label;

        /**
         * 根据指定的名称转换为指令集合
         *
         * @param name 指令名称
         * @return 找到的指令或者抛出Null异常
         * @throws NullPointerException 没有找到时抛出
         */
        public static ControlCommandSet to(String name) {
            return Arrays.stream(values())
                    .filter(control -> control.getName().equals(name))
                    .findFirst()
                    .orElseThrow(NullPointerException::new);
        }

    }
}
