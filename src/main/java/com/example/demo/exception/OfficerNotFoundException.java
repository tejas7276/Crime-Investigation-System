package com.example.demo.exception;

public class OfficerNotFoundException extends RuntimeException {
    public OfficerNotFoundException(Long id) {
        super("Officer not found with ID: " + id);
    }

    public OfficerNotFoundException(String department) {
        super("No officers available in department: " + department);
    }
}