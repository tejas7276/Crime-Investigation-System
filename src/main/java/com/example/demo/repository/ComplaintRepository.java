package com.example.demo.repository;

import com.example.demo.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
	// Change from findByOfficerId to findByOfficerDetailsId
	List<Complaint> findByOfficerDetailsId(Long officerId);

	// Also update the query method
	@Query("SELECT c FROM Complaint c WHERE c.officerDetails.id = :officerId AND LOWER(c.status) = LOWER(:status)")
	List<Complaint> findByOfficerDetailsIdAndStatus(@Param("officerId") Long officerId, @Param("status") String status);

	// Keep other methods the same
	List<Complaint> findByUserId(Long userId);

	int countByUserId(Long userId);

	@Query("SELECT COUNT(c) FROM Complaint c WHERE c.user.id = :userId AND LOWER(c.status) = LOWER(:status)")
	int countByStatus(@Param("userId") Long userId, @Param("status") String status);
}