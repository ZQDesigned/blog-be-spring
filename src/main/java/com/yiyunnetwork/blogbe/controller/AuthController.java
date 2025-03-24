package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.AdminInfoDTO;
import com.yiyunnetwork.blogbe.dto.ChangePasswordDTO;
import com.yiyunnetwork.blogbe.dto.LoginDTO;
import com.yiyunnetwork.blogbe.dto.TokenDTO;
import com.yiyunnetwork.blogbe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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
} 