package ru.tikskit.service;

import org.springframework.stereotype.Component;
import ru.tikskit.config.dto.ProductDto;

@Component
public class BrandNewProductServiceImpl implements BrandNewProductService {
    @Override
    public void addProduct(ProductDto productDto) {
        System.out.println(
                String.format(
                        "BrandNewProduct service has registered the new product and it will be included in weekly email: %s. ThreadId=%s",
                        productDto, Thread.currentThread().getId())
        );

    }
}
