package com.example.demo.DTO;

import java.time.LocalDateTime;

public class NotificationDTO {

    private Long id;
    private String message;
    private LocalDateTime timestamp;
    private String status;
    private Long userId;  // User ID to avoid circular reference
    private Long officerId;
    private Long adminId;

    public NotificationDTO(Long id, String message, LocalDateTime timestamp, String status, Long userId, Long officerId, Long adminId) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
        this.userId = userId;
        this.officerId = officerId;
        this.adminId = adminId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }
}
