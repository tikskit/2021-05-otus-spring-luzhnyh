package ru.tikskit.config.dto;

import ru.tikskit.config.dto.ProductDto;
import ru.tikskit.model.Product;

public interface ProductConverter {
    ProductDto toDto(Product product);
}
