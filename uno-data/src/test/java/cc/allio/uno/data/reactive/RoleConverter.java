package cc.allio.uno.data.reactive;

import cc.allio.uno.data.model.Role;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class RoleConverter implements Converter<Row, Role> {
    @Override
    public Role convert(Row row) {
        return new Role();
    }
}
