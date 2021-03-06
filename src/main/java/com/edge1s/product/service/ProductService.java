package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.entity.Product;
import com.edge1s.product.entity.Type;
import com.edge1s.product.exception.ProductNotFoundException;
import com.edge1s.product.exception.TypeNotFoundException;
import com.edge1s.product.repository.ProductRepository;
import com.edge1s.product.repository.TypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final TypeRepository typeRepository;
    private final DiscountService discountService;
    private final ViewService viewService;

    public List<ProductDTO> getProducts() {
        return productRepository.findAll().stream()
                .map(ProductDTO::new)
                .peek(productDTO -> productDTO.setPrice(discountService.calculateTheDiscountPrice(productDTO)))
                .collect(Collectors.toList());
    }

    public ProductDTO getProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductDTO productDTO = new ProductDTO(optionalProduct.get());
            viewService.increaseViews(id);
            productDTO.setPrice(discountService.calculateTheDiscountPrice(productDTO));
            return productDTO;
        } else {
            throw new ProductNotFoundException(id);
        }
    }

    public void editProduct(ProductDTO productDTO, Long id) {
        Optional<Type> optionalType = typeRepository.findByName(productDTO.getType());
        if (optionalType.isPresent()) {
            if (productRepository.findById(id).isPresent()) {
                Product product = Product.builder()
                        .id(id)
                        .name(productDTO.getName())
                        .description(productDTO.getDescription())
                        .type(optionalType.get())
                        .price(productDTO.getPrice())
                        .build();
                productRepository.save(product);
            } else {
                throw new ProductNotFoundException(id);
            }
        } else {
            throw new TypeNotFoundException(productDTO.getType());
        }

    }

    public void createProduct(ProductDTO productDTO) {
        Optional<Type> optionalType = typeRepository.findByName(productDTO.getType());
        if (optionalType.isPresent()) {
            Long initialViews = 0L;
            Product product = Product.builder()
                    .name(productDTO.getName())
                    .description(productDTO.getDescription())
                    .type(optionalType.get())
                    .price(productDTO.getPrice())
                    .views(initialViews)
                    .build();
            productRepository.save(product);
        } else {
            throw new TypeNotFoundException(productDTO.getType());
        }
    }

    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ProductNotFoundException(id);
        }
    }
}
