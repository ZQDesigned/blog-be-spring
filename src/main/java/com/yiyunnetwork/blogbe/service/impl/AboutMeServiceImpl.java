package com.yiyunnetwork.blogbe.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.dto.AboutMeDTO;
import com.yiyunnetwork.blogbe.entity.AboutMeSection;
import com.yiyunnetwork.blogbe.repository.AboutMeSectionRepository;
import com.yiyunnetwork.blogbe.service.AboutMeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AboutMeServiceImpl implements AboutMeService {

    private final AboutMeSectionRepository aboutMeSectionRepository;
    private final ObjectMapper objectMapper;

    @Override
    public AboutMeDTO getAboutMe() {
        List<AboutMeSection> sections = aboutMeSectionRepository.findByEnabledTrueOrderBySortOrderAsc();
        AboutMeDTO response = new AboutMeDTO();
        List<AboutMeDTO.SectionDTO> sectionDTOs = new ArrayList<>();
        
        for (AboutMeSection section : sections) {
            try {
                AboutMeDTO.SectionDTO sectionDTO = convertToSectionDTO(section);
                sectionDTOs.add(sectionDTO);
            } catch (JsonProcessingException e) {
                log.error("处理区块内容失败: id={}, type={}, error={}", 
                          section.getId(), section.getType(), e.getMessage());
            }
        }
        
        response.setSections(sectionDTOs);
        return response;
    }

    private AboutMeDTO.SectionDTO convertToSectionDTO(AboutMeSection section) throws JsonProcessingException {
        AboutMeDTO.SectionDTO sectionDTO = new AboutMeDTO.SectionDTO();
        sectionDTO.setType(section.getType());
        sectionDTO.setTitle(section.getTitle());
        
        if (section.getSectionId() != null) {
            sectionDTO.setId(section.getSectionId());
        }
        
        // 根据区块类型解析内容
        switch (section.getType()) {
            case "profile":
                AboutMeDTO.ProfileContent profileContent = objectMapper.readValue(
                        section.getContent(), AboutMeDTO.ProfileContent.class);
                sectionDTO.setProfile(profileContent);
                break;
                
            case "skills":
                AboutMeDTO.SkillsContent skillsContent = objectMapper.readValue(
                        section.getContent(), AboutMeDTO.SkillsContent.class);
                sectionDTO.setSkills(skillsContent);
                break;
                
            case "journey":
                AboutMeDTO.JourneyContent journeyContent = objectMapper.readValue(
                        section.getContent(), AboutMeDTO.JourneyContent.class);
                sectionDTO.setJourney(journeyContent);
                break;
                
            case "contact":
                AboutMeDTO.ContactContent contactContent = objectMapper.readValue(
                        section.getContent(), AboutMeDTO.ContactContent.class);
                sectionDTO.setContact(contactContent);
                break;
                
            case "custom":
                AboutMeDTO.CustomContent customContent = objectMapper.readValue(
                        section.getContent(), AboutMeDTO.CustomContent.class);
                sectionDTO.setCustom(customContent);
                break;
                
            default:
                log.warn("未知区块类型: {}", section.getType());
                break;
        }
        
        return sectionDTO;
    }

    @Override
    public List<AboutMeSection> getAllSections() {
        return aboutMeSectionRepository.findAll();
    }

    @Override
    public AboutMeSection getSectionById(Long id) {
        return aboutMeSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("区块不存在: id=" + id));
    }

    @Override
    @Transactional
    public AboutMeSection createSection(AboutMeSection section) {
        // 如果没有设置排序顺序，设置为当前最大值+1
        if (section.getSortOrder() == null) {
            long count = aboutMeSectionRepository.count();
            section.setSortOrder((int) count);
        }
        return aboutMeSectionRepository.save(section);
    }

    @Override
    @Transactional
    public AboutMeSection updateSection(Long id, AboutMeSection section) {
        AboutMeSection existingSection = getSectionById(id);
        
        if (section.getType() != null) {
            existingSection.setType(section.getType());
        }
        
        if (section.getTitle() != null) {
            existingSection.setTitle(section.getTitle());
        }
        
        if (section.getSectionId() != null) {
            existingSection.setSectionId(section.getSectionId());
        }
        
        if (section.getContent() != null) {
            existingSection.setContent(section.getContent());
        }
        
        if (section.getSortOrder() != null) {
            existingSection.setSortOrder(section.getSortOrder());
        }
        
        if (section.getEnabled() != null) {
            existingSection.setEnabled(section.getEnabled());
        }
        
        return aboutMeSectionRepository.save(existingSection);
    }

    @Override
    @Transactional
    public void deleteSection(Long id) {
        aboutMeSectionRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateSectionOrder(List<Long> sectionIds) {
        for (int i = 0; i < sectionIds.size(); i++) {
            Long id = sectionIds.get(i);
            AboutMeSection section = getSectionById(id);
            section.setSortOrder(i);
            aboutMeSectionRepository.save(section);
        }
    }
} 