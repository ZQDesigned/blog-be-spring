package com.yiyunnetwork.blogbe.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.dto.FooterProfileDTO;
import com.yiyunnetwork.blogbe.entity.FooterProfile;
import com.yiyunnetwork.blogbe.repository.FooterProfileRepository;
import com.yiyunnetwork.blogbe.service.FooterProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FooterProfileServiceImpl implements FooterProfileService {

    private final FooterProfileRepository footerProfileRepository;
    private final ObjectMapper objectMapper;

    @Override
    public FooterProfileDTO getFooterProfile() {
        FooterProfile profile = footerProfileRepository.findFirstByOrderByIdAsc();
        if (profile == null) {
            throw new EntityNotFoundException("页脚配置不存在");
        }

        FooterProfileDTO dto = new FooterProfileDTO();
        try {
            dto.setLinks(objectMapper.readValue(
                profile.getLinks(),
                new TypeReference<ArrayList<FooterProfileDTO.LinkDTO>>() {}
            ));
        } catch (Exception e) {
            dto.setLinks(new ArrayList<>());
        }

        return dto;
    }

    @Override
    public FooterProfile getFooterProfileConfig() {
        return footerProfileRepository.findFirstByOrderByIdAsc();
    }

    @Override
    @Transactional
    public FooterProfile updateFooterProfileConfig(FooterProfile profile) {
        FooterProfile existingProfile = footerProfileRepository.findFirstByOrderByIdAsc();
        if (existingProfile == null) {
            return footerProfileRepository.save(profile);
        }

        existingProfile.setLinks(profile.getLinks());
        return footerProfileRepository.save(existingProfile);
    }
} 