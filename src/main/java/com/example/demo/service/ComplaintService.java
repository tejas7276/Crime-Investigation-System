package com.example.demo.service;

import com.example.demo.DTO.ComplaintDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.OfficerDetails;
import com.example.demo.entity.User;
import com.example.demo.exception.ComplaintNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.OfficerDetailsRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.example.demo.exception.OfficerNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComplaintService {

	@Autowired
	private ComplaintRepository complaintRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OfficerDetailsRepository officerDetailsRepository;

	@Autowired
	private NotificationService notificationService;

	@Autowired
	@Qualifier("preferredDepartmentMapping")
	private Map<String, String> crimeTypeToDepartment;

	public ComplaintDTO fileComplaint(Long userId, Complaint complaint) {
		// Check if user exists, otherwise throw an exception
		User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

		// Set the current date and time
		complaint.setDate(LocalDate.now());
		complaint.setTime(LocalTime.now());

		String crimeType = complaint.getType();
		if (crimeType == null || crimeType.trim().isEmpty()) {
			throw new RuntimeException("Crime type cannot be null or empty.");
		}

		// Check if the department is mapped to the crime type, otherwise throw an
		// exception
		String department = crimeTypeToDepartment.get(crimeType.toLowerCase());
		if (department == null) {
			throw new RuntimeException("No department mapped for crime type: " + crimeType);
		}

		// Find officer for the mapped department
		OfficerDetails assignedOfficer = officerDetailsRepository.findTopByDepartmentOrderByIdAsc(department);

		if (assignedOfficer == null) {
			// If no officer is found, mark the complaint as "Pending Assignment"
			complaint.setStatus("Pending Assignment");

			// Save the complaint with "Pending Assignment" status
			complaint.setUser(user);
			complaint.setOfficerDetails(null); // No officer assigned
			Complaint savedComplaint = complaintRepository.save(complaint);

			// Notify the admin about the pending assignment
			String message = "Complaint #" + savedComplaint.getId()
					+ " is unassigned. Please manually assign an officer.";
			notificationService.sendNotificationToAdmin(message);

			// Notify the user about the pending status
			notificationService.saveNotification(user,
					"Your complaint has been filed and is currently pending assignment.");

			return mapToDTO(savedComplaint);
		}

		// If an officer is assigned, proceed with the normal process
		complaint.setOfficerDetails(assignedOfficer);
		complaint.setStatus("Filed");

		// Save the complaint and notify user and officer
		Complaint savedComplaint = complaintRepository.save(complaint);
		notificationService.saveNotification(user,
				"Your complaint has been filed. Status: " + savedComplaint.getStatus());

		if (assignedOfficer != null && assignedOfficer.getUser() != null) {
			notificationService.saveNotification(assignedOfficer.getUser(), "New complaint assigned to you.");
		}

		return mapToDTO(savedComplaint);
	}

	public List<ComplaintDTO> viewAllCrimes(Long userId, String role) {
		List<Complaint> complaints;

		if ("ADMIN".equals(role)) {
			complaints = complaintRepository.findAll();
		}
		// Officer can view complaints assigned to them
		else if ("OFFICER".equals(role)) {
			complaints = complaintRepository.findByOfficerDetailsId(userId);
		}
		// Citizen can view only their complaints
		else {
			complaints = complaintRepository.findByUserId(userId);
		}

		return complaints.stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	public List<ComplaintDTO> getComplaintsByUser(Long userId) {
		return complaintRepository.findByUserId(userId).stream().map(this::mapToDTO).collect(Collectors.toList());
	}

	public List<ComplaintDTO> getComplaintsByOfficer(Long officerId) {
	    // Find officer by officerId, if not found, throw an exception
	    OfficerDetails officer = officerDetailsRepository.findById(officerId)
	            .orElseThrow(() -> new OfficerNotFoundException(officerId)); // This will now use your custom exception

	    // Return complaints assigned to the officer
	    return complaintRepository.findByOfficerDetailsId(officerId).stream()
	            .map(this::mapToDTO)
	            .collect(Collectors.toList());
	}

	
	public int countResolved(Long userId) {
		return complaintRepository.countByStatus(userId, "Resolved");
	}

	public int countPending(Long userId) {
		return complaintRepository.countByStatus(userId, "Pending");
	}

	public int countTotal(Long userId) {
		return complaintRepository.countByUserId(userId);
	}

	@Transactional
	public Map<String, Object> updateComplaintStatus(Long complaintId, String status) {
		// Find complaint by ID, if not found, throw an exception
		Complaint complaint = complaintRepository.findById(complaintId)
				.orElseThrow(() -> new ComplaintNotFoundException(complaintId));

		// Update the status and save the complaint
		complaint.setStatus(status);
		notificationService.saveNotification(complaint.getUser(),
				"Your complaint #" + complaintId + " is now " + status);

		Complaint updatedComplaint = complaintRepository.save(complaint);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Complaint status updated successfully.");
		response.put("complaint", mapToDTO(updatedComplaint));
		response.put("timestamp", LocalDate.now());

		return response;
	}

	private ComplaintDTO mapToDTO(Complaint complaint) {
		ComplaintDTO dto = new ComplaintDTO();
		dto.setId(complaint.getId());
		dto.setType(complaint.getType());
		dto.setDescription(complaint.getDescription());
		dto.setLocation(complaint.getLocation());
		dto.setStatus(complaint.getStatus());
		dto.setDate(complaint.getDate());
		dto.setTime(complaint.getTime());

		// Setting only IDs for user and officer details
		if (complaint.getUser() != null) {
			dto.setUserId(complaint.getUser().getId());
		}
		if (complaint.getOfficerDetails() != null && complaint.getOfficerDetails().getUser() != null) {
		    dto.setOfficerId(complaint.getOfficerDetails().getId());
		    dto.setOfficername(complaint.getOfficerDetails().getName());
		}

		return dto;
	}
}
