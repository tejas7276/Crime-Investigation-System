package com.example.demo.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class ComplaintDTO {

    private Long id;
    private String description;
    private String type;
    private String location;
    private String status;
    private LocalDate date;
    private LocalTime time;
    private Long userId;     // Citizen ID who filed the complaint
    private Long officerId;  // Officer ID assigned to the complaint (nullable if not assigned)
    private String officername;


    // Default constructor
    public ComplaintDTO() {
    }

  
    public ComplaintDTO(Long id, String description, String type, String location, String status, LocalDate date, LocalTime time, Long userId, Long officerId) {
        this.id = id;
        this.description = description;
        this.type = type;
        this.location = location;
        this.status = status;
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.officerId = officerId;
    }

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


	public String getOfficername() {
		return officername;
	}


	public void setOfficername(String officername) {
		this.officername = officername;
	}
    
    

}
