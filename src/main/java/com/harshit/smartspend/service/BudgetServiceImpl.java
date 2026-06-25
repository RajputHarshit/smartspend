package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.BudgetRequestDto;
import com.harshit.smartspend.dto.BudgetResponseDto;
import com.harshit.smartspend.entity.Budget;
import com.harshit.smartspend.entity.Category;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.BudgetRepository;
import com.harshit.smartspend.repository.CategoryRepository;
import com.harshit.smartspend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private BudgetRepository budgetRepository;

    public BudgetServiceImpl(UserRepository userRepository, BudgetRepository budgetRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BudgetResponseDto createBudget(Long userId,BudgetRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not Found"));
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));

        boolean duplicate = budgetRepository.gitexistsByUserIdAndCategoryIdAndMonthYear(userId, request.getCategoryId(), request.getMonthYear());
        if (duplicate) {
            throw new RuntimeException("Budget already exists for this category and month");
        }
        Budget budget = Budget.builder().limitAmount(request.getLimitAmount())
                .monthYear(request.getMonthYear())
                .user(user)
                .category(category)
                .build();
        Budget saved = budgetRepository.save(budget);
        return mapToResponse(saved);
    }


    @Override
    public List<BudgetResponseDto> getBudgetsByUserId(Long userId) {

        return budgetRepository.findByUserId(userId).stream()
                .map(this::mapToResponse).toList();

    }


    private BudgetResponseDto mapToResponse(Budget budget) {
        return BudgetResponseDto.builder()
                .id(budget.getId())
                .limitAmount(budget.getLimitAmount())
                .monthYear(budget.getMonthYear())
                .userName(budget.getUser().getName())
                .categoryName(budget.getCategory().getName())
                .createdAt(budget.getCreatedAt())
                .build();
    }

}
