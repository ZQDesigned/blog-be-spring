package com.yiyunnetwork.blogbe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AboutMeDTO {
    private List<SectionDTO> sections;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SectionDTO {
        private String type;
        private String title;
        private String id; // 仅用于自定义区块
        
        // 不同类型的内容
        private ProfileContent profile;
        private SkillsContent skills;
        private JourneyContent journey;
        private ContactContent contact;
        private CustomContent custom;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProfileContent {
        private String avatar;
        private String bio;
        private List<Education> education;
        private String location;
        private List<String> highlights;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Education {
        private String school;
        private String degree;
        private String major;
        private String time;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SkillsContent {
        private List<SkillCategory> categories;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SkillCategory {
        private String name;
        private List<String> items;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JourneyContent {
        private List<String> description;
        private List<Milestone> milestones;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Milestone {
        private String year;
        private String title;
        private String description;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactContent {
        private List<ContactItem> items;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactItem {
        private String type;
        private String icon;
        private String value;
        private String link;
        private Boolean isQrCode;
        private String qrCodeUrl;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomContent {
        private String description;
        private String blockType; // 'text', 'list', 或 'cards'
        private List<CustomItem> items;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CustomItem {
        private String title;
        private String description;
        private String icon;
        private String imageUrl;
        private String link;
    }
} 