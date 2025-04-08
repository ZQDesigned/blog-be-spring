package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.AboutMeSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AboutMeSectionRepository extends JpaRepository<AboutMeSection, Long> {
    List<AboutMeSection> findByEnabledTrueOrderBySortOrderAsc();
} 