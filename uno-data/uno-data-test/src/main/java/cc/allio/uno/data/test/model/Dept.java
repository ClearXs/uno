package cc.allio.uno.data.test.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "t_dept")
public class Dept {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private List<String> userIds;
}
