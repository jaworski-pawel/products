package com.edge1s.product.service;

import com.edge1s.product.exception.ProductNotFoundException;
import com.edge1s.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewService {

    private final ProductRepository productRepository;

    public void increaseViews(Long id) {
        Long views = getViewsByID(id);
        productRepository.updateViews(id, ++views);
    }

    public Long getViews(Long id) {
        return getViewsByID(id);
    }

    private Long getViewsByID(Long id) {
        Optional<Long> viewsById = productRepository.getViewsById(id);
        if (viewsById.isPresent()) {
            return viewsById.get();
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}
