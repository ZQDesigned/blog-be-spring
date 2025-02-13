package com.yiyunnetwork.blogbe.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminInfoDTO {
    private Long id;
    private String username;
    private LocalDateTime lastLoginTime;
    private String lastLoginIp;
} 