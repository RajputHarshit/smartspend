package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.BudgetRequestDto;
import com.harshit.smartspend.dto.BudgetResponseDto;


import java.util.List;

public interface BudgetService {

    BudgetResponseDto createBudget(Long userId,BudgetRequestDto budgetRequestDto);

    List<BudgetResponseDto> getBudgetsByUserId(Long userId);
}
