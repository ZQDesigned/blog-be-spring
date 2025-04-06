package com.yiyunnetwork.blogbe.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.dto.SidebarDTO;
import com.yiyunnetwork.blogbe.entity.SidebarConfig;
import com.yiyunnetwork.blogbe.repository.SidebarConfigRepository;
import com.yiyunnetwork.blogbe.service.SidebarService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SidebarServiceImpl implements SidebarService {

    private final SidebarConfigRepository sidebarConfigRepository;
    private final ObjectMapper objectMapper;

    @Override
    public SidebarDTO getSidebarData() {
        SidebarConfig config = sidebarConfigRepository.findFirstByOrderByIdAsc();
        if (config == null) {
            throw new EntityNotFoundException("侧边栏配置不存在");
        }

        SidebarDTO dto = new SidebarDTO();

        // 设置个人资料
        SidebarDTO.ProfileDTO profile = new SidebarDTO.ProfileDTO();
        profile.setAvatar(config.getAvatar());
        profile.setName(config.getName());
        profile.setBio(config.getBio());

        SidebarDTO.StatusDTO status = new SidebarDTO.StatusDTO();
        status.setOnline(config.getOnline());
        status.setText(config.getStatusText());
        profile.setStatus(status);

        dto.setProfile(profile);

        // 设置公告
        try {
            List<SidebarDTO.AnnouncementDTO> announcements = objectMapper.readValue(
                config.getAnnouncements(),
                new TypeReference<List<SidebarDTO.AnnouncementDTO>>() {}
            );
            dto.setAnnouncements(announcements);
        } catch (Exception e) {
            dto.setAnnouncements(new ArrayList<>());
        }

        // 设置联系方式
        SidebarDTO.ContactDTO contact = new SidebarDTO.ContactDTO();
        contact.setEmail(config.getEmail());
        dto.setContact(contact);

        // 设置设置项
        SidebarDTO.SettingsDTO settings = new SidebarDTO.SettingsDTO();
        settings.setShowWeather(config.getShowWeather());
        dto.setSettings(settings);

        return dto;
    }

    @Override
    public SidebarConfig getSidebarConfig() {
        return sidebarConfigRepository.findFirstByOrderByIdAsc();
    }

    @Override
    @Transactional
    public SidebarConfig updateSidebarConfig(SidebarConfig config) {
        SidebarConfig existingConfig = sidebarConfigRepository.findFirstByOrderByIdAsc();
        if (existingConfig == null) {
            return sidebarConfigRepository.save(config);
        }

        existingConfig.setAvatar(config.getAvatar());
        existingConfig.setName(config.getName());
        existingConfig.setBio(config.getBio());
        existingConfig.setOnline(config.getOnline());
        existingConfig.setStatusText(config.getStatusText());
        existingConfig.setAnnouncements(config.getAnnouncements());
        existingConfig.setEmail(config.getEmail());
        existingConfig.setShowWeather(config.getShowWeather());

        return sidebarConfigRepository.save(existingConfig);
    }
} 