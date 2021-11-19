package ru.tikskit.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tikskit.config.dto.ProductChanges;
import ru.tikskit.config.dto.ProductConverter;
import ru.tikskit.config.dto.ProductDto;
import ru.tikskit.model.Product;
import ru.tikskit.promo.PromoGateway;
import ru.tikskit.repositories.ProductRepository;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final PromoGateway promo;

    @Override
    public int incQuantity(long productId, int value) {
        if (value <= 0) {
            throw new WrongProductValue("Значение должно быть больше 0!");
        }
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFound::new);
        ProductDto prev = productConverter.toDto(product);
        int newQuantity = product.getQuantity() + value;
        product.setQuantity(newQuantity);
        productRepository.save(product);

        ProductDto cur = productConverter.toDto(product);
        promo.promote(new ProductChanges(prev, cur));

        return newQuantity;
    }

    @Override
    public int decQuantity(long productId, int value) {
        if (value <= 0) {
            throw new WrongProductValue("Значение должно быть больше 0!");
        }
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFound::new);
        if (product.getQuantity() >= value) {
            ProductDto prev = productConverter.toDto(product);
            int newQuantity = product.getQuantity() + value;
            product.setQuantity(newQuantity);
            productRepository.save(product);

            ProductDto cur = productConverter.toDto(product);
            promo.promote(new ProductChanges(prev, cur));

            return newQuantity;
        } else {
            throw new NotEnoughProducts();
        }
    }

    @Override
    public Product addNewProduct(Product product) {
        Product newProduct = productRepository.save(product);

        promo.promote(new ProductChanges(null, productConverter.toDto(newProduct)));

        return newProduct;
    }
}
