package com.yiyunnetwork.blogbe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {
    private Long id;
    
    @NotBlank(message = "分类名称不能为空")
    private String name;
    
    private String description;
    private Integer articleCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 