package com.xideral.order.exception;

public class OrderNoFoundException extends RuntimeException {
    public OrderNoFoundException(String message) {
        super(message);
    }
}
