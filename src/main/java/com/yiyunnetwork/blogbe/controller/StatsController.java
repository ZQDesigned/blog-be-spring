package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.DashboardStatsDTO;
import com.yiyunnetwork.blogbe.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/dashboard")
    public Result<DashboardStatsDTO> getDashboardStats() {
        return Result.success(statsService.getDashboardStats());
    }
} 