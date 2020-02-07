package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.entity.Product;
import com.edge1s.product.entity.Type;
import com.edge1s.product.repository.ProductRepository;
import com.edge1s.product.repository.TypeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceTest {

    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private TypeRepository typeRepository = Mockito.mock(TypeRepository.class);
    private ProductService productService = new ProductService(productRepository, typeRepository);
    private ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);

    @Test
    void shouldCreatedProduct() {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .description("This is test product")
                .type("MALE")
                .price(BigDecimal.valueOf(100))
                .build();

        Type male = Type.builder()
                .name("MALE")
                .discountInPercent(5)
                .build();

        Mockito.when(typeRepository.findByName("MALE")).thenReturn(Optional.of(male));

        //when
        productService.createProduct(productDTO);
        //then
        Mockito.verify(productRepository, Mockito.times(1)).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertEquals("Test Product", capturedProduct.getName());
        assertEquals("This is test product", capturedProduct.getDescription());
        assertEquals(male, capturedProduct.getType());
        assertEquals(BigDecimal.valueOf(100), capturedProduct.getPrice());
    }
}