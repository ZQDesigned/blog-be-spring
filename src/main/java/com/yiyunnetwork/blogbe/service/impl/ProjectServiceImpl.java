package com.yiyunnetwork.blogbe.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.dto.ProjectDTO;
import com.yiyunnetwork.blogbe.entity.Project;
import com.yiyunnetwork.blogbe.repository.ProjectRepository;
import com.yiyunnetwork.blogbe.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
// TODO: 项目缓存功能暂时禁用，需要重新设计缓存策略
// import org.springframework.cache.annotation.CacheEvict;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    @Override
    // TODO: 项目列表缓存功能暂时禁用
    // @Cacheable(value = "projectList")
    public List<ProjectDTO> getProjectList() {
        return projectRepository.findByIsDeletedFalseOrderByCreateTimeDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    // TODO: 项目详情缓存功能暂时禁用
    // @Cacheable(value = "project", key = "#id")
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        return convertToDTO(project);
    }

    @Override
    @Transactional
    // TODO: 项目缓存清除功能暂时禁用
    // @CacheEvict(value = {"project", "projectList"}, allEntries = true)
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        Project project = new Project();
        updateProjectFromDTO(project, projectDTO);
        project = projectRepository.save(project);
        return convertToDTO(project);
    }

    @Override
    @Transactional
    // TODO: 项目缓存清除功能暂时禁用
    // @CacheEvict(value = {"project", "projectList"}, allEntries = true)
    public ProjectDTO updateProject(Long id, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        updateProjectFromDTO(project, projectDTO);
        project = projectRepository.save(project);
        return convertToDTO(project);
    }

    @Override
    @Transactional
    // TODO: 项目缓存清除功能暂时禁用
    // @CacheEvict(value = {"project", "projectList"}, allEntries = true)
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        project.setIsDeleted(true);
        projectRepository.save(project);
    }

    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setContent(project.getContent());
        dto.setStatus(project.getStatus());
        dto.setCreateTime(project.getCreateTime());
        dto.setUpdateTime(project.getUpdateTime());

        try {
            dto.setTags(objectMapper.readValue(project.getTags(), new TypeReference<List<String>>() {}));
            dto.setFeatures(objectMapper.readValue(project.getFeatures(), new TypeReference<List<String>>() {}));
            dto.setTechStack(objectMapper.readValue(project.getTechStack(), new TypeReference<List<String>>() {}));
        } catch (JsonProcessingException e) {
            dto.setTags(new ArrayList<>());
            dto.setFeatures(new ArrayList<>());
            dto.setTechStack(new ArrayList<>());
        }

        ProjectDTO.GithubInfo github = new ProjectDTO.GithubInfo();
        github.setUrl(project.getGithubUrl());
        github.setDisabled(project.getGithubDisabled());
        github.setDisabledReason(project.getGithubDisabledReason());
        dto.setGithub(github);

        ProjectDTO.DemoInfo demo = new ProjectDTO.DemoInfo();
        demo.setUrl(project.getDemoUrl());
        demo.setDisabled(project.getDemoDisabled());
        demo.setDisabledReason(project.getDemoDisabledReason());
        dto.setDemo(demo);

        return dto;
    }

    private void updateProjectFromDTO(Project project, ProjectDTO dto) {
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setContent(dto.getContent());
        project.setStatus(dto.getStatus());

        try {
            project.setTags(objectMapper.writeValueAsString(dto.getTags()));
            project.setFeatures(objectMapper.writeValueAsString(dto.getFeatures()));
            project.setTechStack(objectMapper.writeValueAsString(dto.getTechStack()));
        } catch (JsonProcessingException e) {
            project.setTags("[]");
            project.setFeatures("[]");
            project.setTechStack("[]");
        }

        if (dto.getGithub() != null) {
            project.setGithubUrl(dto.getGithub().getUrl());
            project.setGithubDisabled(dto.getGithub().isDisabled());
            project.setGithubDisabledReason(dto.getGithub().getDisabledReason());
        }

        if (dto.getDemo() != null) {
            project.setDemoUrl(dto.getDemo().getUrl());
            project.setDemoDisabled(dto.getDemo().isDisabled());
            project.setDemoDisabledReason(dto.getDemo().getDisabledReason());
        }
    }
}