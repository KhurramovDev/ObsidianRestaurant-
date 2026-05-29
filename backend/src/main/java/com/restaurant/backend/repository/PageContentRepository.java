package com.restaurant.backend.repository;

import com.restaurant.backend.entity.PageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageContentRepository extends JpaRepository<PageContent, Long> {
    PageContent findBySectionName(String sectionName);
}
