package ru.tikskit.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import ru.tikskit.config.dto.ProductChanges;
import ru.tikskit.config.dto.ProductDto;
import ru.tikskit.service.BrandNewProductService;
import ru.tikskit.service.InStockAgainService;
import ru.tikskit.service.OnlyFewLeftService;
import ru.tikskit.service.ProductChangesPromoAnalyst;

import java.util.concurrent.Executors;

@Configuration
@EnableIntegration
@AllArgsConstructor
public class Channels {

    private final ProductChangesPromoAnalyst promoAnalyst;
    private final InStockAgainService inStockAgainService;
    private final BrandNewProductService brandNewProductService;
    private final OnlyFewLeftService onlyFewLeftService;

    /**
     * Тут конфигурируем бизнес-логику обработки изменения товара на остатке
     *
     * @return
     */
    @Bean
    public IntegrationFlow channelFlow() {
        return IntegrationFlows.from("productChangesChannel")
                .channel(c -> c.executor(Executors.newCachedThreadPool())) // Поток сервиса не хочет ждать окончания этой обработки
                .<ProductChanges>filter(pc -> pc.getCurProduct() != null)
                .publishSubscribeChannel(c -> // обрабатываем случай, когда товар снова появился в наличии
                        c.subscribe(sf -> sf
                                .filter(promoAnalyst::isInStockAgain)
                                .transform(Transformers.converter(ProductChanges::getCurProduct))
                                .handle(m ->
                                        inStockAgainService.sendNotification((ProductDto) m.getPayload())
                                )
                        )
                )
                .publishSubscribeChannel(c -> // обрабатываем случай, когда товар новый
                        c.subscribe(sf -> sf
                                .filter(promoAnalyst::isNewProduct)
                                .transform(Transformers.converter(ProductChanges::getCurProduct))
                                .handle(m -> brandNewProductService.addProduct((ProductDto) m.getPayload()))))
                .publishSubscribeChannel(c -> // обрабатываем случай, когда товара осталось немного
                        c.subscribe(sf -> sf
                                .filter(promoAnalyst::isFewLeft)
                                .transform(Transformers.converter(ProductChanges::getCurProduct))
                                .handle(m -> onlyFewLeftService.notifyClients((ProductDto) m.getPayload()))))
                .get();
    }
}
