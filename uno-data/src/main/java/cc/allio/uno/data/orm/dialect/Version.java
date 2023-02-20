package cc.allio.uno.data.orm.dialect;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 数据库版本号
 *
 * @author jiangwei
 * @date 2023/1/12 18:17
 * @since 1.1.4
 */
@Data
@AllArgsConstructor
public class Version {

    private final Integer major;
    private final Integer minor;
    private final Integer micro;

    /**
     * 创建数据库版本实例 minor = 0
     *
     * @param major major version
     * @return Version 实例
     */
    public static Version make(Integer major) {
        return make(major, 0);
    }

    /**
     * 创建数据库版本实例 micro = 0
     *
     * @param major major version
     * @param minor minor version
     * @return Version 实例
     */
    public static Version make(Integer major, Integer minor) {
        return make(major, minor, 0);
    }

    /**
     * 创建数据库版本实例
     *
     * @param major major version
     * @param minor minor version
     * @param micro micro version
     * @return Version 实例
     */
    public static Version make(Integer major, Integer minor, Integer micro) {
        return new Version(major, minor, micro);
    }


}
