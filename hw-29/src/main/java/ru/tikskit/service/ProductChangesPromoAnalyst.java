package ru.tikskit.service;

import ru.tikskit.config.dto.ProductChanges;

public interface ProductChangesPromoAnalyst {
    boolean isNewProduct(ProductChanges changes);
    boolean isInStockAgain(ProductChanges changes);
    boolean isFewLeft(ProductChanges changes);
}
