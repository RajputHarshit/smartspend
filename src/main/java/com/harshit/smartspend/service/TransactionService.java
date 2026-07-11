package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.PagedResponse;
import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionResponseDto createTransaction(Long userId, TransactionRequestDto requestDto);
    PagedResponse<TransactionResponseDto> getTransactionsByUser(Long userId, Pageable pageable);
    TransactionResponseDto updateTransaction(Long userId, Long transactionId, TransactionRequestDto requestDto);
    void deleteTransaction(Long userId, Long transactionId);
}