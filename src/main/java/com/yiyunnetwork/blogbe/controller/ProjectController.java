package com.yiyunnetwork.blogbe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.config.JsonPropertyEditor;
import com.yiyunnetwork.blogbe.dto.ProjectDTO;
import com.yiyunnetwork.blogbe.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ObjectMapper objectMapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
            ProjectDTO.GithubInfo.class,
            new JsonPropertyEditor<>(objectMapper, ProjectDTO.GithubInfo.class)
        );
        binder.registerCustomEditor(
            ProjectDTO.DemoInfo.class,
            new JsonPropertyEditor<>(objectMapper, ProjectDTO.DemoInfo.class)
        );
        binder.registerCustomEditor(
            List.class,
            "features",
            new JsonPropertyEditor<>(objectMapper, new TypeReference<List<String>>() {})
        );
        binder.registerCustomEditor(
            List.class,
            "techStack",
            new JsonPropertyEditor<>(objectMapper, new TypeReference<List<String>>() {})
        );
    }

    @GetMapping("/list")
    public Result<List<ProjectDTO>> getProjectList() {
        return Result.success(projectService.getProjectList());
    }

    @GetMapping("/{id}")
    public Result<ProjectDTO> getProjectById(@PathVariable Long id) {
        return Result.success(projectService.getProjectById(id));
    }

    @PostMapping
    public Result<ProjectDTO> createProject(@ModelAttribute @Valid ProjectDTO projectDTO) {
        return Result.success(projectService.createProject(projectDTO));
    }

    @PutMapping("/{id}")
    public Result<ProjectDTO> updateProject(@PathVariable Long id, @ModelAttribute @Valid ProjectDTO projectDTO) {
        return Result.success(projectService.updateProject(id, projectDTO));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return Result.success();
    }
} 