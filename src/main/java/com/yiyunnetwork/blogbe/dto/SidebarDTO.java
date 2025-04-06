package com.yiyunnetwork.blogbe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SidebarDTO {
    private ProfileDTO profile;
    private List<AnnouncementDTO> announcements;
    private ContactDTO contact;
    private SettingsDTO settings;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProfileDTO {
        private String avatar;
        private String name;
        private String bio;
        private StatusDTO status;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StatusDTO {
        private boolean online;
        private String text;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AnnouncementDTO {
        private String title;
        private String content;
        private String type;
        private String link;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactDTO {
        private String email;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SettingsDTO {
        private boolean showWeather;
    }
} 