package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.entity.Discount;
import com.edge1s.product.exception.DiscountNotFoundException;
import com.edge1s.product.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    public BigDecimal calculateTheDiscountPrice(ProductDTO productDTO) {
        int discount = 0;
        BigDecimal price = productDTO.getPrice();
        if (price.compareTo(BigDecimal.valueOf(500)) >= 0) {
            if (price.compareTo(BigDecimal.valueOf(2000)) >= 0) {
                discount = getPercentDiscountByName("big");
            } else {
                discount = getPercentDiscountByName("small");
            }
        }
        price = price.multiply(BigDecimal.valueOf(100 - discount))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.UP);
        return price;
    }

    private int getPercentDiscountByName(String name) {
        Optional<Discount> optionalDiscount = discountRepository.findByName(name);
        if (optionalDiscount.isPresent()) {
            return optionalDiscount.get().getDiscountInPercent();
        } else throw new DiscountNotFoundException(name);
    }
}
