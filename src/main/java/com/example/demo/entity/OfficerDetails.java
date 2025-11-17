package com.example.demo.entity;

import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.*;

@Entity
@Table(name = "officer_details")
public class OfficerDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String location; // Officer's location
	private boolean isActive; // Availability
	private String department; // Officer's department
	private String badgeNumber; // Officer's badge number

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user; // Officer is linked to a User

	@OneToMany(mappedBy = "officerDetails")
	private List<Complaint> complaints = new ArrayList<>(); // Officer's Complaints

	@OneToMany(mappedBy = "officer", cascade = CascadeType.ALL)
	private List<Notification> notifications;

	// Officer's Assignment Notifications

	// Constructors
	public OfficerDetails() {
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
		return isActive;
	}

	public void setActive(boolean active) {
		isActive = active;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Complaint> getComplaints() {
		return complaints;
	}

	public void setComplaints(List<Complaint> complaints) {
		this.complaints = complaints;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
}
