package com.harshit.smartspend.repository;

import com.harshit.smartspend.entity.Category;
import com.harshit.smartspend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.category.id = :categoryId " +
            "AND t.type = 'EXPENSE' AND t.createdAt >= :start AND t.createdAt < :end")
    BigDecimal sumExpensesForUserCategoryInRange(
            @Param("userId") Long userId,
            @Param("categoryId") Long categoryId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

     interface CategoryExpenseSummary {
        Category getCategory();
        BigDecimal getTotalSpent();
    }

    @Query("SELECT t.category AS category, COALESCE(SUM(t.amount), 0) AS totalSpent FROM Transaction t " +
            "WHERE t.user.id = :userId AND t.type = 'EXPENSE' " +
            "AND t.createdAt >= :start AND t.createdAt < :end " +
            "GROUP BY t.category")
    List<CategoryExpenseSummary> sumExpensesGroupedByCategory(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

}
