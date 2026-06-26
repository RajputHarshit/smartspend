package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import com.harshit.smartspend.entity.Category;
import com.harshit.smartspend.entity.Transaction;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.CategoryRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import com.harshit.smartspend.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @CacheEvict(value = "transactions", key = "#userId")
    public TransactionResponseDto createTransaction(Long userId,TransactionRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found: " + requestDto.getCategoryId()));
        Transaction transaction = Transaction.builder()
                .amount(requestDto.getAmount())
                .type(requestDto.getType())
                .note(requestDto.getNote())
                .user(user)
                .category(category)
                .build();
       Transaction saved =transactionRepository.save(transaction);
        return mapToResponse(saved);
    }

    @Override
    @Cacheable(value = "transactions",  key="#userId")
    public List<TransactionResponseDto> getTransactionsByUser(Long userId) {
        return transactionRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }
    private TransactionResponseDto mapToResponse(Transaction t) {
        return TransactionResponseDto.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType())
                .note(t.getNote())
                .createdAt(t.getCreatedAt())
                .userName(t.getUser().getName())
                .categoryName(t.getCategory().getName())
                .build();
    }
}
