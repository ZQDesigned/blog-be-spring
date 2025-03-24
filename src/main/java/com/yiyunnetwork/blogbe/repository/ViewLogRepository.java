package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.ViewLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ViewLogRepository extends JpaRepository<ViewLog, Long> {
    Long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
} 