package cc.allio.uno.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Flux;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements Serializable {

    private String id;

    private String name;

    private String type;

    public static Flux<User> mocks(int mockNumber) {
        return Flux.range(0, mockNumber)
                .map(num -> new User(String.valueOf(num), "jojo", "type"));
    }

}
