package com.yiyunnetwork.blogbe.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_sidebar_config")
public class SidebarConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String avatar;

    @Column(length = 50)
    private String name;

    @Column(length = 200)
    private String bio;

    private Boolean online;

    @Column(name = "status_text", length = 100)
    private String statusText;

    @Column(columnDefinition = "TEXT")
    private String announcements;

    @Column(length = 100)
    private String email;

    @Column(name = "show_weather")
    private Boolean showWeather;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 