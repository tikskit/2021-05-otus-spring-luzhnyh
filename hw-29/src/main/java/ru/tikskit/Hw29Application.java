package ru.tikskit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.tikskit.config.dto.ProductChanges;
import ru.tikskit.config.dto.ProductDto;
import ru.tikskit.promo.PromoGateway;

@SpringBootApplication
public class Hw29Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Hw29Application.class, args);
		PromoGateway promo = context.getBean(PromoGateway.class);
		ProductChanges changes;
		// in stock again case
		changes = new ProductChanges(new ProductDto(1, "t-shirt", 0), new ProductDto(1, "t-shirt", 15));
		promo.promote(changes);
		// Brand new case
		changes = new ProductChanges(null, new ProductDto(10, "t-shirt", 200));
		promo.promote(changes);
		// only few left case
		changes = new ProductChanges(new ProductDto(100, "t-shirt", 200), new ProductDto(100, "t-shirt", 2));
		promo.promote(changes);
	}

}
