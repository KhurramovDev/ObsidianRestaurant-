package com.restaurant.backend.controller;

import com.restaurant.backend.entity.MenuItem;
import com.restaurant.backend.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public/ai")
@RequiredArgsConstructor
public class AIRecommendationController {

    private final MenuItemRepository menuItemRepository;

    @GetMapping("/recommend")
    public List<MenuItem> getRecommendations(@RequestParam List<Long> cartItemIds) {
        // Haqiqiy AI o'rniga biz "Mantiqiy Tavsiya" algoritmidan foydalanamiz
        // Masalan: Agar go'shtli taom bo'lsa -> Ichimlik yoki Salat tavsiya qiladi
        
        List<MenuItem> allItems = menuItemRepository.findAll();
        List<MenuItem> recommendations = new ArrayList<>();

        boolean hasMainDish = false;
        for (Long id : cartItemIds) {
            // Savatchadagi taomlarni tekshirish (aslida bu yerda ML model bo'lishi mumkin)
            hasMainDish = true; 
        }

        if (hasMainDish) {
            // Ichimliklar va desertlarni qidirish
            recommendations = allItems.stream()
                .filter(item -> item.getCategory() != null && 
                       (item.getCategory().getTitle().toLowerCase().contains("ichimlik") || 
                        item.getCategory().getTitle().toLowerCase().contains("desert") ||
                        item.getCategory().getTitle().toLowerCase().contains("salat")))
                .limit(3)
                .collect(Collectors.toList());
        }

        return recommendations;
    }
}
