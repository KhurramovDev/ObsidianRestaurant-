package com.restaurant.backend.controller;

import com.restaurant.backend.entity.*;
import com.restaurant.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicContentController {

    private final PageContentRepository pageContentRepository;
    private final NewsRepository newsRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final SliderImageRepository sliderImageRepository;
    private final FooterInfoRepository footerInfoRepository;

    @GetMapping("/content/{sectionName}")
    public ResponseEntity<PageContent> getContent(@PathVariable String sectionName) {
        return ResponseEntity.ok(pageContentRepository.findBySectionName(sectionName));
    }

    @GetMapping("/content")
    public ResponseEntity<List<PageContent>> getAllContent() {
        return ResponseEntity.ok(pageContentRepository.findAll());
    }

    @GetMapping("/news")
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsRepository.findAll());
    }

    @GetMapping("/menu/categories")
    public ResponseEntity<List<MenuCategory>> getMenuCategories() {
        return ResponseEntity.ok(menuCategoryRepository.findAllByOrderByPageNumberAsc());
    }

    @GetMapping("/menu/items/{categoryId}")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuItemRepository.findByCategoryId(categoryId));
    }

    @GetMapping("/slider")
    public ResponseEntity<List<SliderImage>> getSliderImages() {
        return ResponseEntity.ok(sliderImageRepository.findAllByOrderBySortOrderAsc());
    }

    @GetMapping("/footer")
    public ResponseEntity<FooterInfo> getFooter() {
        Optional<FooterInfo> footer = footerInfoRepository.findFirstByIsActiveTrue();
        return footer.map(ResponseEntity::ok).orElseGet(() -> {
            FooterInfo defaultFooter = new FooterInfo();
            defaultFooter.setRestaurantName("Obsidian");
            defaultFooter.setAddress("Toshkent shahar, Yunusobod tumani");
            defaultFooter.setPhone1("+998 90 123 45 67");
            return ResponseEntity.ok(defaultFooter);
        });
    }
}
