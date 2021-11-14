package ru.tikskit.service;

import org.springframework.stereotype.Component;
import ru.tikskit.config.dto.ProductDto;

@Component
public class OnlyFewLeftServiceImpl implements OnlyFewLeftService {
    @Override
    public void notifyClients(ProductDto productDto) {
        System.out.println(
                String.format(
                        "OnlyFewLeft service is notifying all the customers who have the product in their shop card that we're running out: %s. ThreadId=%s",
                        productDto, Thread.currentThread().getId())
        );

    }
}
