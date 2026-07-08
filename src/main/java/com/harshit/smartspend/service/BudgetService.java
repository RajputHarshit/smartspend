package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.BudgetRequestDto;
import com.harshit.smartspend.dto.BudgetResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface BudgetService {

    BudgetResponseDto createBudget(Long userId,BudgetRequestDto budgetRequestDto);

    List<BudgetResponseDto> getBudgetsByUserId(Long userId);
    Page<BudgetResponseDto> getBudgetsByUser(Long userId, Pageable pageable);
}
