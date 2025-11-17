package com.example.demo.service;

import com.example.demo.entity.OfficerDetails;
import com.example.demo.entity.Complaint;
import com.example.demo.repository.OfficerDetailsRepository;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.config.DepartmentMappingConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class OfficerAssignmentService {

    @Autowired
    private OfficerDetailsRepository officerDetailsRepository;

    @Autowired
    private DepartmentMappingConfig departmentMappingConfig;

    @Autowired
    private ComplaintRepository complaintRepository;

    // Assign officer based on complaint type
    public void assignOfficerToComplaint(Complaint complaint) {
        String complaintType = complaint.getType().toLowerCase();

        // Get preferred department
        Map<String, String> preferredDepartmentMapping = departmentMappingConfig.crimeTypeToPreferredDepartment();
        String preferredDepartment = preferredDepartmentMapping.get(complaintType);

        // Get fallback department
        Map<String, String> fallbackDepartmentMapping = departmentMappingConfig.crimeTypeToFallbackDepartment();
        String fallbackDepartment = fallbackDepartmentMapping.get(complaintType);

        // Try to assign an officer in the preferred department
        OfficerDetails assignedOfficer = officerDetailsRepository.findTopByDepartmentOrderByIdAsc(preferredDepartment);

        // If no officer in the preferred department, try the fallback department
        if (assignedOfficer == null) {
            assignedOfficer = officerDetailsRepository.findTopByDepartmentOrderByIdAsc(fallbackDepartment);
        }

        // If no officer available in both, mark the complaint as unassigned
        if (assignedOfficer == null) {
            complaint.setStatus("Unassigned");
            complaintRepository.save(complaint);
            // Optionally, notify admin here
            // notifyAdminAboutUnassignedComplaint(complaint);
        } else {
            // If officer found, assign the officer to the complaint
            complaint.setAssignedOfficer(assignedOfficer);
            complaint.setStatus("Assigned");
            complaintRepository.save(complaint);
        }
    }

    // Optional method to notify the admin if no officer is available
    private void notifyAdminAboutUnassignedComplaint(Complaint complaint) {
        // Your logic to send notification to admin
        System.out.println("No officer available for complaint ID: " + complaint.getId() + " with type: " + complaint.getType());
    }
}
