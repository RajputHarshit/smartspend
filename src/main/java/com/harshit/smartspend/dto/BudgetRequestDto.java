package com.harshit.smartspend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetRequestDto {
    @NotNull(message = "Limit amount is required")
    @Positive(message = "Limit amount must be positive")
    private BigDecimal limitAmount;

    @NotBlank(message = "Month year is required")
    private String monthYear;

//    @NotNull(message = "User ID is required")
//    private Long userId;

    @NotNull(message = "Category ID is required")
    private Long categoryId;
}
