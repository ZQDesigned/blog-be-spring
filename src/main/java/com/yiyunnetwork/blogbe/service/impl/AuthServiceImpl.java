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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
} 