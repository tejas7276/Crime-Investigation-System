package com.example.demo.service;

import com.example.demo.entity.Complaint;
import com.example.demo.entity.OfficerDetails;
import com.example.demo.entity.User;
import com.example.demo.exception.ComplaintNotFoundException;
import com.example.demo.exception.OfficerNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.OfficerDetailsRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.DTO.OfficerDetailsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfficerDetailsService {

	@Autowired
    private ComplaintRepository complaintRepository;
	
    @Autowired
    private OfficerDetailsRepository officerRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a new officer
    public OfficerDetails addOfficer(OfficerDetails officerDetails) {
        User user = officerDetails.getUser();
        
        if (user == null) {
            throw new RuntimeException("User must not be null when adding an officer.");
        }

        Long userId = user.getId();
        
        // Ensure user exists or save the user
        if (userId == null || !userRepository.existsById(userId)) {
            user = userRepository.save(user); // Save the user if it doesn't exist
        } else {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));  // Throw custom exception if user not found
        }

        officerDetails.setUser(user);  // Associate the officer with the user
        return officerRepository.save(officerDetails);  // Save and return officer details
    }

    // Update officer details
    public OfficerDetails updateOfficer(Long id, OfficerDetails updatedDetails) {
        OfficerDetails officer = officerRepository.findById(id)
                .orElseThrow(() -> new OfficerNotFoundException(id));

        // Update officer fields
        officer.setName(updatedDetails.getName());
        officer.setLocation(updatedDetails.getLocation());
        officer.setActive(updatedDetails.isActive());
        officer.setDepartment(updatedDetails.getDepartment());
        officer.setBadgeNumber(updatedDetails.getBadgeNumber());

        // Save and return updated officer details
        return officerRepository.save(officer);
    }

    // Delete officer by ID
    public void deleteOfficer(Long id) {
        if (!officerRepository.existsById(id)) {
            throw new OfficerNotFoundException(id);
        }
        officerRepository.deleteById(id); // Delete officer by ID
    }

    // Get all officers
    public List<OfficerDetailsDTO> getAllOfficers() {
        List<OfficerDetails> officers = officerRepository.findAll();
        
        return officers.stream()
                .map(this::convertToDTO)  // Convert each officer to DTO
                .collect(Collectors.toList()); // Return list of DTOs
    }

    // Get officer by ID
 // Get officer by ID
    public OfficerDetailsDTO getOfficerById(Long id) {
        OfficerDetails officer = officerRepository.findById(id)
                .orElseThrow(() -> new OfficerNotFoundException(id)); // Now the exception correctly mentions ID
        return convertToDTO(officer);  // Convert officer entity to DTO and return
    }


    public OfficerDetails getOfficerForComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ComplaintNotFoundException(complaintId));

        // Return the officer assigned to the complaint
        return complaint.getOfficerDetails();
    }

    
    // Convert OfficerDetails to OfficerDetailsDTO
    private OfficerDetailsDTO convertToDTO(OfficerDetails officer) {
        OfficerDetailsDTO officerDTO = new OfficerDetailsDTO();
        officerDTO.setId(officer.getId());
        officerDTO.setName(officer.getName());
        officerDTO.setLocation(officer.getLocation());
        officerDTO.setActive(officer.isActive());
        officerDTO.setDepartment(officer.getDepartment());
        officerDTO.setBadgeNumber(officer.getBadgeNumber());

        // Set the user ID separately (instead of nesting UserDTO)
        officerDTO.setUserId(officer.getUser().getId());

        return officerDTO;
    }
}
