package com.example.demo.controller;

import com.example.demo.service.OfficerDetailsService;
import com.example.demo.DTO.OfficerDetailsDTO;
import com.example.demo.entity.OfficerDetails;
import com.example.demo.exception.OfficerNotFoundException;
import com.example.demo.exception.UserValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officers")
public class OfficerDetailsController {

    @Autowired
    private OfficerDetailsService officerDetailsService;

    // Add a new officer
    @PostMapping("/add")
    public ResponseEntity<OfficerDetails> addOfficer(@RequestBody OfficerDetails officerDetails) {
        try {
            OfficerDetails createdOfficer = officerDetailsService.addOfficer(officerDetails);
            return new ResponseEntity<>(createdOfficer, HttpStatus.CREATED);
        } catch (UserValidationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update officer details
    @PutMapping("/{id}/update")
    public ResponseEntity<OfficerDetails> updateOfficer(@PathVariable Long id, @RequestBody OfficerDetails updatedDetails) {
        try {
            OfficerDetails updatedOfficer = officerDetailsService.updateOfficer(id, updatedDetails);
            return new ResponseEntity<>(updatedOfficer, HttpStatus.OK);
        } catch (OfficerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete officer by ID
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteOfficer(@PathVariable Long id) {
        try {
            officerDetailsService.deleteOfficer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (OfficerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all officers
    @GetMapping("/all")
    public ResponseEntity<List<OfficerDetailsDTO>> getAllOfficers() {
        List<OfficerDetailsDTO> officers = officerDetailsService.getAllOfficers();
        return new ResponseEntity<>(officers, HttpStatus.OK);
    }

    // Get officer by ID
    @GetMapping("/{id}")
    public ResponseEntity<OfficerDetailsDTO> getOfficerById(@PathVariable Long id) {
        try {
            OfficerDetailsDTO officer = officerDetailsService.getOfficerById(id);
            return new ResponseEntity<>(officer, HttpStatus.OK);
        } catch (OfficerNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
