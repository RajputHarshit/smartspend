package com.harshit.smartspend.dto;

import java.math.BigDecimal;

public record CategorySpendingInsight(String categoryName, BigDecimal spent, BigDecimal budgetLimit) {
}
