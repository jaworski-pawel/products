package com.edge1s.product.dto;

import com.edge1s.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private String type;
    private BigDecimal price;
    private Integer views;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.type = product.getType().getName();
        this.price = calculateTheDiscountPrice(product);
        this.views = product.getViews();
    }

    public void increaseViews() {
        views++;
    }

    private BigDecimal calculateTheDiscountPrice(Product product) {
        return product
                .getPrice()
                .multiply(BigDecimal.valueOf(100 - product.getType().getDiscountInPercent()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.UP);
    }
}
