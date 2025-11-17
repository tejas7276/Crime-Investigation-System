package com.example.demo.DTO;

public class UserDTO {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String role; // 🆕 User role (Citizen, Officer, Admin)

    // Default constructor
    public UserDTO() {}

    // All-args constructor (optional, saves mapping hustle)
    public UserDTO(Long id, String firstname, String lastname, String email, String username, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
