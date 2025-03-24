package com.yiyunnetwork.blogbe.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "github_disabled")
    private Boolean githubDisabled = false;

    @Column(name = "github_disabled_reason")
    private String githubDisabledReason;

    @Column(name = "demo_url")
    private String demoUrl;

    @Column(name = "demo_disabled")
    private Boolean demoDisabled = false;

    @Column(name = "demo_disabled_reason")
    private String demoDisabledReason;

    @Column(length = 20)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String features;

    @Column(name = "tech_stack", columnDefinition = "TEXT")
    private String techStack;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 