package cc.allio.uno.data.reactive.repository;

import cc.allio.uno.data.model.Car;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CarReactiveRepository extends R2dbcRepository<Car, Long> {
}
