package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.BudgetRequestDto;
import com.harshit.smartspend.dto.BudgetResponseDto;
import com.harshit.smartspend.dto.PagedResponse;
import com.harshit.smartspend.dto.TransactionResponseDto;
import com.harshit.smartspend.service.BudgetService;
import com.harshit.smartspend.service.UserService;
import com.harshit.smartspend.util.PaginationConstants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Operation(summary = "Create a new budget for a category")
    @PostMapping
    public ResponseEntity<BudgetResponseDto> createBudget(@Valid @RequestBody BudgetRequestDto request,@AuthenticationPrincipal UserDetails userDetails) {
        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(budgetService.createBudget(userId,request));
    }
    @Operation(summary = "Get all budgets for the logged-in user")
    @GetMapping("/user/my")
    public ResponseEntity<PagedResponse<BudgetResponseDto>> getBudgetsByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                    @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
                                                                    @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
                                                                    @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
                                                                    @RequestParam(defaultValue = PaginationConstants.DEFAULT_DIRECTION) String direction) {

     Sort sort= direction.equalsIgnoreCase("asc")
             ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(page,size,sort);

        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        Page<BudgetResponseDto> pageResult= budgetService.getBudgetsByUser(userId,pageable);
        PagedResponse<BudgetResponseDto> response = new PagedResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
        return ResponseEntity.ok(response);
    }
}