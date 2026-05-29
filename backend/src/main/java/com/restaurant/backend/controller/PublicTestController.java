package com.restaurant.backend.controller;

import com.restaurant.backend.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicTestController {

    private final TelegramService telegramService;

    @GetMapping("/test-bot")
    public String testBot() {
        String res1 = telegramService.sendOrderNotification("✅ Public Test: Dining Bot is working!", "DINING");
        String res2 = telegramService.sendOrderNotification("🚀 Public Test: Delivery Bot is working!", "DELIVERY");
        
        return "Natija:\n" + 
               "1. Dining Bot: " + res1 + "\n" +
               "2. Delivery Bot: " + res2 + "\n\n" +
               "Agar 'Muvaffaqiyatli' deb yozilgan bo'lsa-yu, xabar kelmasa:\n" +
               "- Telegramda botga kirib /start bosing.\n" +
               "- Chat ID (6028004749) sizniki ekanligiga ishonch hosil qiling.";
    }
}
