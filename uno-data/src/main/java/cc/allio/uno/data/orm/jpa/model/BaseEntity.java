package cc.allio.uno.data.orm.jpa.model;

import cc.allio.uno.core.util.DateUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable, Persistable<Long> {

    /**
     * 主键id
     */
    @Id
    @GenericGenerator(name = "snowflake", strategy = "cc.allio.uno.data.jpa.id.JpaIdGenerator")
    @GeneratedValue(generator = "snowflake")
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(length = 64)
    @org.springframework.data.annotation.Id
    private Long id;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
    @JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
    @LastModifiedDate
    private Date updateTime;

    @Override
    public boolean isNew() {
        return getId() != null && getId() > 0;
    }
}
