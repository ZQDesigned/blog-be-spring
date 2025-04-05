package com.yiyunnetwork.blogbe.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.dto.HomeContentDTO;
import com.yiyunnetwork.blogbe.dto.HomeStatsDTO;
import com.yiyunnetwork.blogbe.entity.HomeSection;
import com.yiyunnetwork.blogbe.entity.SiteMeta;
import com.yiyunnetwork.blogbe.repository.*;
import com.yiyunnetwork.blogbe.service.HomeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final HomeSectionRepository homeSectionRepository;
    private final BlogMetaRepository blogMetaRepository;
    private final ProjectRepository projectRepository;
    private final ViewLogRepository viewLogRepository;
    private final SiteMetaRepository siteMetaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public HomeContentDTO getHomeContent() {
        HomeContentDTO contentDTO = new HomeContentDTO();
        List<HomeContentDTO.SectionDTO> sections = new ArrayList<>();
        
        // 获取所有启用的区块，按排序顺序
        List<HomeSection> homeSections = homeSectionRepository.findByEnabledTrueOrderBySortOrderAsc();
        log.debug("找到 {} 个启用的区块", homeSections.size());
        
        for (HomeSection section : homeSections) {
            try {
                log.debug("正在处理区块: id={}, type={}, title={}", section.getId(), section.getType(), section.getTitle());
                HomeContentDTO.SectionDTO sectionDTO = convertToSectionDTO(section);
                sections.add(sectionDTO);
                log.debug("区块处理成功");
            } catch (JsonProcessingException e) {
                log.error("处理区块时发生错误: id={}, type={}, error={}", section.getId(), section.getType(), e.getMessage(), e);
                // 创建一个基本的 SectionDTO，只包含基本信息
                HomeContentDTO.SectionDTO basicSectionDTO = new HomeContentDTO.SectionDTO();
                basicSectionDTO.setType(section.getType());
                basicSectionDTO.setTitle(section.getTitle());
                basicSectionDTO.setDescription(section.getDescription());
                sections.add(basicSectionDTO);
            }
        }
        
        contentDTO.setSections(sections);
        
        // 设置元数据
        SiteMeta siteMeta = getSiteMeta();
        HomeContentDTO.MetaDTO meta = new HomeContentDTO.MetaDTO();
        meta.setTitle(siteMeta.getTitle());
        meta.setDescription(siteMeta.getDescription());
        meta.setKeywords(Arrays.asList(siteMeta.getKeywords().split(",")));
        meta.setUpdateTime(siteMeta.getUpdateTime());
        contentDTO.setMeta(meta);
        
        return contentDTO;
    }

    @Override
    public HomeStatsDTO getHomeStats() {
        HomeStatsDTO stats = new HomeStatsDTO();
        
        // 获取文章总数
        stats.setTotalArticles(blogMetaRepository.countByIsDeletedFalse());
        
        // 获取项目总数
        stats.setTotalProjects(projectRepository.countByIsDeletedFalse());
        
        // 获取总访问量
        stats.setTotalViews(viewLogRepository.count());
        
        // 获取最后更新时间
        stats.setLastUpdateTime(LocalDateTime.now());
        
        return stats;
    }

    @Override
    public List<HomeSection> getAllSections() {
        return homeSectionRepository.findAll();
    }

    @Override
    public HomeSection getSectionById(Long id) {
        return homeSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("区块不存在"));
    }

    @Override
    @Transactional
    public HomeSection createSection(HomeSection section) {
        return homeSectionRepository.save(section);
    }

    @Override
    @Transactional
    public HomeSection updateSection(Long id, HomeSection section) {
        HomeSection existingSection = getSectionById(id);
        
        // 只更新非 null 字段
        if (section.getType() != null) {
            existingSection.setType(section.getType());
        }
        if (section.getTitle() != null) {
            existingSection.setTitle(section.getTitle());
        }
        if (section.getDescription() != null) {
            existingSection.setDescription(section.getDescription());
        }
        if (section.getContent() != null) {
            existingSection.setContent(section.getContent());
        }
        if (section.getEnabled() != null) {
            existingSection.setEnabled(section.getEnabled());
        }
        if (section.getSortOrder() != null) {
            existingSection.setSortOrder(section.getSortOrder());
        }
        
        return homeSectionRepository.save(existingSection);
    }

    @Override
    @Transactional
    public void deleteSection(Long id) {
        homeSectionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateSectionOrder(List<Long> sectionIds) {
        for (int i = 0; i < sectionIds.size(); i++) {
            HomeSection section = getSectionById(sectionIds.get(i));
            section.setSortOrder(i);
            homeSectionRepository.save(section);
        }
    }

    private HomeContentDTO.SectionDTO convertToSectionDTO(HomeSection section) throws JsonProcessingException {
        HomeContentDTO.SectionDTO sectionDTO = new HomeContentDTO.SectionDTO();
        sectionDTO.setType(section.getType());
        sectionDTO.setTitle(section.getTitle());
        sectionDTO.setDescription(section.getDescription());
        
        // 根据区块类型解析具体内容
        switch (section.getType()) {
            case "banner":
                HomeContentDTO.BannerContentDTO bannerContent = objectMapper.readValue(section.getContent(), 
                    HomeContentDTO.BannerContentDTO.class);
                sectionDTO.setBanner(bannerContent);
                break;
            case "features":
                sectionDTO.setItems(objectMapper.readValue(section.getContent(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, HomeContentDTO.FeatureItemDTO.class)));
                break;
            case "skills":
                sectionDTO.setCategories(objectMapper.readValue(section.getContent(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, HomeContentDTO.SkillCategoryDTO.class)));
                break;
            case "timeline":
                sectionDTO.setTimelineItems(objectMapper.readValue(section.getContent(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, HomeContentDTO.TimelineItemDTO.class)));
                break;
            case "contact":
                sectionDTO.setContactItems(objectMapper.readValue(section.getContent(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, HomeContentDTO.ContactItemDTO.class)));
                break;
        }
        
        return sectionDTO;
    }

    @Override
    public SiteMeta getSiteMeta() {
        SiteMeta siteMeta = siteMetaRepository.findFirstByOrderByIdAsc();
        if (siteMeta == null) {
            // 如果不存在，创建默认元数据
            siteMeta = new SiteMeta();
            siteMeta.setTitle("ZQDesigned 的个人网站");
            siteMeta.setDescription("全栈开发者的技术博客");
            siteMeta.setKeywords("全栈开发,游戏开发,技术博客");
            siteMeta = siteMetaRepository.save(siteMeta);
        }
        return siteMeta;
    }

    @Override
    @Transactional
    public SiteMeta updateSiteMeta(SiteMeta siteMeta) {
        SiteMeta existingMeta = getSiteMeta();
        existingMeta.setTitle(siteMeta.getTitle());
        existingMeta.setDescription(siteMeta.getDescription());
        existingMeta.setKeywords(siteMeta.getKeywords());
        return siteMetaRepository.save(existingMeta);
    }
} 