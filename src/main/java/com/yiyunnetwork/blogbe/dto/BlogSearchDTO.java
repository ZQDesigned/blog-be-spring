package com.yiyunnetwork.blogbe.dto;

import java.time.LocalDateTime;

public record BlogSearchDTO(Long id, String title, String summary, String categoryName,
                            LocalDateTime updateTime) {
}
