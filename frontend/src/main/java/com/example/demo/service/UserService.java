package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.UserAlreadyExistsException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.UserRepository;
import com.example.demo.DTO.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	
	public User loginUser(String username, String rawPassword) {
	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));
	    
	    System.out.println("🔑 Password Check:");
	    System.out.println("Raw input: " + rawPassword);
	    System.out.println("DB stored: " + user.getPassword());
	    System.out.println("Matches?: " + passwordEncoder.matches(rawPassword, user.getPassword()));
	    
	    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
	        throw new InvalidPasswordException();
	    }
	    return user;
	}
	

	// 📝 Register method (with encoded password save)
	public User registerUser(User user) {
		if (user.getPassword() == null || user.getPassword().length() <= 3) {
			throw new InvalidPasswordException();
		}

		if (userRepository.existsByEmail(user.getEmail())) {
			throw new UserAlreadyExistsException("Email already in use.");
		}

		if (userRepository.existsByUsername(user.getUsername())) {
			throw new UserAlreadyExistsException("Username already in use.");
		}

		// 🔐 Encode password before saving
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		return userRepository.save(user);
	}

	// ✏️ Update method
	public User updateUser(Long id, User updatedUser) {
		Optional<User> existingUserOpt = userRepository.findById(id);

		if (existingUserOpt.isPresent()) {
			User existingUser = existingUserOpt.get();
			existingUser.setFirstname(updatedUser.getFirstname());
			existingUser.setLastname(updatedUser.getLastname());
			existingUser.setEmail(updatedUser.getEmail());
			existingUser.setUsername(updatedUser.getUsername());

			// 🔄 Re-encode password if changed
			if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
				existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
			}

			return userRepository.save(existingUser);
		} else {
			throw new UserNotFoundException("User not found with ID: " + id);
		}
	}

	// 🔍 Get user by ID
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
	}

	// 🔍 Get user by username
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	// 👑 Get Admin User (first one found)
	public User getAdminUser() {
		List<User> adminUsers = userRepository.findByRole("ADMIN");

		if (adminUsers.isEmpty()) {
			throw new UserNotFoundException("No admin users found.");
		}

		return adminUsers.get(0); // You can enhance this logic later
	}

	public void migratePlaintextPasswordsToBCrypt() {
	    List<User> users = userRepository.findAll();
	    users.forEach(user -> {
	        if (!user.getPassword().startsWith("$2a$")) { // If plaintext
	            user.setPassword(passwordEncoder.encode(user.getPassword()));
	            userRepository.save(user);
	            System.out.println("Migrated password for: " + user.getUsername());
	        }
	    });
	}
	
	// 🧾 Convert User entity to DTO
	public UserDTO convertToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setFirstname(user.getFirstname());
		userDTO.setLastname(user.getLastname());
		userDTO.setEmail(user.getEmail());
		userDTO.setUsername(user.getUsername());
		userDTO.setRole(user.getRole());
		return userDTO;
	}

}
