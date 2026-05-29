package com.restaurant.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "page_content")
public class PageContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_name", nullable = false)
    private String sectionName; // MAIN_SLIDER, ABOUT, CONTACT, FOOTER

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
}
