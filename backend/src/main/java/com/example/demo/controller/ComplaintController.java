package com.example.demo.controller;

import com.example.demo.DTO.ComplaintDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

	@Autowired
	private ComplaintService complaintService;

	@PostMapping("/file/{userId}")
	public ResponseEntity<ComplaintDTO> fileComplaint(@PathVariable Long userId, @RequestBody Complaint complaint) {
		ComplaintDTO complaintDTO = complaintService.fileComplaint(userId, complaint);
		return ResponseEntity.ok(complaintDTO);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ComplaintDTO>> getComplaintsByUser(@PathVariable Long userId) {
		List<ComplaintDTO> complaints = complaintService.getComplaintsByUser(userId);
		return ResponseEntity.ok(complaints);
	}

	@GetMapping("/view-all")
	public ResponseEntity<List<ComplaintDTO>> viewComplaints(
	    @RequestParam(value = "userId", required = false) Long userId,
	    @RequestParam(value = "officerId", required = false) Long officerId,
	    @RequestParam("role") String role
	) {
	    List<ComplaintDTO> complaints;

	    if ("ADMIN".equalsIgnoreCase(role)) {
	        complaints = complaintService.viewAllCrimes(null, "ADMIN");
	    } else if ("OFFICER".equalsIgnoreCase(role)) {
	        complaints = complaintService.getComplaintsByOfficer(officerId); // ✅ Use officerId here
	    } else {
	        complaints = complaintService.getComplaintsByUser(userId); // ✅ Use userId for citizens
	    }

	    return ResponseEntity.ok(complaints);
	}


	@GetMapping("/officer/{officerId}")
	public ResponseEntity<List<ComplaintDTO>> getComplaintsByOfficer(@PathVariable Long officerId) {
		List<ComplaintDTO> complaints = complaintService.getComplaintsByOfficer(officerId);
		return ResponseEntity.ok(complaints);
	}

	@PutMapping("/status/{complaintId}")
	public ResponseEntity<Map<String, Object>> updateComplaintStatus(@PathVariable Long complaintId,
			@RequestBody String status) {
		Map<String, Object> response = complaintService.updateComplaintStatus(complaintId, status.replace("\"", ""));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/user/stats/{userId}")
	public ResponseEntity<Map<String, Integer>> getStats(@PathVariable Long userId) {
		int total = complaintService.countTotal(userId);
		int resolved = complaintService.countResolved(userId);
		int pending = total - resolved;

		Map<String, Integer> stats = new HashMap<>();
		stats.put("total", total);
		stats.put("resolved", resolved);
		stats.put("pending", pending);

		return ResponseEntity.ok(stats);
	}

}
