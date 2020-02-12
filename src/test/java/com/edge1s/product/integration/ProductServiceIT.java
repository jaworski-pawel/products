package com.edge1s.product.integration;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.repository.ProductRepository;
import com.edge1s.product.service.ProductService;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceIT {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void deleteAllCars() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnProductWithPriceAfterDiscountAndThreeDisplay() {
        //given
        ProductDTO existProduct = ProductDTO.builder()
                .name("Test Product")
                .description("This is test product")
                .type("MALE")
                .price(BigDecimal.valueOf(100))
                .build();
        productService.createProduct(existProduct);

        //when
        productService.getProduct(1L);
        productService.getProduct(1L);
        ProductDTO resultProduct = productService.getProduct(1L);

        //then
        assertEquals("Test Product", resultProduct.getName());
        assertEquals("This is test product", resultProduct.getDescription());
        assertEquals("MALE", resultProduct.getType());
        assertEquals(BigDecimal.valueOf(95).setScale(2, RoundingMode.UP), resultProduct.getPrice());
        assertEquals(Integer.valueOf(3), resultProduct.getViews());
    }
}