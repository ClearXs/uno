package cc.allio.uno.data.model;

import cc.allio.uno.data.orm.jpa.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "users")
public class User extends BaseEntity {

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "role_id", length = 512)
    private Long roleId;

}
