package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.ViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ViewLogRepository extends JpaRepository<ViewLog, Long> {
    Long countByCreateTimeBetween(LocalDateTime start, LocalDateTime end);
    
    Long countByBlogId(Long blogId);
    
    @Query("SELECT v.blogId, COUNT(v) as count FROM ViewLog v GROUP BY v.blogId ORDER BY count DESC")
    List<Object[]> findBlogViewCounts();
    
    @Query("SELECT v.blogId, COUNT(v) as count FROM ViewLog v " +
           "WHERE v.createTime BETWEEN ?1 AND ?2 GROUP BY v.blogId ORDER BY count DESC")
    List<Object[]> findBlogViewCountsBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByBlogIdAndIpAndUserAgentAndCreateTimeGreaterThan(
        Long blogId,
        String ip,
        String userAgent,
        LocalDateTime time
    );
} 