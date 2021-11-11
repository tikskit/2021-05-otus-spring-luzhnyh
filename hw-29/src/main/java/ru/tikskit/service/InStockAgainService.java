package ru.tikskit.service;

import ru.tikskit.config.dto.ProductDto;

public interface InStockAgainService {
    void sendNotification(ProductDto productDto);
}
