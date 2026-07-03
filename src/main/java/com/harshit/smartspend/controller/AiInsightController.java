package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.MonthlyInsightResponse;
import com.harshit.smartspend.service.AiInsightService;
import com.harshit.smartspend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/insights")
@RequiredArgsConstructor
public class AiInsightController {

    private final AiInsightService aiInsightService;
    private final UserService userService;

    @Operation(
            summary = "Get AI-generated monthly spending insight",
            description = "Uses Gemini AI to analyze a user's transactions for the given month and returns " +
                    "a summary, top spending category, over-budget categories, and personalized recommendations. " +
                    "If monthYear is not provided, defaults to the current month."
    )

    @GetMapping("/monthly")
    public MonthlyInsightResponse getMonthlyInsight(@AuthenticationPrincipal UserDetails userDetails,
                                                    @Parameter(name = "monthYear", description = "Target month in yyyy-MM format (e.g. 2026-07). Defaults to current month if omitted.")

                                                    @RequestParam (required = false) String monthYear) {
        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        if(monthYear==null){
            monthYear= YearMonth.now().toString();
        }
        return aiInsightService.generateMonthlyInsight(userId, monthYear);
    }
    @Operation(
            summary = "Ask a natural-language question about your budget",
            description = "Uses Gemini AI with tool-calling (BudgetStatusTool) to answer natural-language " +
                    "questions about spending and budget status, e.g. 'How much have I spent on food this month?' " +
                    "or 'Am I over budget on entertainment?'"
    )
    @PostMapping(value = "/ask", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> askAboutBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody String question) {
        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        String answer = aiInsightService.askAboutBudget(userId, question);
        return ResponseEntity.ok(answer);
    }
}