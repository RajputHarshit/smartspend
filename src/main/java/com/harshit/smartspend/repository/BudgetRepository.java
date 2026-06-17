package com.harshit.smartspend.repository;

import com.harshit.smartspend.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget,Long> {

    List<Budget> findByUserId(Long userId);

    boolean existsByUserIdAndCategoryIdAndMonthYear(
            Long userId, Long categoryId, String monthYear);
}
