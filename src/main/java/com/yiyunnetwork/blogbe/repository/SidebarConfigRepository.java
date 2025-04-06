package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.SidebarConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidebarConfigRepository extends JpaRepository<SidebarConfig, Long> {
    SidebarConfig findFirstByOrderByIdAsc();
} 