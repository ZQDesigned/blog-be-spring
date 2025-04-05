package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.HomeContentDTO;
import com.yiyunnetwork.blogbe.dto.HomeStatsDTO;
import com.yiyunnetwork.blogbe.entity.HomeSection;
import com.yiyunnetwork.blogbe.entity.SiteMeta;

import java.util.List;

public interface HomeService {
    HomeContentDTO getHomeContent();
    HomeStatsDTO getHomeStats();
    
    // 管理端接口
    List<HomeSection> getAllSections();
    HomeSection getSectionById(Long id);
    HomeSection createSection(HomeSection section);
    HomeSection updateSection(Long id, HomeSection section);
    void deleteSection(Long id);
    void updateSectionOrder(List<Long> sectionIds);

    // 网站元数据管理接口
    SiteMeta getSiteMeta();
    SiteMeta updateSiteMeta(SiteMeta siteMeta);
} 