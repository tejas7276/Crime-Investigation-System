package com.example.demo.service;

import com.example.demo.entity.EmergencyReport;
import com.example.demo.repository.EmergencyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmergencyReportService {
    
    @Autowired
    private EmergencyReportRepository repository;
    
    public EmergencyReport createEmergency(Long userId, String location) {
        EmergencyReport report = new EmergencyReport();
        report.setUserId(userId);
        report.setLocation(location);
        report.setCreatedAt(LocalDateTime.now());
        return repository.save(report);
    }
    
    public EmergencyReport markAsResponded(Long reportId) {
        EmergencyReport report = repository.findById(reportId).orElseThrow();
        report.setStatus("RESPONDED");
        report.setRespondedAt(LocalDateTime.now());
        return repository.save(report);
    }
    
	public List<EmergencyReport> getActiveEmergencies() {
        return repository.findByStatus("URGENT");
    }
}
