package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.FooterProfileDTO;
import com.yiyunnetwork.blogbe.entity.FooterProfile;
import com.yiyunnetwork.blogbe.service.FooterProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FooterProfileController {

    private final FooterProfileService footerProfileService;

    @GetMapping("/api/footer/profile")
    public Result<FooterProfileDTO> getFooterProfile() {
        return Result.success(footerProfileService.getFooterProfile());
    }

    @GetMapping("/api/footer/profile/config")
    public Result<FooterProfile> getFooterProfileConfig() {
        return Result.success(footerProfileService.getFooterProfileConfig());
    }

    @PutMapping("/api/footer/profile/config")
    public Result<FooterProfile> updateFooterProfileConfig(@RequestBody @Valid FooterProfile profile) {
        return Result.success(footerProfileService.updateFooterProfileConfig(profile));
    }
} 