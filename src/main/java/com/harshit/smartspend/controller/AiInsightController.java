package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.MonthlyInsightResponse;
import com.harshit.smartspend.service.AiInsightService;
import com.harshit.smartspend.service.UserService;
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

    @GetMapping("/monthly")
    public MonthlyInsightResponse getMonthlyInsight(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestParam (required = false) String monthYear) {
        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        if(monthYear==null){
            monthYear= YearMonth.now().toString();
        }
        return aiInsightService.generateMonthlyInsight(userId, monthYear);
    }
    @PostMapping(value = "/ask", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> askAboutBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody String question) {
        Long userId=userService.getUserIdByEmail(userDetails.getUsername());
        String answer = aiInsightService.askAboutBudget(userId, question);
        return ResponseEntity.ok(answer);
    }
}