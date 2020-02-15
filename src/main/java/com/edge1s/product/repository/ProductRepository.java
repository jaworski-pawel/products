package com.edge1s.product.repository;

import com.edge1s.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "SELECT `VIEWS` FROM `PRODUCT` WHERE `ID` = ?1", nativeQuery = true)
    Optional<Long> getViewsById(Long id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE `PRODUCT` SET `VIEWS` = ?2 WHERE `ID` = ?1", nativeQuery = true)
    void updateViews(Long id, Long views);
}
