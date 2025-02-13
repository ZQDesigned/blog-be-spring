package com.yiyunnetwork.blogbe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "t_blog_content")
public class BlogContent {
    @Id
    private Long id;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private BlogMeta blogMeta;
} 