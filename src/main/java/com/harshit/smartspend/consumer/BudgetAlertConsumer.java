package com.harshit.smartspend.consumer;

import com.harshit.smartspend.dto.TransactionEventDto;
import com.harshit.smartspend.entity.Budget;
import com.harshit.smartspend.entity.Notification;
import com.harshit.smartspend.repository.BudgetRepository;
import com.harshit.smartspend.repository.NotificationRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BudgetAlertConsumer {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "transaction-created-topic", groupId = "smartspend-group")
    public void handleTransactionEvent(TransactionEventDto event){
        YearMonth yearMonth = YearMonth.parse(event.getMonthYear());
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

      Optional<Budget> budgetOpt = budgetRepository.
              findByUserIdAndCategoryIdAndMonthYear(event.getUserId(), event.getCategoryId(),event.getMonthYear()) ;
        if (budgetOpt.isEmpty()) {
            return;
        }
        Budget budget = budgetOpt.get();

        BigDecimal totalSpent = transactionRepository.sumExpensesForUserCategoryInRange(
                event.getUserId(), event.getCategoryId(), start, end);

        BigDecimal percentageUsed = totalSpent
                .divide(budget.getLimitAmount(), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        if (percentageUsed.compareTo(BigDecimal.valueOf(80)) >= 0) {

         Notification newAlert =  new Notification();
            newAlert.setUserId(event.getUserId());
            newAlert.setMessage("ALERT: User " + event.getUserId() +
                    " has used " + percentageUsed + "% of budget for category " +
                    event.getCategoryId() + " in " + event.getMonthYear());
            newAlert.setRead(false);

         notificationRepository.save(newAlert);
            System.out.println("ALERT: User " + event.getUserId() +
                    " has used " + percentageUsed + "% of budget for category " +
                    event.getCategoryId() + " in " + event.getMonthYear());
        }

        System.out.println("Received event "+ event);
    }
}
