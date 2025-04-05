package com.yiyunnetwork.blogbe.service.impl;

import com.yiyunnetwork.blogbe.dto.AdminInfoDTO;
import com.yiyunnetwork.blogbe.dto.LoginDTO;
import com.yiyunnetwork.blogbe.dto.TokenDTO;
import com.yiyunnetwork.blogbe.entity.Admin;
import com.yiyunnetwork.blogbe.repository.AdminRepository;
import com.yiyunnetwork.blogbe.service.AuthService;
import com.yiyunnetwork.blogbe.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Override
    @Transactional
    public TokenDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        Admin admin = adminRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(request.getRemoteAddr());
        adminRepository.save(admin);

        return new TokenDTO(token, "Bearer", expiration / 1000);
    }

    @Override
    public AdminInfoDTO getCurrentAdmin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(username).orElseThrow();

        AdminInfoDTO adminInfoDTO = new AdminInfoDTO();
        adminInfoDTO.setId(admin.getId());
        adminInfoDTO.setUsername(admin.getUsername());
        adminInfoDTO.setLastLoginTime(admin.getLastLoginTime());
        adminInfoDTO.setLastLoginIp(admin.getLastLoginIp());

        return adminInfoDTO;
    }

    @Override
    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Admin admin = adminRepository.findByUsername(username).orElseThrow();

        if (!passwordEncoder.matches(oldPassword, admin.getPassword())) {
            throw new IllegalArgumentException("旧密码错误");
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(admin);
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public TokenDTO refreshToken(String token) {
        // 从旧令牌中提取用户名
        String username = jwtUtil.extractUsername(token);
        if (username == null) {
            throw new IllegalArgumentException("无效的令牌");
        }

        // 获取用户信息
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 创建 UserDetails 对象
        UserDetails userDetails = new User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        // 验证旧令牌是否有效（除了过期时间）
        if (!jwtUtil.validateToken(token, userDetails)) {
            throw new IllegalArgumentException("无效的令牌");
        }

        // 生成新令牌
        String newToken = jwtUtil.generateToken(userDetails);

        return new TokenDTO(newToken, "Bearer", expiration / 1000);
    }
} 