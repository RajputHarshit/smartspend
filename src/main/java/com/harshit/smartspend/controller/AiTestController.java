package com.harshit.smartspend.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {
    private final ChatClient chatClient;

    public AiTestController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ping")
    public String ping(){
        return chatClient
                .prompt()
                .options(GoogleGenAiChatOptions.builder()
                .model("gemini-2.5-flash")
                .build())
                .user("Say hello in one short sentence")
                .call().content();
    }
}
