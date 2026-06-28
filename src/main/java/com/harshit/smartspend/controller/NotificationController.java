package com.harshit.smartspend.controller;

import com.harshit.smartspend.entity.Notification;
import com.harshit.smartspend.service.NotificationService;
import com.harshit.smartspend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final UserService userService;

    @GetMapping("/my")
    public ResponseEntity<List<Notification>> getTransactionsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        List<Notification> response = notificationService.getNotificationByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
