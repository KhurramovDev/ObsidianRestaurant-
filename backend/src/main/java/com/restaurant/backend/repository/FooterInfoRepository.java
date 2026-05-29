package com.restaurant.backend.repository;

import com.restaurant.backend.entity.FooterInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FooterInfoRepository extends JpaRepository<FooterInfo, Long> {
    Optional<FooterInfo> findFirstByIsActiveTrue();
}
