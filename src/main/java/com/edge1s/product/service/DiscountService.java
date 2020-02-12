package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class DiscountService {

    public BigDecimal calculateTheDiscountPrice(ProductDTO productDTO) {
        return productDTO
                .getPrice()
                .multiply(BigDecimal.valueOf(100 - productDTO.getDiscount()))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.UP);
    }
}
