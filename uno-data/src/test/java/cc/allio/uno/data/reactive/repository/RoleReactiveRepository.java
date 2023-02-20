package cc.allio.uno.data.reactive.repository;

import cc.allio.uno.data.model.Role;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface RoleReactiveRepository extends R2dbcRepository<Role, Long> {

    Flux<Role> findByNameContains(String name);
}
