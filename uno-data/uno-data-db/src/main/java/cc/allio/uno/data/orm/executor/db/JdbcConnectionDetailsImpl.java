package cc.allio.uno.data.orm.executor.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;

@AllArgsConstructor
@Getter
public class JdbcConnectionDetailsImpl implements JdbcConnectionDetails {

    private final String username;
    private final String password;
    private final String jdbcUrl;

}
