package cc.allio.uno.test.testcontainers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.testcontainers.utility.DockerImageName;

/**
 * describe to docker container
 *
 * @author j.x
 * @date 2024/3/18 22:54
 * @since 1.1.7
 */
@Getter
@AllArgsConstructor
public enum ContainerType {

    PostgreSQL("postgis/postgis:16-3.4-alpine", "org.testcontainers.containers.PostgreSQLContainer"),
    MySQL("mysql:8.0.36", "org.testcontainers.containers.MySQLContainer"),
    MSSQL("", "org.testcontainers.containers.MSSQLServerContainer"),
    Mongodb("mongo:7.0.6", "org.testcontainers.containers.MongoDBContainer"),

    // marked test
    Test("", "cc.allio.uno.test.testcontainers.TestContainer");

    final String version;
    final String className;

    public DockerImageName getDockerImageName() {
        DockerImageName imageName = DockerImageName.parse(version);
        if (this == PostgreSQL) {
            return imageName.asCompatibleSubstituteFor("postgres");
        } else if (this == MSSQL) {
            return DockerImageName.parse("mcr.microsoft.com/mssql/server");
        }
        return imageName;
    }
}
