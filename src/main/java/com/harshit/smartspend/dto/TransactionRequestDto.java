package com.harshit.smartspend.dto;

import com.harshit.smartspend.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {
    private BigDecimal amount;
    private TransactionType type;
    private String note;
    private Long userId;
    private Long categoryId;
}
