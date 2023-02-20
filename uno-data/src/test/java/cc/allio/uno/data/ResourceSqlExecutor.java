package cc.allio.uno.data;

import cc.allio.uno.core.util.IoUtil;
import cc.allio.uno.core.util.template.ExpressionTemplate;
import cc.allio.uno.core.util.template.Tokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;

/**
 * resource
 *
 * @author jiangwei
 * @date 2022/12/14 19:45
 * @since 1.1.3
 */
@Slf4j
public class ResourceSqlExecutor {

    public static final String H2 = "h2";
    public static final String MYSQL = "mysql";
    public static final String POSTGRES = "postgres";
    public static final String ORACLE = "oracle";
    public static final String MSSQL = "mssql";

    private static final String SQL_FILE = "classpath:database/" + "{{dbType}}" + "/" + "{{sqlName}}";

    private static final ExpressionTemplate TEMPLATE = ExpressionTemplate.createTemplate(Tokenizer.DOUBLE_BRACE);


    /**
     * 执行指定resource下的SQL文件
     *
     * @param dbType  数据类型
     * @param sqlName sql文件名称
     * @param factory SqlSessionFactory实例对象，创建Statement执行创建表语句
     * @throws Throwable 发生异常时抛出
     */
    public static void executeSql(String dbType, String sqlName, SqlSessionFactory factory) throws Throwable {
        String sql = TEMPLATE.parseTemplate(SQL_FILE, "dbType", dbType, "sqlName", sqlName);
        URL url = ResourceUtils.getURL(sql);
        UrlResource resource = new UrlResource(url);
        String sqlStatement = IoUtil.readToString(resource.getInputStream());
        executeSql(sqlStatement, factory);
    }

    /**
     * 执行给定的SQL语句
     *
     * @param sql
     * @param factory
     */
    public static void executeSql(String sqlStatement, SqlSessionFactory factory) {
        SqlSession sqlSession = factory.openSession();
        Connection connection = sqlSession.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(sqlStatement);
            sqlSession.commit();
        } catch (Throwable ex) {
            log.error("execute sql: {} error", sqlStatement, ex);
            sqlSession.rollback(true);
        }
    }
}
