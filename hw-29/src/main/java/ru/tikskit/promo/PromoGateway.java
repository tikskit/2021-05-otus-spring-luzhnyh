package ru.tikskit.promo;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.tikskit.config.dto.ProductChanges;

@MessagingGateway
public interface PromoGateway {
    @Gateway(requestChannel = "productServiceInChannel")
    void promote(ProductChanges changes);
}
