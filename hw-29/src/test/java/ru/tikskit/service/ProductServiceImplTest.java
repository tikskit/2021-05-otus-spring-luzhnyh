package ru.tikskit.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import ru.tikskit.repositories.ProductRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Служба продуктов должна")
@DataJpaTest
@Import(ProductServiceImpl.class)
class ProductServiceImplTest {
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @DisplayName("выбрасывать ProductNotFound если продукт с указанным ид не найден при вызове incQuantity")
    @Test
    public void incQuantityShouldThrowProductNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.incQuantity(1050, 10)).isInstanceOf(ProductNotFound.class);

        verify(productRepository, times(1)).findById(anyLong());
    }

    @DisplayName("выбрасывать ProductNotFound если продукт с указанным ид не найден при вызове decQuantity")
    @Test
    public void decQuantityShouldThrowProductNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.decQuantity(1050, 10)).isInstanceOf(ProductNotFound.class);

        verify(productRepository, times(1)).findById(anyLong());
    }

    @DisplayName("Выбрасывать InappropriateProductValue если значение value меньше или равно 0 при вызове incQuantity")
    @Test
    public void incQuantityShouldThrowInappropriateProductValue(){
        assertThatThrownBy(() -> productService
                .incQuantity(1050, 0))
                .isInstanceOf(WrongProductValue.class);
        assertThatThrownBy(() -> productService
                .incQuantity(1050, -10))
                .isInstanceOf(WrongProductValue.class);
    }

    @DisplayName("Выбрасывать InappropriateProductValue если значение value меньше или равно 0 при вызове decQuantity")
    @Test
    public void decQuantityShouldThrowInappropriateProductValue(){
        assertThatThrownBy(() -> productService
                .decQuantity(1050, 0))
                .isInstanceOf(WrongProductValue.class);
        assertThatThrownBy(() -> productService
                .decQuantity(1050, -10))
                .isInstanceOf(WrongProductValue.class);
    }
}