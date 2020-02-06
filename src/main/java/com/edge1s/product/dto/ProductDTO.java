package com.edge1s.product.dto;

import com.edge1s.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
        this.price = product.getPrice();
        this.views = product.getViews();
    }

    public void increaseViews() {
        views++;
    }
}
