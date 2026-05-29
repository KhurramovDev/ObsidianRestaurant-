package com.restaurant.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, PREPARING, SHIPPING, COMPLETED, CANCELLED

    @Column(name = "order_type", nullable = false)
    private String orderType = "DINING"; // DINING, DELIVERY

    @Column(name = "payment_method")
    private String paymentMethod = "CASH"; // CASH, CARD, ONLINE

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "final_amount")
    private BigDecimal finalAmount;

    @Column(name = "delivery_address", length = 500)
    private String deliveryAddress;

    @Column(name = "delivery_time")
    private String deliveryTime; // e.g., "ASAP" or "2023-10-27 18:00"

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
}
