package com.harshit.smartspend.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponseDto {
    private Long id;
    private BigDecimal limitAmount;
    private String monthYear;
    private String userName;
    private String categoryName;
    private LocalDateTime createdAt;
}
