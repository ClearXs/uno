package cc.allio.uno.data.orm;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class User {

    @Id
    private Long id;
    private String userName;
    private String password;
}
