package com.yiyunnetwork.blogbe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagDTO {
    private Long id;
    
    @NotBlank(message = "标签名称不能为空")
    private String name;
    
    private String description;
    private Integer articleCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
} 