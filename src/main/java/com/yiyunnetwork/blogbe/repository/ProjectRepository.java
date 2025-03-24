package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByIsDeletedFalseOrderByCreateTimeDesc();
    
    Long countByIsDeletedFalse();
    
    @Query("SELECT p.status as status, COUNT(p) as count FROM Project p WHERE p.isDeleted = false GROUP BY p.status")
    List<Object[]> countByStatus();
} 