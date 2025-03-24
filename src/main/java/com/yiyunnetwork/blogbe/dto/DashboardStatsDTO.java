package com.yiyunnetwork.blogbe.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardStatsDTO {
    private Long totalArticles;
    private Long totalProjects;
    private Long totalCategories;
    private Long totalTags;
    private List<VisitTrendDTO> visitTrend;
    private List<CategoryStatsDTO> categoryStats;
    private List<ProjectStatsDTO> projectStats;

    @Data
    public static class VisitTrendDTO {
        private String date;
        private Long value;
    }

    @Data
    public static class CategoryStatsDTO {
        private String category;
        private Long value;
    }

    @Data
    public static class ProjectStatsDTO {
        private String type;
        private Long value;
    }
} 