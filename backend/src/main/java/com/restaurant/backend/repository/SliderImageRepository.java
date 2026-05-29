package com.restaurant.backend.repository;

import com.restaurant.backend.entity.SliderImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SliderImageRepository extends JpaRepository<SliderImage, Long> {
    List<SliderImage> findAllByOrderBySortOrderAsc();
}
