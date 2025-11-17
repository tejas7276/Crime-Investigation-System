package com.example.demo.DTO;

public class OfficerDetailsDTO {

    private Long id;
    private String name;
    private String location;    // Officer's location (City/Area assigned)
    private boolean active;     // Officer's active status (true = active, false = inactive)
    private String department;  // Officer's department name
    private String badgeNumber; // Unique badge/ID number
    private Long userId;         // Linked User ID (officer login credentials)

    // Default constructor
    public OfficerDetailsDTO() {}

    // All-args constructor (optional, for easy mapping)
    public OfficerDetailsDTO(Long id, String name, String location, boolean active, String department, String badgeNumber, Long userId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.active = active;
        this.department = department;
        this.badgeNumber = badgeNumber;
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBadgeNumber() {
        return badgeNumber;
    }

    public void setBadgeNumber(String badgeNumber) {
        this.badgeNumber = badgeNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
