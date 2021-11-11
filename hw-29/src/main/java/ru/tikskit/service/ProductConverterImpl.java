package ru.tikskit.service;

import org.springframework.stereotype.Component;
import ru.tikskit.config.dto.ProductDto;
import ru.tikskit.model.Product;

@Component
public class ProductConverterImpl implements ProductConverter {
    @Override
    public ProductDto toDto(Product product) {
        return new ru.tikskit.config.dto.ProductDto(product.getId(), product.getName(), product.getQuantity());
    }
}
