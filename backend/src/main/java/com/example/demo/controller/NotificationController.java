package com.example.demo.controller;

import com.example.demo.DTO.NotificationDTO;
import com.example.demo.entity.Notification;
import com.example.demo.service.NotificationService;
import com.example.demo.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ComplaintService complaintService;

    // ✅ Get paginated notifications for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Pageable pageable = PageRequest.of(page, size);
            List<NotificationDTO> notifications = notificationService.getUserNotifications(userId, pageable);
            if (notifications.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(notifications);
            }
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ Mark as read + return updated unread count
    @PutMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable Long notificationId) {
        try {
            Map<String, Object> response = notificationService.markAsRead(notificationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error marking notification as read: " + e.getMessage()));
        }
    }

    // ✅ Update complaint status & notify user
    @PutMapping("/update-complaint-status/{complaintId}")
    public ResponseEntity<String> updateComplaintStatus(
            @PathVariable Long complaintId,
            @RequestParam String status,
            @RequestParam Long userId) {

        try {
            complaintService.updateComplaintStatus(complaintId, status);
            notificationService.updateComplaintStatus(complaintId, status, userId);
            return ResponseEntity.ok("Complaint status updated and notification sent.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating complaint status: " + e.getMessage());
        }
    }

    // ✅ Unread notification count for a user
    @GetMapping("/user/{userId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable Long userId) {
        try {
            long unreadCount = notificationService.getUnreadCount(userId);
            return ResponseEntity.ok(unreadCount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ Send notification to all admins
    @PostMapping("/send-to-admins")
    public ResponseEntity<String> sendToAdmins(@RequestParam String message) {
        try {
            notificationService.sendNotificationToAdmin(message);
            return ResponseEntity.ok("Notification sent to all admins.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending to admins: " + e.getMessage());
        }
    }
}
