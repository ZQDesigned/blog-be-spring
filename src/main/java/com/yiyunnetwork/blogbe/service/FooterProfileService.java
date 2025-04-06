package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.FooterProfileDTO;
import com.yiyunnetwork.blogbe.entity.FooterProfile;

public interface FooterProfileService {
    FooterProfileDTO getFooterProfile();
    FooterProfile getFooterProfileConfig();
    FooterProfile updateFooterProfileConfig(FooterProfile profile);
} 