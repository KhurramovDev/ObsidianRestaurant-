package com.restaurant.backend.controller;

import com.restaurant.backend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController {

    private final OrderRepository orderRepository;

    @GetMapping("/sales-chart")
    public ResponseEntity<?> getSalesChartData() {
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        
        // Mock data logic based on real orders for the last 7 days
        // In a real app, you'd group by date in SQL
        Map<String, BigDecimal> dailyRevenue = orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(weekAgo) && "COMPLETED".equals(o.getStatus()))
                .collect(Collectors.groupingBy(
                        o -> o.getCreatedAt().toLocalDate().toString(),
                        TreeMap::new,
                        Collectors.mapping(o -> o.getFinalAmount(), Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        return ResponseEntity.ok(dailyRevenue);
    }
}
