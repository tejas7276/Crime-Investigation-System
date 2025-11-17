package com.example.demo.exception;

public class ComplaintNotFoundException extends RuntimeException {
    public ComplaintNotFoundException(Long complaintId) {
        super("Complaint not found with ID: " + complaintId);
    }
    
    // Added constructor for status-based lookup
    public ComplaintNotFoundException(String status) {
        super("No complaints found with status: " + status);
    }
}