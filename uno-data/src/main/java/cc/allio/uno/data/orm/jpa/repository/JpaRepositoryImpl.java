package cc.allio.uno.data.orm.jpa.repository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

/**
 * JPA Repository
 *
 * @author jiangwei
 * @date 2022/12/27 14:14
 * @since 1.1.4
 */
public abstract class JpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> {

    protected JpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }
}
