package cc.allio.uno.data.orm;

import lombok.Data;

import javax.persistence.Id;

@Data
public class User {

    @Id
    private Long id;
    private String userName;
    private String password;
}
