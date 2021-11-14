package ru.tikskit.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductChanges {
    private final ProductDto prevProduct;
    private final ProductDto curProduct;
}
