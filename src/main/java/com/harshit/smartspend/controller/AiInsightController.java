package com.harshit.smartspend.controller;

import com.harshit.smartspend.dto.MonthlyInsightResponse;
import com.harshit.smartspend.service.AiInsightService;
import com.harshit.smartspend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
        }// adjust to match how your other controllers pull userId
        return aiInsightService.generateMonthlyInsight(userId, monthYear);
    }
}