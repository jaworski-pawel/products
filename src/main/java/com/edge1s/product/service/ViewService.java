package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import org.springframework.stereotype.Service;

@Service
public class ViewService {

    public ProductDTO increaseViews(ProductDTO productDTO) {
        Integer views = productDTO.getViews();
        views++;
        productDTO.setViews(views);
        return productDTO;
    }
}
