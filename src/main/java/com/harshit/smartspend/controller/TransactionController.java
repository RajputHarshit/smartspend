package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import com.harshit.smartspend.service.TransactionService;
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
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(@Valid @RequestBody TransactionRequestDto requestDto, @AuthenticationPrincipal UserDetails userDetails) {
        Long userId= userService.getUserIdByEmail(userDetails.getUsername());
        TransactionResponseDto response = transactionService.createTransaction(userId,requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/user/my")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByUser( @AuthenticationPrincipal UserDetails userDetails) {
        Long userId= userService.getUserIdByEmail(userDetails.getUsername());
        List<TransactionResponseDto> response = transactionService.getTransactionsByUser(userId);
        return ResponseEntity.ok(response);
    }
}
