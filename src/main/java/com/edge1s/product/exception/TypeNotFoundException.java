package com.edge1s.product.exception;

public class TypeNotFoundException extends RuntimeException {
    public TypeNotFoundException(String type) {
        super("could not find type with name " + type);
    }
}
