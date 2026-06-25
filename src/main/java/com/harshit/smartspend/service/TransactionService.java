package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;

import java.util.List;

public interface TransactionService {

    TransactionResponseDto createTransaction(Long userId, TransactionRequestDto requestDto);
    List<TransactionResponseDto> getTransactionsByUser(Long userId);
}
