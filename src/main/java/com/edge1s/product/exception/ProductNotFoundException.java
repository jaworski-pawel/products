package com.edge1s.product.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("could not find product with id: " + id);
    }
}
