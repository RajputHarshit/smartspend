package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.PagedResponse;
import com.harshit.smartspend.dto.TransactionEventDto;
import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import com.harshit.smartspend.entity.Category;
import com.harshit.smartspend.entity.Transaction;
import com.harshit.smartspend.entity.TransactionType;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.exceptions.ResourceNotFoundException;
import com.harshit.smartspend.exceptions.UnauthorizedActionException;
import com.harshit.smartspend.repository.CategoryRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import com.harshit.smartspend.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private static final String TRANSACTION_TOPIC = "transaction-created-topic";
    private final KafkaTemplate<String, TransactionEventDto> kafkaTemplate;

    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, CategoryRepository categoryRepository, KafkaTemplate<String, TransactionEventDto> kafkaTemplate) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public TransactionResponseDto createTransaction(Long userId, TransactionRequestDto requestDto) {
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
        Transaction savedTransaction = transactionRepository.save(transaction);
        if (savedTransaction.getType() == TransactionType.EXPENSE) {
            TransactionEventDto event = TransactionEventDto.builder()
                    .userId(savedTransaction.getUser().getId())
                    .categoryId(savedTransaction.getCategory().getId())
                    .amount(savedTransaction.getAmount())
                    .monthYear(savedTransaction.getCreatedAt()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM")))
                    .build();

            kafkaTemplate.send(TRANSACTION_TOPIC, event);
        }
        return mapToResponse(savedTransaction);
    }

    @Override
    @Cacheable(value = "transactions", key = "#userId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort")
    public PagedResponse<TransactionResponseDto> getTransactionsByUser(Long userId, Pageable pageable) {
        Page<TransactionResponseDto> pageResult = transactionRepository.findByUserId(userId, pageable)
                .map(this::mapToResponse);

        return new PagedResponse<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public TransactionResponseDto updateTransaction(Long userId, Long transactionId, TransactionRequestDto requestDto) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("You are not allowed to update this transaction");
        }

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + requestDto.getCategoryId()));

        transaction.setAmount(requestDto.getAmount());
        transaction.setType(requestDto.getType());
        transaction.setNote(requestDto.getNote());
        transaction.setCategory(category);

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return mapToResponse(updatedTransaction);
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public void deleteTransaction(Long userId, Long transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + transactionId));

        if (!transaction.getUser().getId().equals(userId)) {
            throw new UnauthorizedActionException("You are not allowed to delete this transaction");
        }

        transactionRepository.delete(transaction);
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