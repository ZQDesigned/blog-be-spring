package com.yiyunnetwork.blogbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.entity.*;
import com.yiyunnetwork.blogbe.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
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
    private final FooterProfileRepository footerProfileRepository;
    private final AboutMeSectionRepository aboutMeSectionRepository;
    private final ObjectMapper objectMapper;
    private static final String CONFIG_DIR = "configs";
    private static final String INIT_FLAG_FILE = "data_initialization.json";

    @Override
    public void run(String... args) {
        Path configDir = getConfigDirPath();
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
                log.info("创建配置目录: {}", configDir);
            }

            Map<String, Boolean> initStatus = getInitializationStatus();
            initializeDefaultData(initStatus);
            saveInitializationStatus(initStatus);
        } catch (Exception e) {
            log.error("初始化配置失败", e);
        }
    }

    private Map<String, Boolean> getInitializationStatus() {
        Path flagFilePath = getFlagFilePath();
        Map<String, Boolean> initStatus = new HashMap<>();
        
        // 定义所有需要初始化的模块
        initStatus.put("category", false);
        initStatus.put("tag", false);
        initStatus.put("siteMeta", false);
        initStatus.put("sidebar", false);
        initStatus.put("footerProfile", false);
        initStatus.put("aboutMe", false);
        
        try {
            if (Files.exists(flagFilePath)) {
                String content = Files.readString(flagFilePath);
                Map<String, Boolean> savedStatus = objectMapper.readValue(content, 
                        objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Boolean.class));
                
                // 合并已保存的状态
                for (Map.Entry<String, Boolean> entry : savedStatus.entrySet()) {
                    if (initStatus.containsKey(entry.getKey())) {
                        initStatus.put(entry.getKey(), entry.getValue());
                    }
                }
                
                log.info("读取初始化状态: {}", initStatus);
            } else {
                log.info("初始化状态文件不存在，将创建新文件");
            }
        } catch (Exception e) {
            log.error("读取初始化状态文件失败，将使用默认状态", e);
        }
        
        return initStatus;
    }
    
    private void saveInitializationStatus(Map<String, Boolean> initStatus) {
        Path flagFilePath = getFlagFilePath();
        try {
            String content = objectMapper.writeValueAsString(initStatus);
            Files.writeString(flagFilePath, content);
            log.info("保存初始化状态: {}", initStatus);
        } catch (Exception e) {
            log.error("保存初始化状态失败", e);
        }
    }

    private Path getConfigDirPath() {
        String userDir = System.getProperty("user.dir");
        return Paths.get(userDir + File.separator + CONFIG_DIR);
    }

    private Path getFlagFilePath() {
        return Paths.get(getConfigDirPath().toString(), INIT_FLAG_FILE);
    }

    private void initializeDefaultData(Map<String, Boolean> initStatus) {
        try {
            if (!initStatus.get("category")) {
                initializeDefaultCategory();
                initStatus.put("category", true);
            }
            
            if (!initStatus.get("tag")) {
                initializeDefaultTag();
                initStatus.put("tag", true);
            }
            
            if (!initStatus.get("siteMeta")) {
                initializeDefaultSiteMeta();
                initStatus.put("siteMeta", true);
            }
            
            if (!initStatus.get("sidebar")) {
                initializeDefaultSidebar();
                initStatus.put("sidebar", true);
            }
            
            if (!initStatus.get("footerProfile")) {
                initializeDefaultFooterProfile();
                initStatus.put("footerProfile", true);
            }
            
            if (!initStatus.get("aboutMe")) {
                initializeDefaultAboutMe();
                initStatus.put("aboutMe", true);
            }
            
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
            siteMeta.setTitle("我的个人博客");
            siteMeta.setDescription("这是一个基于Spring Boot的个人博客系统");
            siteMeta.setKeywords("博客,技术,Java,Spring Boot");
            siteMetaRepository.save(siteMeta);
            log.info("已创建默认网站元数据");
        }
    }

    private void initializeDefaultSidebar() {
        if (sidebarConfigRepository.count() == 0) {
            SidebarConfig config = new SidebarConfig();
            config.setName("博主");
            config.setBio("一名热爱技术的开发者");
            config.setOnline(true);
            config.setStatusText("专注开发中...");
            config.setShowWeather(true);
            
            try {
                List<Map<String, Object>> announcements = new ArrayList<>();
                
                Map<String, Object> announcement1 = new HashMap<>();
                announcement1.put("title", "网站上线啦");
                announcement1.put("content", "个人博客网站正式上线，欢迎访问！");
                announcement1.put("type", "success");
                announcements.add(announcement1);
                
                Map<String, Object> announcement2 = new HashMap<>();
                announcement2.put("title", "新功能");
                announcement2.put("content", "评论功能即将上线，敬请期待！");
                announcement2.put("type", "info");
                announcements.add(announcement2);
                
                config.setAnnouncements(objectMapper.writeValueAsString(announcements));
            } catch (Exception e) {
                log.error("初始化侧边栏公告数据失败", e);
                config.setAnnouncements("[]");
            }
            
            sidebarConfigRepository.save(config);
            log.info("已创建默认侧边栏配置");
        }
    }

    private void initializeDefaultFooterProfile() {
        if (footerProfileRepository.count() == 0) {
            FooterProfile profile = new FooterProfile();
            try {
                List<Map<String, Object>> links = new ArrayList<>();

                Map<String, Object> github = new HashMap<>();
                github.put("title", "GitHub");
                github.put("url", "https://github.com/yourusername");
                github.put("icon", "GithubOutlined");
                github.put("isExternal", true);
                links.add(github);

                Map<String, Object> projects = new HashMap<>();
                projects.put("title", "我的项目");
                projects.put("url", "/projects");
                projects.put("icon", "ProjectOutlined");
                projects.put("isExternal", false);
                links.add(projects);

                profile.setLinks(objectMapper.writeValueAsString(links));
            } catch (Exception e) {
                log.error("初始化页脚链接数据失败", e);
                profile.setLinks("[]");
            }

            footerProfileRepository.save(profile);
            log.info("已创建默认页脚配置");
        }
    }

    private void initializeDefaultAboutMe() {
        if (aboutMeSectionRepository.count() == 0) {
            try {
                // 创建个人资料区块
                AboutMeSection profileSection = new AboutMeSection();
                profileSection.setType("profile");
                profileSection.setTitle("关于我");
                profileSection.setSortOrder(0);
                profileSection.setEnabled(true);
                
                Map<String, Object> profileContent = new HashMap<>();
                profileContent.put("avatar", "/uploads/default-avatar.jpg");
                profileContent.put("bio", "热爱技术，热爱生活");
                profileContent.put("location", "中国");
                
                List<Map<String, Object>> education = new ArrayList<>();
                Map<String, Object> edu1 = new HashMap<>();
                edu1.put("school", "XX大学");
                edu1.put("degree", "学士");
                edu1.put("major", "计算机科学与技术");
                edu1.put("time", "2016-2020");
                education.add(edu1);
                
                profileContent.put("education", education);
                
                List<String> highlights = new ArrayList<>();
                highlights.add("⚡ 自学 & 实践驱动的开发者");
                highlights.add("🎓 热爱探索新技术，持续学习并追求卓越");
                highlights.add("💡 善于解决复杂问题，具有创新思维");
                profileContent.put("highlights", highlights);
                
                profileSection.setContent(objectMapper.writeValueAsString(profileContent));
                aboutMeSectionRepository.save(profileSection);
                
                // 创建技能区块
                AboutMeSection skillsSection = new AboutMeSection();
                skillsSection.setType("skills");
                skillsSection.setTitle("专业技能");
                skillsSection.setSortOrder(1);
                skillsSection.setEnabled(true);
                
                Map<String, Object> skillsContent = new HashMap<>();
                List<Map<String, Object>> categories = new ArrayList<>();
                
                Map<String, Object> backendCategory = new HashMap<>();
                backendCategory.put("name", "后端开发");
                List<String> backendItems = new ArrayList<>();
                backendItems.add("Java");
                backendItems.add("Spring Boot");
                backendItems.add("MySQL");
                backendItems.add("Redis");
                backendCategory.put("items", backendItems);
                categories.add(backendCategory);
                
                Map<String, Object> frontendCategory = new HashMap<>();
                frontendCategory.put("name", "前端开发");
                List<String> frontendItems = new ArrayList<>();
                frontendItems.add("HTML/CSS");
                frontendItems.add("JavaScript");
                frontendItems.add("React");
                frontendItems.add("Vue.js");
                frontendCategory.put("items", frontendItems);
                categories.add(frontendCategory);
                
                skillsContent.put("categories", categories);
                skillsSection.setContent(objectMapper.writeValueAsString(skillsContent));
                aboutMeSectionRepository.save(skillsSection);
                
                // 创建联系方式区块
                AboutMeSection contactSection = new AboutMeSection();
                contactSection.setType("contact");
                contactSection.setTitle("联系我");
                contactSection.setSortOrder(2);
                contactSection.setEnabled(true);
                
                Map<String, Object> contactContent = new HashMap<>();
                List<Map<String, Object>> contactItems = new ArrayList<>();
                
                Map<String, Object> email = new HashMap<>();
                email.put("type", "邮箱");
                email.put("icon", "MailOutlined");
                email.put("value", "example@example.com");
                contactItems.add(email);
                
                Map<String, Object> github = new HashMap<>();
                github.put("type", "GitHub");
                github.put("icon", "GithubOutlined");
                github.put("value", "yourusername");
                github.put("link", "https://github.com/yourusername");
                contactItems.add(github);
                
                contactContent.put("items", contactItems);
                contactSection.setContent(objectMapper.writeValueAsString(contactContent));
                aboutMeSectionRepository.save(contactSection);
                
                log.info("已创建默认关于我页面数据");
            } catch (Exception e) {
                log.error("初始化关于我页面数据失败", e);
            }
        }
    }
} 