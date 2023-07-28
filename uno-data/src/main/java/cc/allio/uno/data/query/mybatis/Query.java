package cc.allio.uno.data.query.mybatis;

import cc.allio.uno.data.query.mybatis.mapper.QueryMapper;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识注解，使用于Mapper查询
 *
 * @author jiangwei
 * @date 2022/9/30 16:40
 * @see QueryMapper
 * @since 1.1.0
 */
@Inherited
@Documented
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Query {
}
