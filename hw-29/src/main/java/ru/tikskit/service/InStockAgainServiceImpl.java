package ru.tikskit.service;

import org.springframework.stereotype.Component;
import ru.tikskit.config.dto.ProductDto;

@Component
public class InStockAgainServiceImpl implements InStockAgainService {
    @Override
    public void sendNotification(ProductDto productDto) {
        System.out.println(
                String.format(
                        "InStockAgain service is notifying all who has the product in their wish list that it's available again: %s. ThreadId=%s",
                        productDto, Thread.currentThread().getId())
        );
    }
}
