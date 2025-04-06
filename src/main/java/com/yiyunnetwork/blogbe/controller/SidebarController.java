package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.SidebarDTO;
import com.yiyunnetwork.blogbe.entity.SidebarConfig;
import com.yiyunnetwork.blogbe.service.SidebarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SidebarController {

    private final SidebarService sidebarService;

    @GetMapping("/api/home/sidebar")
    public Result<SidebarDTO> getSidebarData() {
        return Result.success(sidebarService.getSidebarData());
    }

    @GetMapping("/api/home/sidebar/config")
    public Result<SidebarConfig> getSidebarConfig() {
        return Result.success(sidebarService.getSidebarConfig());
    }

    @PutMapping("/api/home/sidebar/config")
    public Result<SidebarConfig> updateSidebarConfig(@RequestBody @Valid SidebarConfig config) {
        return Result.success(sidebarService.updateSidebarConfig(config));
    }
} 