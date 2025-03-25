package com.yiyunnetwork.blogbe.dto;

import lombok.Data;

@Data
public class ImageUploadDTO {
    private String url;
    private String filename;

    public ImageUploadDTO(String url, String filename) {
        this.url = url;
        this.filename = filename;
    }
} 