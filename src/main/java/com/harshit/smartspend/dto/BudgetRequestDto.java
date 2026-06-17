package com.harshit.smartspend.dto;

import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequestDto {
    private BigDecimal limitAmount;
    private String monthYear;      // format: "2026-06"
    private Long userId;
    private Long categoryId;
}
