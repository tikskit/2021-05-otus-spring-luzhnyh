package ru.tikskit.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ProductDto {
    private final long id;
    private final String name;
    private final int quantity;
}
