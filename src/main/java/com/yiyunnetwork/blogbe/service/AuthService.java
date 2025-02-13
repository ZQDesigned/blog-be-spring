package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.AdminInfoDTO;
import com.yiyunnetwork.blogbe.dto.LoginDTO;
import com.yiyunnetwork.blogbe.dto.TokenDTO;

public interface AuthService {
    TokenDTO login(LoginDTO loginDTO);
    AdminInfoDTO getCurrentAdmin();
    void changePassword(String oldPassword, String newPassword);
    void logout();
} 