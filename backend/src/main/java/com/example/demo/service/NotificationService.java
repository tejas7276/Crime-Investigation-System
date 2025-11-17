package com.example.demo.service;

import com.example.demo.DTO.NotificationDTO;
import com.example.demo.entity.Complaint;
import com.example.demo.entity.Notification;
import com.example.demo.entity.User;
import com.example.demo.exception.ComplaintNotFoundException;
import com.example.demo.exception.NotificationNotFoundException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.repository.ComplaintRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Save a notification for a user
    public void saveNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setStatus("unread");
        notificationRepository.save(notification);
    }

    // ✅ Get user notifications sorted by latest
    public List<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        Page<Notification> notificationsPage = notificationRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
        return notificationsPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ Mark a single notification as read and return updated unread count
    @Transactional
    public Map<String, Object> markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));

        notification.setStatus("read");
        notificationRepository.save(notification);

        long unreadCount = getUnreadCount(notification.getUser().getId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Notification marked as read.");
        response.put("unreadCount", unreadCount);
        return response;
    }

    // ✅ Get unread notification count for a user
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndStatus(userId, "unread");
    }

    // ✅ Send a notification to all admin users
    public void sendNotificationToAdmin(String message) {
        List<User> adminUsers = userRepository.findByRole("ADMIN");

        if (adminUsers.isEmpty()) {
            throw new UserNotFoundException("ADMIN");
        }

        for (User admin : adminUsers) {
            Notification notification = new Notification();
            notification.setUser(admin);
            notification.setMessage(message);
            notification.setTimestamp(LocalDateTime.now());
            notification.setStatus("unread");
            notificationRepository.save(notification);
        }
    }

    // ✅ Update complaint status and notify user
    public void updateComplaintStatus(Long complaintId, String status, Long userId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ComplaintNotFoundException(complaintId));

        complaint.setStatus(status);
        complaintRepository.save(complaint);

        String message = "Your complaint #" + complaintId + " has been updated to status: " + status;
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        saveNotification(user, message);
    }

    // ✅ Convert entity to DTO
    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getId(),
                notification.getMessage(),
                notification.getTimestamp(),
                notification.getStatus(),
                notification.getUser().getId(),
                notification.getOfficer() != null ? notification.getOfficer().getId() : null,
                notification.getAdmin() != null ? notification.getAdmin().getId() : null
        );
    }
}
