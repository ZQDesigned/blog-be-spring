package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByIsDeletedFalseOrderByCreateTimeDesc();
} 