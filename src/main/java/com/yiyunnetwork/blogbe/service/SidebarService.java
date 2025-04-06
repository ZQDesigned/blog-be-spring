package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.SidebarDTO;
import com.yiyunnetwork.blogbe.entity.SidebarConfig;

public interface SidebarService {
    SidebarDTO getSidebarData();
    SidebarConfig getSidebarConfig();
    SidebarConfig updateSidebarConfig(SidebarConfig config);
} 