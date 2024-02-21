package cc.allio.uno.data.test.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "t_roles")
@Table
public class Role implements Persistable<Long> {

    @Id
    @GeneratedValue
    @org.springframework.data.annotation.Id
    private Long id;

    @Column(name = "name", length = 64)
    private String name;

    @Override
    public boolean isNew() {
        return id != null && id > 0;
    }
}
