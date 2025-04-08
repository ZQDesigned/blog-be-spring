package com.yiyunnetwork.blogbe.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_about_me_section")
public class AboutMeSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    // 用于自定义区块的唯一标识
    private String sectionId;

    // 存储JSON格式的内容数据
    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer sortOrder;

    private Boolean enabled = true;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 