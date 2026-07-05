package com.harshit.smartspend.consumer;


import com.harshit.smartspend.dto.TransactionEventDto;
import com.harshit.smartspend.entity.Notification;
import com.harshit.smartspend.repository.BudgetRepository;
import com.harshit.smartspend.repository.NotificationRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BudgetAlertConsumerTest {
    @Mock
    private  BudgetRepository budgetRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private BudgetAlertConsumer budgetAlertConsumer;

    @Test
    public void noBudgetNoNotification(){
        when(budgetRepository.findByUserIdAndCategoryIdAndMonthYear(1L,1L,"2026-06"))
                .thenReturn(Optional.empty());
        TransactionEventDto event = TransactionEventDto.builder()
                .userId(1L)
                .categoryId(1L)
                .amount(BigDecimal.valueOf(500))
                .monthYear("2026-06")
                .build();
        budgetAlertConsumer.handleTransactionEvent(event);
        verify(notificationRepository, never()).save(any());
    }
}
