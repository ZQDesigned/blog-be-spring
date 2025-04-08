package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.AboutMeDTO;
import com.yiyunnetwork.blogbe.entity.AboutMeSection;

import java.util.List;

public interface AboutMeService {
    // 前台接口
    AboutMeDTO getAboutMe();
    
    // 管理接口
    List<AboutMeSection> getAllSections();
    AboutMeSection getSectionById(Long id);
    AboutMeSection createSection(AboutMeSection section);
    AboutMeSection updateSection(Long id, AboutMeSection section);
    void deleteSection(Long id);
    void updateSectionOrder(List<Long> sectionIds);
} 