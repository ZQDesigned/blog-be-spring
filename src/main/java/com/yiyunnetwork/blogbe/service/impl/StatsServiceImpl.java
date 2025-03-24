package com.yiyunnetwork.blogbe.service.impl;

import com.yiyunnetwork.blogbe.dto.DashboardStatsDTO;
import com.yiyunnetwork.blogbe.repository.*;
import com.yiyunnetwork.blogbe.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final BlogMetaRepository blogMetaRepository;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ViewLogRepository viewLogRepository;

    @Override
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        // 获取基础统计数据
        stats.setTotalArticles(blogMetaRepository.countByIsDeletedFalse());
        stats.setTotalProjects(projectRepository.countByIsDeletedFalse());
        stats.setTotalCategories(categoryRepository.count());
        stats.setTotalTags(tagRepository.count());

        // 获取最近7天的访问趋势
        List<DashboardStatsDTO.VisitTrendDTO> visitTrend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            Long count = viewLogRepository.countByCreateTimeBetween(
                date.atStartOfDay(),
                date.plusDays(1).atStartOfDay()
            );
            DashboardStatsDTO.VisitTrendDTO trend = new DashboardStatsDTO.VisitTrendDTO();
            trend.setDate(date.format(formatter));
            trend.setValue(count);
            visitTrend.add(trend);
        }
        stats.setVisitTrend(visitTrend);

        // 获取分类统计
        List<DashboardStatsDTO.CategoryStatsDTO> categoryStats = categoryRepository.findAllByOrderByArticleCountDesc()
            .stream()
            .limit(5)
            .map(category -> {
                DashboardStatsDTO.CategoryStatsDTO stat = new DashboardStatsDTO.CategoryStatsDTO();
                stat.setCategory(category.getName());
                stat.setValue((long) category.getArticleCount());
                return stat;
            })
            .collect(Collectors.toList());
        stats.setCategoryStats(categoryStats);

        // 获取项目状态统计
        List<Object[]> projectStatusCounts = projectRepository.countByStatus();
        List<DashboardStatsDTO.ProjectStatsDTO> projectStats = projectStatusCounts.stream()
            .map(result -> {
                DashboardStatsDTO.ProjectStatsDTO stat = new DashboardStatsDTO.ProjectStatsDTO();
                stat.setType((String) result[0]);
                stat.setValue((Long) result[1]);
                return stat;
            })
            .collect(Collectors.toList());
        stats.setProjectStats(projectStats);

        return stats;
    }
} 