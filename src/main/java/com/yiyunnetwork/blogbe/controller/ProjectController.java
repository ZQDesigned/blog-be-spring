package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.ProjectDTO;
import com.yiyunnetwork.blogbe.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/list")
    public Result<List<ProjectDTO>> getProjectList() {
        return Result.success(projectService.getProjectList());
    }

    @GetMapping("/{id}")
    public Result<ProjectDTO> getProjectById(@PathVariable Long id) {
        return Result.success(projectService.getProjectById(id));
    }

    @PostMapping
    public Result<ProjectDTO> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        return Result.success(projectService.createProject(projectDTO));
    }

    @PutMapping("/{id}")
    public Result<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO projectDTO) {
        return Result.success(projectService.updateProject(id, projectDTO));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return Result.success();
    }
} 