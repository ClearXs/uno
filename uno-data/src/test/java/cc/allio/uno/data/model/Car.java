package cc.allio.uno.data.model;

import lombok.*;

import javax.persistence.*;

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
