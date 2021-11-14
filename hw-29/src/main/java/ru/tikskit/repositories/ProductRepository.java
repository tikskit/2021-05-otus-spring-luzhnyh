package ru.tikskit.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tikskit.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
