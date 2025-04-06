package com.yiyunnetwork.blogbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.entity.Category;
import com.yiyunnetwork.blogbe.entity.SidebarConfig;
import com.yiyunnetwork.blogbe.entity.SiteMeta;
import com.yiyunnetwork.blogbe.entity.Tag;
import com.yiyunnetwork.blogbe.repository.CategoryRepository;
import com.yiyunnetwork.blogbe.repository.SidebarConfigRepository;
import com.yiyunnetwork.blogbe.repository.SiteMetaRepository;
import com.yiyunnetwork.blogbe.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2) // 在 AdminInitializer 之后运行
public class DefaultDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final SiteMetaRepository siteMetaRepository;
    private final SidebarConfigRepository sidebarConfigRepository;
    private final ObjectMapper objectMapper;
    private static final String CONFIG_DIR = "configs";
    private static final String INIT_FLAG_FILE = ".default_data_initialized";

    @Override
    public void run(String... args) {
        if (!isInitialized()) {
            initializeDefaultData();
            createInitFlag();
        }
    }

    private boolean isInitialized() {
        Path flagFile = getFlagFilePath();
        return Files.exists(flagFile);
    }

    private void createInitFlag() {
        try {
            Path configDir = getConfigDirPath();
            if (!Files.exists(configDir)) {
                Files.createDirectory(configDir);
            }
            Path flagFile = getFlagFilePath();
            Files.createFile(flagFile);
            log.info("创建初始化标记文件: {}", flagFile);
        } catch (IOException e) {
            log.error("创建初始化标记文件失败", e);
        }
    }

    private Path getConfigDirPath() {
        String userDir = System.getProperty("user.dir");
        return Paths.get(userDir + File.separator + CONFIG_DIR);
    }

    private Path getFlagFilePath() {
        return Paths.get(getConfigDirPath().toString(), INIT_FLAG_FILE);
    }

    private void initializeDefaultData() {
        try {
            initializeDefaultCategory();
            initializeDefaultTag();
            initializeDefaultSiteMeta();
            initializeDefaultSidebar();
            log.info("默认数据初始化完成");
        } catch (Exception e) {
            log.error("初始化默认数据失败", e);
        }
    }

    private void initializeDefaultCategory() {
        if (!categoryRepository.existsByName("默认分类")) {
            Category category = new Category();
            category.setName("默认分类");
            category.setDescription("系统默认分类");
            category.setArticleCount(0);
            categoryRepository.save(category);
            log.info("已创建默认分类");
        }
    }

    private void initializeDefaultTag() {
        if (!tagRepository.existsByName("默认标签")) {
            Tag tag = new Tag();
            tag.setName("默认标签");
            tag.setDescription("系统默认标签");
            tag.setArticleCount(0);
            tagRepository.save(tag);
            log.info("已创建默认标签");
        }
    }

    private void initializeDefaultSiteMeta() {
        if (siteMetaRepository.count() == 0) {
            SiteMeta siteMeta = new SiteMeta();
            siteMeta.setTitle("ZQDesigned 的个人网站");
            siteMeta.setDescription("全栈开发者的技术博客与项目展示");
            siteMeta.setKeywords("全栈开发,Java,Spring Boot,Vue.js,React,游戏开发,技术博客");
            siteMetaRepository.save(siteMeta);
            log.info("已创建默认网站元数据");
        }
    }

    private void initializeDefaultSidebar() {
        if (sidebarConfigRepository.count() == 0) {
            SidebarConfig config = new SidebarConfig();
            config.setAvatar("/avatar.jpg");
            config.setName("ZQDesigned");
            config.setBio("分享开发历程、科技生活～");
            config.setOnline(true);
            config.setStatusText("一日之计在于晨");
            config.setEmail("zqdesigned@mail.lnyynet.com");
            config.setShowWeather(true);

            try {
                List<Map<String, Object>> announcements = new ArrayList<>();
                
                Map<String, Object> welcome = new HashMap<>();
                welcome.put("title", "👋 欢迎");
                welcome.put("content", "我是 ZQDesigned！欢迎你！");
                welcome.put("type", "text");
                announcements.add(welcome);

                Map<String, Object> newFeature = new HashMap<>();
                newFeature.put("title", "🎉 新功能");
                newFeature.put("content", "查看最新功能");
                newFeature.put("type", "link");
                newFeature.put("link", "/blog/1");
                announcements.add(newFeature);

                Map<String, Object> question = new HashMap<>();
                question.put("title", "❓ 问题");
                question.put("content", "有任何问题欢迎评论区交流！");
                question.put("type", "text");
                announcements.add(question);

                config.setAnnouncements(objectMapper.writeValueAsString(announcements));
            } catch (Exception e) {
                log.error("初始化公告数据失败", e);
                config.setAnnouncements("[]");
            }

            sidebarConfigRepository.save(config);
            log.info("已创建默认侧边栏配置");
        }
    }
} 