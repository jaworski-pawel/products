package com.edge1s.product.service;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.entity.Product;
import com.edge1s.product.entity.Type;
import com.edge1s.product.exception.ProductNotFoundException;
import com.edge1s.product.exception.TypeNotFoundException;
import com.edge1s.product.repository.ProductRepository;
import com.edge1s.product.repository.TypeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private TypeRepository typeRepository = Mockito.mock(TypeRepository.class);
    private ProductService productService = new ProductService(productRepository, typeRepository);
    private ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
    private ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);

    @Test
    void shouldCreateProduct() {
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

    @Test
    void shouldNotCreateProductBecauseTypeIsNotExist() {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .name("Test Product")
                .description("This is test product")
                .type("M")
                .price(BigDecimal.valueOf(100))
                .build();

        Type male = Type.builder()
                .name("MALE")
                .discountInPercent(5)
                .build();

        Mockito.when(typeRepository.findByName("MALE")).thenReturn(Optional.of(male));
        Mockito.when(typeRepository.findByName("M")).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(TypeNotFoundException.class, () -> {
            productService.createProduct(productDTO);
        });

        String expectedMessage = "could not find type with name M";
        String actualMessage = exception.getMessage();
        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldReturnAllProduct() {
        //given
        List<Product> products = Arrays.asList(
                Product.builder()
                        .id(1L)
                        .name("product1")
                        .description("description1")
                        .type(Type.builder().id(1L).name("MALE").discountInPercent(5).build())
                        .price(BigDecimal.valueOf(100))
                        .views(10)
                        .build(),
                Product.builder()
                        .id(2L)
                        .name("product2")
                        .description("description2")
                        .type(Type.builder().id(1L).name("FEMALE").discountInPercent(5).build())
                        .price(BigDecimal.valueOf(200))
                        .views(5)
                        .build(),
                Product.builder()
                        .id(3L)
                        .name("product3")
                        .description("description3")
                        .type(Type.builder().id(3L).name("KID").discountInPercent(10).build())
                        .price(BigDecimal.valueOf(300))
                        .views(3)
                        .build()
        );
        Mockito.when(productRepository.findAll()).thenReturn(products);

        //when
        List<ProductDTO> productDTOList = productService.getProducts();

        //then
        assertEquals(3, productDTOList.size());
        ProductDTO secondProduct = productDTOList.get(1);
        assertEquals("product2", secondProduct.getName());
        assertEquals("description2", secondProduct.getDescription());
        assertEquals("FEMALE", secondProduct.getType());
        assertEquals(BigDecimal.valueOf(190.00).setScale(2, RoundingMode.UP), secondProduct.getPrice());
        assertEquals(5, secondProduct.getViews());
    }

    @Test
    void shouldReturnProductAndIncreaseViews() {
        //given
        Product product2 = Product.builder()
                .id(2L)
                .name("product2")
                .description("description2")
                .type(Type.builder().id(1L).name("FEMALE").discountInPercent(5).build())
                .price(BigDecimal.valueOf(200))
                .views(5)
                .build();

        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        Mockito.when(typeRepository.findByName("FEMALE"))
                .thenReturn(Optional.of(Type.builder().id(1L).name("FEMALE").discountInPercent(5).build()));

        //when
        ProductDTO productDTO = productService.getProduct(2L);

        //then
        assertEquals("product2", productDTO.getName());
        assertEquals("description2", productDTO.getDescription());
        assertEquals("FEMALE", productDTO.getType());
        assertEquals(BigDecimal.valueOf(190.00).setScale(2, RoundingMode.UP), productDTO.getPrice());
        assertEquals(6, productDTO.getViews());
    }

    @Test
    void shouldNotReturnProductBecauseIsNotExist() {
        //given
        Mockito.when(productRepository.findById(2L)).thenReturn(Optional.empty());


        //when
        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProduct(1L);
        });
        String expectedMessage = "could not find product with id: 1";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldEditProduct() {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("New product name")
                .description("New description")
                .type("MALE")
                .price(BigDecimal.valueOf(100))
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Old product name")
                .description("Old description")
                .type(Type.builder().id(1L).name("MALE").discountInPercent(5).build())
                .price(BigDecimal.valueOf(100))
                .views(10)
                .build();

        Type male = Type.builder()
                .name("MALE")
                .discountInPercent(5)
                .build();

        Mockito.when(typeRepository.findByName("MALE")).thenReturn(Optional.of(male));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        //when
        productService.editProduct(productDTO, 1L);

        //then
        Mockito.verify(productRepository, Mockito.times(1)).save(productArgumentCaptor.capture());
        Product capturedProduct = productArgumentCaptor.getValue();
        assertEquals("New product name", capturedProduct.getName());
        assertEquals("New description", capturedProduct.getDescription());
        assertEquals(male, capturedProduct.getType());
        assertEquals(BigDecimal.valueOf(100), capturedProduct.getPrice());
    }

    @Test
    void shouldNotEditProductBecauseIsNotExist() {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("New product name")
                .description("New description")
                .type("MALE")
                .price(BigDecimal.valueOf(100))
                .build();

        Type male = Type.builder()
                .name("MALE")
                .discountInPercent(5)
                .build();

        Mockito.when(typeRepository.findByName("MALE")).thenReturn(Optional.of(male));
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.editProduct(productDTO, 1L);
        });
        String expectedMessage = "could not find product with id: 1";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldNotEditProductBecauseTypeNotExist() {
        //given
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("New product name")
                .description("New description")
                .type("NEW_TYPE")
                .price(BigDecimal.valueOf(100))
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("Old product name")
                .description("Old description")
                .type(Type.builder().id(1L).name("MALE").discountInPercent(5).build())
                .price(BigDecimal.valueOf(100))
                .views(10)
                .build();

        Type male = Type.builder()
                .name("MALE")
                .discountInPercent(5)
                .build();

        Mockito.when(typeRepository.findByName("MALE")).thenReturn(Optional.of(male));
        Mockito.when(typeRepository.findByName("NEW_TYPE")).thenReturn(Optional.empty());
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        //when
        Exception exception = assertThrows(TypeNotFoundException.class, () -> {
            productService.editProduct(productDTO, 1L);
        });

        String expectedMessage = "could not find type with name NEW_TYPE";
        String actualMessage = exception.getMessage();
        //then
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void shouldDeleteProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Old product name")
                .description("Old description")
                .type(Type.builder().id(1L).name("MALE").discountInPercent(5).build())
                .price(BigDecimal.valueOf(100))
                .views(10)
                .build();

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        //when
        productService.deleteProduct(1L);

        //then
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(idArgumentCaptor.capture());
        Long capturedId = idArgumentCaptor.getValue();
        assertEquals(1L, capturedId);
    }

    @Test
    void shouldNotDeleteProductBecauseIsNotExist() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });
        String expectedMessage = "could not find product with id: 1";
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

}