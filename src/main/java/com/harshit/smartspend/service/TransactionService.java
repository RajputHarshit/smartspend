package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {

    TransactionResponseDto createTransaction(Long userId, TransactionRequestDto requestDto);
    Page<TransactionResponseDto> getTransactionsByUser(Long userId, Pageable pageable);
}
