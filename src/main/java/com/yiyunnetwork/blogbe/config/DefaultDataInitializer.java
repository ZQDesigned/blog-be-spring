package com.yiyunnetwork.blogbe.config;

import com.yiyunnetwork.blogbe.entity.Category;
import com.yiyunnetwork.blogbe.entity.Tag;
import com.yiyunnetwork.blogbe.repository.CategoryRepository;
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

@Slf4j
@Component
@RequiredArgsConstructor
@Order(2) // 在 AdminInitializer 之后运行
public class DefaultDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
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
} 