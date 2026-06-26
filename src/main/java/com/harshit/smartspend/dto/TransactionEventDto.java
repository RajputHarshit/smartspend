package com.harshit.smartspend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventDto {

    private Long userId;
    private Long categoryId;
    private BigDecimal amount;
    private String monthYear;  // e.g. "2026-06" — captured at produce time, not computed later
}
