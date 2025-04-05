package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.HomeSection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeSectionRepository extends JpaRepository<HomeSection, Long> {
    List<HomeSection> findByEnabledTrueOrderBySortOrderAsc();
} 