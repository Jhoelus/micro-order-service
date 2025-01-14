package com.xideral.order.exception;

public class UserServiceUnavailable extends RuntimeException {
    public UserServiceUnavailable(String message) {
        super(message);
    }
}
