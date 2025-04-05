package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.HomeContentDTO;
import com.yiyunnetwork.blogbe.dto.HomeStatsDTO;
import com.yiyunnetwork.blogbe.entity.HomeSection;
import com.yiyunnetwork.blogbe.entity.SiteMeta;
import com.yiyunnetwork.blogbe.service.HomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    // 前端接口
    @GetMapping("/api/home/content")
    public Result<HomeContentDTO> getHomeContent() {
        return Result.success(homeService.getHomeContent());
    }

    @GetMapping("/api/home/stats")
    public Result<HomeStatsDTO> getHomeStats() {
        return Result.success(homeService.getHomeStats());
    }

    // 管理端接口
    @GetMapping("/api/home/sections")
    public Result<List<HomeSection>> getAllSections() {
        return Result.success(homeService.getAllSections());
    }

    @GetMapping("/api/home/sections/{id}")
    public Result<HomeSection> getSectionById(@PathVariable Long id) {
        return Result.success(homeService.getSectionById(id));
    }

    @PostMapping("/api/home/sections")
    public Result<HomeSection> createSection(@RequestBody @Valid HomeSection section) {
        return Result.success(homeService.createSection(section));
    }

    @PutMapping("/api/home/sections/{id}")
    public Result<HomeSection> updateSection(@PathVariable Long id, @RequestBody @Valid HomeSection section) {
        return Result.success(homeService.updateSection(id, section));
    }

    @DeleteMapping("/api/home/sections/{id}")
    public Result<?> deleteSection(@PathVariable Long id) {
        homeService.deleteSection(id);
        return Result.success();
    }

    @PutMapping("/api/home/sections/order")
    public Result<?> updateSectionOrder(@RequestBody List<Long> sectionIds) {
        homeService.updateSectionOrder(sectionIds);
        return Result.success();
    }

    // 网站元数据管理接口
    @GetMapping("/api/home/meta")
    public Result<SiteMeta> getSiteMeta() {
        return Result.success(homeService.getSiteMeta());
    }

    @PutMapping("/api/home/meta")
    public Result<SiteMeta> updateSiteMeta(@RequestBody @Valid SiteMeta siteMeta) {
        return Result.success(homeService.updateSiteMeta(siteMeta));
    }
} 