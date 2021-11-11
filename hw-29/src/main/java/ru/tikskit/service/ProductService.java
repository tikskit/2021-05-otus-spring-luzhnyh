package ru.tikskit.service;

public interface ProductService {
    /**
     * Увеличивает остаток на value
     * @param productId  Ид продукта
     * @param value количество, на которое увеличивается остаток
     * @return новый остаток
     * @throws ProductNotFound если продукт с укзанным ид не найден
     */
    int incQuantity(long productId, int value);

    /**
     *
     * @param productId Ид продукта
     * @param value количество, на которое уменьшается остаток
     * @return новый остаток
     * @throws ProductNotFound если продукт с укзанным ид не найден
     * @throws NotEnoughProducts если продукт остаток на складе меньше value. В этом случае списание не происходит
     */
    int decQuantity(long productId, int value);
}
