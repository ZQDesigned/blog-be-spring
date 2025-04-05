package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.AdminInfoDTO;
import com.yiyunnetwork.blogbe.dto.ChangePasswordDTO;
import com.yiyunnetwork.blogbe.dto.LoginDTO;
import com.yiyunnetwork.blogbe.dto.TokenDTO;
import com.yiyunnetwork.blogbe.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${jwt.header}")
    private String headerName;

    @Value("${jwt.prefix}")
    private String headerPrefix;

    @PostMapping("/login")
    public Result<TokenDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }

    @GetMapping("/info")
    public Result<AdminInfoDTO> getInfo() {
        return Result.success(authService.getCurrentAdmin());
    }

    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        authService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return Result.success();
    }

    @PostMapping("/logout")
    public Result<?> logout() {
        authService.logout();
        return Result.success();
    }

    @PostMapping("/refresh")
    public Result<TokenDTO> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(headerName);
        if (authHeader == null || !authHeader.startsWith(headerPrefix)) {
            return Result.error(401, "无效的令牌");
        }
        String token = authHeader.substring(headerPrefix.length()).trim();
        return Result.success(authService.refreshToken(token));
    }
} 