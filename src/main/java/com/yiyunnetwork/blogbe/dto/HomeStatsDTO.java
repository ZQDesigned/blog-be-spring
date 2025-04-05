package com.yiyunnetwork.blogbe.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HomeStatsDTO {
    private Long totalArticles;
    private Long totalProjects;
    private Long totalViews;
    private LocalDateTime lastUpdateTime;
} 