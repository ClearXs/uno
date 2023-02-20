package cc.allio.uno.data.jpa.repository;

import cc.allio.uno.data.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByBrand(String brand);

    List<Car> findByUserId(Long userId);
}
