package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getProjectList();
    ProjectDTO getProjectById(Long id);
    ProjectDTO createProject(ProjectDTO projectDTO);
    ProjectDTO updateProject(Long id, ProjectDTO projectDTO);
    void deleteProject(Long id);
} 