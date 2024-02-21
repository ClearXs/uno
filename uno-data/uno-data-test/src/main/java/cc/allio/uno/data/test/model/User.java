package cc.allio.uno.data.test.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "t_users")
public class User extends BaseEntity {

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "role_id", length = 512)
    private Long roleId;

}
