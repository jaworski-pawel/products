package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.entity.Product;
import com.edge1s.product.exception.ProductNotFoundException;
import com.edge1s.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    public ProductDTO getProduct(Long id) throws ProductNotFoundException {

        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ProductDTO(product.get());
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    public void editProduct(ProductDTO productDTO, Long id) {

        if (productRepository.findById(id).isPresent()) {
            Product product = Product.builder()
                    .id(id)
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .type(productDTO.getType())
                    .price(productDTO.getPrice())
                    .views(productDTO.getViews())
                    .build();
            productRepository.save(product);
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    public void createProduct(ProductDTO productDTO) {
        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .type(productDTO.getType())
                .price(productDTO.getPrice())
                .views(productDTO.getViews())
                .build();
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}
