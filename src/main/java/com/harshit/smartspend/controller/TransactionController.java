package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.PagedResponse;
import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import com.harshit.smartspend.service.TransactionService;
import com.harshit.smartspend.service.UserService;
import com.harshit.smartspend.util.PaginationConstants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        TransactionResponseDto response = transactionService.createTransaction(userId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/my")
    public ResponseEntity<PagedResponse<TransactionResponseDto>> getTransactionsByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                                       @RequestParam(defaultValue = PaginationConstants.DEFAULT_PAGE) int page,
                                                                                       @RequestParam(defaultValue = PaginationConstants.DEFAULT_SIZE) int size,
                                                                                       @RequestParam(defaultValue = PaginationConstants.DEFAULT_SORT_BY) String sortBy,
                                                                                       @RequestParam(defaultValue = PaginationConstants.DEFAULT_DIRECTION) String direction) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(transactionService.getTransactionsByUser(userId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> updateTransaction(@PathVariable Long id,
                                                                    @Valid @RequestBody TransactionRequestDto requestDto,
                                                                    @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        TransactionResponseDto response = transactionService.updateTransaction(userId, id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdByEmail(userDetails.getUsername());
        transactionService.deleteTransaction(userId, id);
        return ResponseEntity.noContent().build();
    }
}