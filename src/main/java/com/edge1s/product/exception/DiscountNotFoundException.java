package com.edge1s.product.exception;

public class DiscountNotFoundException extends RuntimeException {
    public DiscountNotFoundException(String name) {
        super("could not find discount with name " + name);
    }
}
