package com.harshit.smartspend.consumer;


import com.harshit.smartspend.dto.TransactionEventDto;
import com.harshit.smartspend.entity.Budget;
import com.harshit.smartspend.entity.Notification;
import com.harshit.smartspend.repository.BudgetRepository;
import com.harshit.smartspend.repository.NotificationRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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

    @Test
    public void budgetExceededNotificationSaved(){
        Budget budget= new Budget();
        budget.setLimitAmount(BigDecimal.valueOf(1000));
        when(budgetRepository.findByUserIdAndCategoryIdAndMonthYear(1L,1L,"2026-06"))
                .thenReturn(Optional.of(budget));
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 0, 0);

        when(transactionRepository.sumExpensesForUserCategoryInRange(1L, 1L, start, end))
                .thenReturn(BigDecimal.valueOf(900));
        TransactionEventDto event = TransactionEventDto.builder()
                .userId(1L)
                .categoryId(1L)
                .amount(BigDecimal.valueOf(500))
                .monthYear("2026-06")
                .build();
        budgetAlertConsumer.handleTransactionEvent(event);
        verify(notificationRepository, times(1)).save(any());
    }
    @Test
    public void budgetNotExceededNoNotificationSaved(){
        Budget budget= new Budget();
        budget.setLimitAmount(BigDecimal.valueOf(1000));
        when(budgetRepository.findByUserIdAndCategoryIdAndMonthYear(1L,1L,"2026-06"))
                .thenReturn(Optional.of(budget));
        LocalDateTime start = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 7, 1, 0, 0);

        when(transactionRepository.sumExpensesForUserCategoryInRange(1L, 1L, start, end))
                .thenReturn(BigDecimal.valueOf(700));
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
