package com.edge1s.product.controller;

import com.edge1s.product.dto.ProductDTO;
import com.edge1s.product.service.ProductService;
import com.edge1s.product.service.ViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ViewService viewService;

    @GetMapping()
    public List<ProductDTO> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/{id}/views")
    public Long getViewsOfProduct(@PathVariable Long id) {
        return viewService.getViews(id);
    }

    @PostMapping()
    @ResponseStatus(value = HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductDTO productDTO) {
        productService.createProduct(productDTO);
    }

    @PutMapping("/{id}")
    public void editProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) {
        productService.editProduct(productDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
