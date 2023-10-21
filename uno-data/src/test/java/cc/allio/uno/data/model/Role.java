package cc.allio.uno.data.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "roles")
@Table
@org.springframework.data.relational.core.mapping.Table("roles")
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
