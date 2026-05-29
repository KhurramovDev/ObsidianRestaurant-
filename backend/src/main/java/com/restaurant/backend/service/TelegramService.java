package com.restaurant.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.HashMap;
import java.util.Map;

@Service
public class TelegramService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${telegram.bot.dining.token}")
    private String diningBotToken;

    @Value("${telegram.bot.delivery.token}")
    private String deliveryBotToken;

    @Value("${telegram.admin.chatId}")
    private String adminChatId;

    public String sendOrderNotification(String message, String orderType) {
        return sendToChat(adminChatId, message, orderType);
    }

    public String sendToChat(String chatId, String message, String orderType) {
        if (chatId == null || chatId.trim().isEmpty()) {
            return "Xato: Chat ID topilmadi!";
        }
        
        try {
            String token = "DELIVERY".equals(orderType) ? deliveryBotToken : diningBotToken;
            if (token == null || token.trim().isEmpty()) {
                return "Xato: Bot tokeni topilmadi! application.properties faylini tekshiring.";
            }

            String url = "https://api.telegram.org/bot" + token.trim() + "/sendMessage";

            Map<String, Object> body = new HashMap<>();
            body.put("chat_id", chatId.trim());
            body.put("text", message);
            body.put("parse_mode", "HTML");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(url, entity, String.class);
            return "Muvaffaqiyatli yuborildi: " + chatId;
        } catch (Exception e) {
            return "Telegram API Xatosi: " + e.getMessage();
        }
    }
}
