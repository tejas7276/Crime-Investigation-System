package com.example.demo.controller;

import com.example.demo.DTO.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserService;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	// Register a new user
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		try {
			User savedUser = userService.registerUser(user);
			return ResponseEntity.ok(savedUser); // ✅ Return JSON to frontend
		} catch (UserAlreadyExistsException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("message", e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("message", "Registration failed"));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User userRequest) {
	    try {
	        User loggedInUser = userService.loginUser(userRequest.getUsername(), userRequest.getPassword());
	        UserDTO userDTO = userService.convertToDTO(loggedInUser); // Convert to DTO
	        return ResponseEntity.ok(userDTO); // Return DTO instead of raw User
	    } catch (UserNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Collections.singletonMap("message", "User not found"));
	    } catch (InvalidPasswordException e) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                .body(Collections.singletonMap("message", "Invalid password"));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("message", "An error occurred while processing your request"));
	    }
	}
	
	@PutMapping("/updateUser/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
		try {
			User user = userService.updateUser(id, updatedUser);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Collections.singletonMap("message", e.getMessage()));
		}
	}

	@PostMapping("/migrate-passwords")
	public ResponseEntity<String> migratePasswords() {
		userService.migratePlaintextPasswordsToBCrypt();
		return ResponseEntity.ok("All passwords migrated to BCrypt");
	}

	// Get user by ID
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}
}
