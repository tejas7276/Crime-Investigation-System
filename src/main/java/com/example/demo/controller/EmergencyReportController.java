package com.example.demo.controller;

import com.example.demo.entity.EmergencyReport;
import com.example.demo.service.EmergencyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyReportController {
    
    @Autowired
    private EmergencyReportService service;
    
    @PostMapping
    public ResponseEntity<EmergencyReport> reportEmergency(
        @RequestParam Long userId,
        @RequestParam(required = false) String location
    ) {
        return ResponseEntity.ok(service.createEmergency(userId, location));
    }
    
    @PatchMapping("/{id}/respond")
    public ResponseEntity<EmergencyReport> markResponded(@PathVariable Long id) {
        return ResponseEntity.ok(service.markAsResponded(id));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<EmergencyReport>> getActive() {
        return ResponseEntity.ok(service.getActiveEmergencies());
    }
}
