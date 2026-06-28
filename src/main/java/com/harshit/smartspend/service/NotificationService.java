package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.BudgetResponseDto;
import com.harshit.smartspend.entity.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getNotificationByUserId(Long userId);
}
