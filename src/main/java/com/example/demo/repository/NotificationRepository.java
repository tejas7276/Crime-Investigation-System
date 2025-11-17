package com.example.demo.repository;

import com.example.demo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Paginated version of the method to fetch notifications by userId
    Page<Notification> findByUserId(Long userId, Pageable pageable);

    // Count unread notifications for a specific user
    long countByUserIdAndStatus(Long userId, String status);
    
    Page<Notification> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
}
