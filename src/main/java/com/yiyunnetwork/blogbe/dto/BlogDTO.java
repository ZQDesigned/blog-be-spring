package com.yiyunnetwork.blogbe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogDTO {
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    private String title;
    
    private String content;
    private String summary;
    
    @NotNull(message = "分类不能为空")
    private Long categoryId;
    private String categoryName;
    
    private List<Long> tagIds;
    private List<String> tagNames;
    
    private Long viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 