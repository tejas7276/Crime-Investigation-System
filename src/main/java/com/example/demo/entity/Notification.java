package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference(value = "user-notifications")
    private User user; // Recipient of the notification

    private String message; // Notification content

    private LocalDateTime timestamp; // When notification was created/sent

    @Column(nullable = false)
    private String status; // "UNREAD" or "READ"

    @ManyToOne
    @JoinColumn(name = "officer_id")
    private OfficerDetails officer;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonBackReference(value = "admin-notifications")
    private User admin;

    // Default constructor
    public Notification() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public OfficerDetails getOfficer() {
        return officer;
    }

    public void setOfficer(OfficerDetails officer) {
        this.officer = officer;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }
}
