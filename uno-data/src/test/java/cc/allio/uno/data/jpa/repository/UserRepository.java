package cc.allio.uno.data.jpa.repository;

import cc.allio.uno.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);

    int deleteByName(String name);
}
