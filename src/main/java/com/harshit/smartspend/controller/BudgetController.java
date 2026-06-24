package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.BudgetRequestDto;
import com.harshit.smartspend.dto.BudgetResponseDto;
import com.harshit.smartspend.service.BudgetService;
import com.harshit.smartspend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(@Valid @RequestBody BudgetRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(request));
    }

    @GetMapping("/user/my")
    public ResponseEntity<List<BudgetResponseDto>> getBudgetsByUser(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(budgetService.getBudgetsByUserId(userId));
    }
}