package com.yiyunnetwork.blogbe.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String token;
    private String tokenType;
    private long expiresIn;

    public TokenDTO(String token, String tokenType, long expiresIn) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }
} 