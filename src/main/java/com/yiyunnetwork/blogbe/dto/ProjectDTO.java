package com.yiyunnetwork.blogbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String imageUrl;
    
    @JsonIgnore
    private MultipartFile image;
    
    private List<String> tags;
    private GithubInfo github;
    private DemoInfo demo;
    private String status;
    private List<String> features;
    private List<String> techStack;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Data
    public static class GithubInfo {
        private String url;
        private boolean disabled;
        private String disabledReason;
    }

    @Data
    public static class DemoInfo {
        private String url;
        private boolean disabled;
        private String disabledReason;
    }
} 