package com.yiyunnetwork.blogbe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HomeContentDTO {
    private List<SectionDTO> sections;
    private MetaDTO meta;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SectionDTO {
        private String type;
        private String title;
        private String description;
        // banner 类型的内容
        private BannerContentDTO banner;
        // 其他类型的内容
        private List<ButtonDTO> buttons;
        private List<FeatureItemDTO> items;
        private List<SkillCategoryDTO> categories;
        private List<TimelineItemDTO> timelineItems;
        private List<ContactItemDTO> contactItems;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class BannerContentDTO {
        private String subtitle;
        private String backgroundImage;
        private List<ButtonDTO> buttons;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ButtonDTO {
        private String text;
        private String link;
        private String type;
        private String icon;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FeatureItemDTO {
        private String icon;
        private String title;
        private String description;
        private String link;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SkillCategoryDTO {
        private String name;
        private List<SkillItemDTO> items;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SkillItemDTO {
        private String name;
        private String icon;
        private Integer level;
        private String description;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class TimelineItemDTO {
        private String date;
        private String title;
        private String description;
        private String icon;
        private String color;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactItemDTO {
        private String type;
        private String icon;
        private String value;
        private String link;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetaDTO {
        private String title;
        private String description;
        private List<String> keywords;
        private LocalDateTime updateTime;
    }
} 