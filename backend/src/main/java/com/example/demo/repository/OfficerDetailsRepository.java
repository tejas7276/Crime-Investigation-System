package com.example.demo.repository;

import com.example.demo.entity.OfficerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OfficerDetailsRepository extends JpaRepository<OfficerDetails, Long> {
    
    OfficerDetails findTopByDepartmentOrderByIdAsc(String department);

    // ✨ New — get active officers from a specific location
    List<OfficerDetails> findByLocationAndIsActiveTrue(String location);

    // ✨ New — find officer by userId (reverse linking Officer <-> User)
    OfficerDetails findByUserId(Long userId);
}
