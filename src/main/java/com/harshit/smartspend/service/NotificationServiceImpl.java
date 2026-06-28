package com.harshit.smartspend.service;

import com.harshit.smartspend.entity.Notification;
import com.harshit.smartspend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;
    @Override
    public List<Notification> getNotificationByUserId(Long userId) {
      return notificationRepository.findByUserId(userId);
    }
}
