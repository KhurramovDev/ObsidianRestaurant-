package com.restaurant.backend.controller;

import com.restaurant.backend.entity.*;
import com.restaurant.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminContentController {

    private final PageContentRepository pageContentRepository;
    private final NewsRepository newsRepository;
    private final MenuCategoryRepository menuCategoryRepository;
    private final MenuItemRepository menuItemRepository;
    private final SliderImageRepository sliderImageRepository;
    private final FooterInfoRepository footerInfoRepository;

    // ==================== PAGE CONTENT ====================
    @PostMapping("/content")
    public ResponseEntity<PageContent> saveContent(@RequestBody PageContent content) {
        PageContent existing = pageContentRepository.findBySectionName(content.getSectionName());
        if (existing != null) {
            existing.setTitle(content.getTitle());
            existing.setDescription(content.getDescription());
            existing.setImageUrl(content.getImageUrl());
            return ResponseEntity.ok(pageContentRepository.save(existing));
        }
        return ResponseEntity.ok(pageContentRepository.save(content));
    }

    @GetMapping("/content")
    public ResponseEntity<List<PageContent>> getAllContent() {
        return ResponseEntity.ok(pageContentRepository.findAll());
    }

    @DeleteMapping("/content/{id}")
    public ResponseEntity<?> deleteContent(@PathVariable Long id) {
        pageContentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ==================== NEWS ====================
    @GetMapping("/news")
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsRepository.findAll());
    }

    @PostMapping("/news")
    public ResponseEntity<News> createNews(@RequestBody News news) {
        return ResponseEntity.ok(newsRepository.save(news));
    }

    @PutMapping("/news/{id}")
    public ResponseEntity<News> updateNews(@PathVariable Long id, @RequestBody News news) {
        News existing = newsRepository.findById(id).orElseThrow();
        existing.setTitle(news.getTitle());
        existing.setContent(news.getContent());
        existing.setImageUrl(news.getImageUrl());
        return ResponseEntity.ok(newsRepository.save(existing));
    }

    @DeleteMapping("/news/{id}")
    public ResponseEntity<?> deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ==================== MENU CATEGORIES ====================
    @GetMapping("/menu/categories")
    public ResponseEntity<List<MenuCategory>> getAllCategories() {
        return ResponseEntity.ok(menuCategoryRepository.findAllByOrderByPageNumberAsc());
    }

    @PostMapping("/menu/categories")
    public ResponseEntity<MenuCategory> createMenuCategory(@RequestBody MenuCategory category) {
        return ResponseEntity.ok(menuCategoryRepository.save(category));
    }

    @PutMapping("/menu/categories/{id}")
    public ResponseEntity<MenuCategory> updateMenuCategory(@PathVariable Long id, @RequestBody MenuCategory category) {
        MenuCategory existing = menuCategoryRepository.findById(id).orElseThrow();
        existing.setTitle(category.getTitle());
        existing.setPageNumber(category.getPageNumber());
        return ResponseEntity.ok(menuCategoryRepository.save(existing));
    }

    @DeleteMapping("/menu/categories/{id}")
    public ResponseEntity<?> deleteMenuCategory(@PathVariable Long id) {
        menuCategoryRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ==================== MENU ITEMS ====================
    @GetMapping("/menu/items/{categoryId}")
    public ResponseEntity<List<MenuItem>> getMenuItems(@PathVariable Long categoryId) {
        return ResponseEntity.ok(menuItemRepository.findByCategoryId(categoryId));
    }

    @PostMapping("/menu/items")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem item) {
        if (item.getRating() == null) item.setRating(4);
        return ResponseEntity.ok(menuItemRepository.save(item));
    }

    @PutMapping("/menu/items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Long id, @RequestBody MenuItem item) {
        MenuItem existing = menuItemRepository.findById(id).orElseThrow();
        existing.setName(item.getName());
        existing.setPrice(item.getPrice());
        existing.setImageUrl(item.getImageUrl());
        if (item.getRating() != null) existing.setRating(item.getRating());
        return ResponseEntity.ok(menuItemRepository.save(existing));
    }

    @DeleteMapping("/menu/items/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        menuItemRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ==================== SLIDER IMAGES ====================
    @GetMapping("/slider")
    public ResponseEntity<List<SliderImage>> getAllSliderImages() {
        return ResponseEntity.ok(sliderImageRepository.findAllByOrderBySortOrderAsc());
    }

    @PostMapping("/slider")
    public ResponseEntity<SliderImage> addSliderImage(@RequestBody SliderImage image) {
        return ResponseEntity.ok(sliderImageRepository.save(image));
    }

    @PutMapping("/slider/{id}")
    public ResponseEntity<SliderImage> updateSliderImage(@PathVariable Long id, @RequestBody SliderImage image) {
        SliderImage existing = sliderImageRepository.findById(id).orElseThrow();
        existing.setImageUrl(image.getImageUrl());
        existing.setCaption(image.getCaption());
        existing.setSortOrder(image.getSortOrder());
        return ResponseEntity.ok(sliderImageRepository.save(existing));
    }

    @DeleteMapping("/slider/{id}")
    public ResponseEntity<?> deleteSliderImage(@PathVariable Long id) {
        sliderImageRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // ==================== FOOTER ====================
    @GetMapping("/footer")
    public ResponseEntity<FooterInfo> getFooter() {
        Optional<FooterInfo> footer = footerInfoRepository.findFirstByIsActiveTrue();
        return footer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(new FooterInfo()));
    }

    @PostMapping("/footer")
    public ResponseEntity<FooterInfo> saveFooter(@RequestBody FooterInfo footerInfo) {
        Optional<FooterInfo> existing = footerInfoRepository.findFirstByIsActiveTrue();
        if (existing.isPresent()) {
            FooterInfo f = existing.get();
            f.setRestaurantName(footerInfo.getRestaurantName());
            f.setDescription(footerInfo.getDescription());
            f.setAddress(footerInfo.getAddress());
            f.setPhone1(footerInfo.getPhone1());
            f.setPhone2(footerInfo.getPhone2());
            f.setInstagramUrl(footerInfo.getInstagramUrl());
            f.setFacebookUrl(footerInfo.getFacebookUrl());
            f.setTelegramUrl(footerInfo.getTelegramUrl());
            f.setMapEmbedUrl(footerInfo.getMapEmbedUrl());
            return ResponseEntity.ok(footerInfoRepository.save(f));
        }
        footerInfo.setIsActive(true);
        return ResponseEntity.ok(footerInfoRepository.save(footerInfo));
    }

    // ==================== ORDERS (Admin view) ====================
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders(com.restaurant.backend.repository.OrderRepository orderRepository) {
        return ResponseEntity.ok(orderRepository.findAll());
    }
}
