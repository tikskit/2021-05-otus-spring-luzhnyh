package ru.tikskit.service;

import org.springframework.stereotype.Component;
import ru.tikskit.config.dto.ProductChanges;

@Component
public class ProductChangesPromoAnalystImpl implements ProductChangesPromoAnalyst {
    private static final int FEW_VALUE = 10;

    @Override
    public boolean isNewProduct(ProductChanges changes) {
        return changes.getPrevProduct() == null;
    }

    @Override
    public boolean isInStockAgain(ProductChanges changes) {
        return (changes.getPrevProduct() != null) && (changes.getPrevProduct().getQuantity() == 0) &&
                (changes.getCurProduct().getQuantity() > 0);
    }

    @Override
    public boolean isFewLeft(ProductChanges changes) {
        return (changes.getCurProduct().getQuantity() > 0) && (changes.getCurProduct().getQuantity() <= FEW_VALUE);
    }
}
