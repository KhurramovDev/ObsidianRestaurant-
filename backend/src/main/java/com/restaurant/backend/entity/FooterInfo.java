package com.restaurant.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "footer_info")
public class FooterInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantName = "Obsidian";

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    private String phone1;
    private String phone2;

    private String instagramUrl;
    private String facebookUrl;
    private String telegramUrl;

    private String mapEmbedUrl;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
