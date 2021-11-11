package ru.tikskit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tikskit.model.Product;
import ru.tikskit.repositories.ProductRepository;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public int incQuantity(long productId, int value) {
        if (value <= 0) {
            throw new WrongProductValue("Значение должно быть больше 0!");
        }
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFound::new);
        int newQuantity = product.getQuantity() + value;
        product.setQuantity(newQuantity);
        productRepository.save(product);
        return newQuantity;
    }

    @Override
    public int decQuantity(long productId, int value) {
        if (value <= 0) {
            throw new WrongProductValue("Значение должно быть больше 0!");
        }
        Product product = productRepository.findById(productId).orElseThrow(ProductNotFound::new);
        if (product.getQuantity() >= value) {
            int newQuantity = product.getQuantity() + value;
            product.setQuantity(newQuantity);
            productRepository.save(product);
            return newQuantity;
        } else {
            throw new NotEnoughProducts();
        }
    }
}
