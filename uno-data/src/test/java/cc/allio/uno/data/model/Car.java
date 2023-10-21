package cc.allio.uno.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "car")
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    private String brand;

    @Column
    private Long userId;
}
