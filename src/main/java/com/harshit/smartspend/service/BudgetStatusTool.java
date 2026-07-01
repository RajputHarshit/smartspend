package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.CategorySpendingInsight;
import org.springframework.ai.tool.annotation.Tool;

import java.math.BigDecimal;
import java.util.List;

public class BudgetStatusTool {

    private final Long userId;
    private final AiInsightService aiInsightService;

    public BudgetStatusTool(Long userId, AiInsightService aiInsightService) {
        this.userId = userId;
        this.aiInsightService = aiInsightService;
    }

    @Tool(description = "Get spending and budget limit status for a specific category and month")
    public CategorySpendingInsight getCategoryBudgetStatus(String categoryName, String monthYear) {
        List<CategorySpendingInsight> insights =
                aiInsightService.getMonthlySpendingSummary(userId, monthYear);

        return insights.stream()
                .filter(insight -> insight.categoryName().equalsIgnoreCase(categoryName))
                .findFirst()
                .orElse(new CategorySpendingInsight(categoryName, BigDecimal.ZERO, null));
    }
}