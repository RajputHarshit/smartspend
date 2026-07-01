package com.harshit.smartspend.service;

import com.harshit.smartspend.dto.CategorySpendingInsight;
import com.harshit.smartspend.dto.MonthlyInsightResponse;
import com.harshit.smartspend.entity.Budget;
import com.harshit.smartspend.repository.BudgetRepository;
import com.harshit.smartspend.repository.TransactionRepository;
import com.harshit.smartspend.repository.TransactionRepository.CategoryExpenseSummary;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiInsightService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final ChatClient chatClient;

    public AiInsightService(TransactionRepository transactionRepository,
                            BudgetRepository budgetRepository,
                            ChatClient.Builder chatClientBuilder) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.chatClient = chatClientBuilder.build();
    }

    public List<CategorySpendingInsight> getMonthlySpendingSummary(Long userId, String monthYear) {
        YearMonth ym = YearMonth.parse(monthYear);
        LocalDateTime start = ym.atDay(1).atStartOfDay();
        LocalDateTime end = ym.plusMonths(1).atDay(1).atStartOfDay();

        List<CategoryExpenseSummary> summaries =
                transactionRepository.sumExpensesGroupedByCategory(userId, start, end);

        List<CategorySpendingInsight> result = new ArrayList<>();

        for (CategoryExpenseSummary row : summaries) {
            BigDecimal budgetLimit = budgetRepository
                    .findByUserIdAndCategoryIdAndMonthYear(userId, row.getCategory().getId(), monthYear)
                    .map(Budget::getLimitAmount)
                    .orElse(null);

            result.add(new CategorySpendingInsight(row.getCategory().getName(), row.getTotalSpent(), budgetLimit));
        }

        return result;
    }

    private String buildPromptText(List<CategorySpendingInsight> insights) {
        StringBuilder sb = new StringBuilder();
        for (CategorySpendingInsight insight : insights) {
            sb.append(insight.categoryName())
                    .append(": spent ")
                    .append(insight.spent());

            if (insight.budgetLimit() != null) {
                sb.append(", budget ").append(insight.budgetLimit());
            } else {
                sb.append(", no budget set");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public MonthlyInsightResponse generateMonthlyInsight(Long userId, String monthYear) {
        List<CategorySpendingInsight> insights = getMonthlySpendingSummary(userId, monthYear);
        String promptText = buildPromptText(insights);

        return chatClient.prompt()
                .system("""
    You are a financial assistant. Analyze the user's monthly spending data
    and respond with a structured JSON summary containing exactly these fields:
    
    - summary: A short, encouraging 2-3 sentence overview of the user's spending this month.
    - topSpendingCategory: The single category name where the user spent the most money.
    - overBudgetCategories: A list of category names where spending exceeded the budget limit. 
      If none are over budget, return an empty list.
    - recommendation: One practical, friendly suggestion to help the user improve their spending next month.
    """).options(GoogleGenAiChatOptions.builder()
                        .model("gemini-2.5-flash")
                        .build())
                .user(promptText)
                .call()
                .entity(MonthlyInsightResponse.class);
    }
    public String askAboutBudget(Long userId, String question) {
        BudgetStatusTool tool = new BudgetStatusTool(userId, this);

        return chatClient.prompt()
                .system("""
                You are a financial assistant. Answer the user's question about their
                budget. If you need live spending or budget data for a specific
                category, use the getCategoryBudgetStatus tool. Always specify the
                month in yyyy-MM format when calling it.
                """)
                .options(GoogleGenAiChatOptions.builder()
                .model("gemini-2.5-flash")
                .build())
                .user(question)
                .tools(tool)
                .call()
                .content();
    }
}