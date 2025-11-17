package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String type;
    private String location;
    private String status;
    private LocalDate date;
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-notifications") 
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "officer_id", referencedColumnName = "id")
    private OfficerDetails officerDetails;

    // Constructors
    public Complaint() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OfficerDetails getOfficerDetails() {
        return officerDetails;
    }

    public void setOfficerDetails(OfficerDetails officerDetails) {
        this.officerDetails = officerDetails;
    }

    public OfficerDetails getAssignedOfficer() {
        return officerDetails;
    }

    public void setAssignedOfficer(OfficerDetails assignedOfficer) {
        this.officerDetails = assignedOfficer;
    }
}
