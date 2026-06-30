package com.harshit.smartspend.dto;

import java.util.List;

public record MonthlyInsightResponse(String summary,
                                     String topSpendingCategory,
                                     List<String> overBudgetCategories,
                                     String recommendation)
{ }
