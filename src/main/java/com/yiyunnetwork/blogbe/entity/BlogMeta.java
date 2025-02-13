package com.yiyunnetwork.blogbe.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "t_blog")
public class BlogMeta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String summary;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BlogTag> blogTags = new ArrayList<>();

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @OneToOne(mappedBy = "blogMeta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BlogContent content;

    public void addTag(Tag tag) {
        BlogTag blogTag = new BlogTag();
        blogTag.setBlog(this);
        blogTag.setTag(tag);
        blogTags.add(blogTag);
    }

    public void removeTags() {
        blogTags.clear();
    }
} 