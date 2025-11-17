package com.example.demo.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String field) {
        super("User with this " + field + " already exists");
    }
}