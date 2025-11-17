package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.EmergencyReport;

import java.util.List;

public interface EmergencyReportRepository extends JpaRepository<EmergencyReport, Long> {
    List<EmergencyReport> findByStatus(String status);
    List<EmergencyReport> findByUserId(Long userId);
}
