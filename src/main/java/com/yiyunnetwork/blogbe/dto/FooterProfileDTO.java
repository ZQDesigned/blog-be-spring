package com.yiyunnetwork.blogbe.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FooterProfileDTO {
    private List<LinkDTO> links;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LinkDTO {
        private String title;
        private String url;
        private String icon;
        private Boolean isExternal;
    }
} 