package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.BudgetRequestDto;
import com.harshit.smartspend.dto.BudgetResponseDto;
import com.harshit.smartspend.entity.Budget;

import java.util.List;

public interface BudgetService {

    BudgetResponseDto createBudget(BudgetRequestDto budgetRequestDto);

    List<BudgetResponseDto> getBudgetsByUserId(Long userId);
}
