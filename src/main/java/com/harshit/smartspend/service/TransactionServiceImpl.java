package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.TransactionEventDto;
import com.harshit.smartspend.dto.TransactionRequestDto;
import com.harshit.smartspend.dto.TransactionResponseDto;
import com.harshit.smartspend.entity.Category;
import com.harshit.smartspend.entity.Transaction;
import com.harshit.smartspend.entity.TransactionType;
import com.harshit.smartspend.entity.User;
import com.harshit.smartspend.repository.CategoryRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import com.harshit.smartspend.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService{
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
       Transaction savedTransaction =transactionRepository.save(transaction);
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
