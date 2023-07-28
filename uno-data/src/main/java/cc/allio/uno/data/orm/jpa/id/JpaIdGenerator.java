package cc.allio.uno.data.orm.jpa.id;

import cc.allio.uno.core.util.id.IdGenerator;
import cc.allio.uno.data.orm.jpa.model.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

/**
 * JPA ID生成器
 *
 * @author jiangwei
 * @date 2022/12/27 10:52
 * @see BaseEntity
 * @since 1.1.4
 */
public class JpaIdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return IdGenerator.defaultGenerator().getNextId();
    }
}
