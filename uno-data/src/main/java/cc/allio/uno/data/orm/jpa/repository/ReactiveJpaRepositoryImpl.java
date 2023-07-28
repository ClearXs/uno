package cc.allio.uno.data.orm.jpa.repository;

import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.repository.query.RelationalEntityInformation;

import java.io.Serializable;

/**
 * Reactive JPA Repository
 *
 * @author jiangwei
 * @date 2022/12/27 14:11
 * @since 1.1.4
 */
public abstract class ReactiveJpaRepositoryImpl<T, ID extends Serializable> extends SimpleR2dbcRepository<T, ID> {

    protected ReactiveJpaRepositoryImpl(RelationalEntityInformation<T, ID> entity, R2dbcEntityOperations entityOperations, R2dbcConverter converter) {
        super(entity, entityOperations, converter);
    }


}
