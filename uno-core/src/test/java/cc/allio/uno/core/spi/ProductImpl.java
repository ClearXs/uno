package cc.allio.uno.core.spi;

import com.google.auto.service.AutoService;
import lombok.Getter;

@AutoService(Product.class)
public class ProductImpl implements Product {

    @Getter
    private final String name;

    public ProductImpl(String name) {
        this.name = name;
    }

}
