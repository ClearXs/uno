package cc.allio.uno.data.reactive.repository;

import cc.allio.uno.data.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserReactiveRepository extends R2dbcRepository<User, Long> {


    Flux<User> findByNameContains(String name);
}
