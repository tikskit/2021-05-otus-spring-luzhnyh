package ru.tikskit.service;

import ru.tikskit.config.dto.ProductDto;

public interface OnlyFewLeftService {
    void notifyClients(ProductDto productDto);
}
