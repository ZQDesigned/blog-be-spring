package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.AboutMeDTO;
import com.yiyunnetwork.blogbe.entity.AboutMeSection;
import com.yiyunnetwork.blogbe.service.AboutMeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AboutMeController {

    private final AboutMeService aboutMeService;

    @GetMapping("/api/about/me")
    public Result<AboutMeDTO> getAboutMe() {
        return Result.success(aboutMeService.getAboutMe());
    }

    @GetMapping("/api/about/sections")
    public Result<List<AboutMeSection>> getAllSections() {
        return Result.success(aboutMeService.getAllSections());
    }

    @GetMapping("/api/about/sections/{id}")
    public Result<AboutMeSection> getSectionById(@PathVariable Long id) {
        return Result.success(aboutMeService.getSectionById(id));
    }

    @PostMapping("/api/about/sections")
    public Result<AboutMeSection> createSection(@RequestBody @Valid AboutMeSection section) {
        return Result.success(aboutMeService.createSection(section));
    }

    @PutMapping("/api/about/sections/{id}")
    public Result<AboutMeSection> updateSection(@PathVariable Long id, @RequestBody AboutMeSection section) {
        return Result.success(aboutMeService.updateSection(id, section));
    }

    @DeleteMapping("/api/about/sections/{id}")
    public Result<?> deleteSection(@PathVariable Long id) {
        aboutMeService.deleteSection(id);
        return Result.success();
    }

    @PutMapping("/api/about/sections/order")
    public Result<?> updateSectionOrder(@RequestBody List<Long> sectionIds) {
        aboutMeService.updateSectionOrder(sectionIds);
        return Result.success();
    }
} 