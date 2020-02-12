package com.edge1s.product.dto;

import com.edge1s.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private String type;
    private Integer discount;
    private BigDecimal price;
    private Integer views;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.type = product.getType().getName();
        this.discount = product.getType().getDiscountInPercent();
        this.price = (product.getPrice());
        this.views = product.getViews();
    }

}
