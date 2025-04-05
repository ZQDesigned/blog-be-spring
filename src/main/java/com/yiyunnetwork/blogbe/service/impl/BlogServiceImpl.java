package com.yiyunnetwork.blogbe.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yiyunnetwork.blogbe.dto.BlogDTO;
import com.yiyunnetwork.blogbe.entity.*;
import com.yiyunnetwork.blogbe.repository.BlogMetaRepository;
import com.yiyunnetwork.blogbe.repository.CategoryRepository;
import com.yiyunnetwork.blogbe.repository.TagRepository;
import com.yiyunnetwork.blogbe.repository.ViewLogRepository;
import com.yiyunnetwork.blogbe.service.BlogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
// TODO: 博客缓存功能暂时禁用，需要重新设计缓存策略
// import org.springframework.cache.annotation.CacheEvict;
// import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogMetaRepository blogMetaRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ViewLogRepository viewLogRepository;
    private final ObjectMapper objectMapper;

    @Override
    // TODO: 博客列表缓存功能暂时禁用，需要解决 Page 对象序列化问题
    // @Cacheable(value = "blogList", key = "'list:' + #pageable.pageNumber + '_' + #pageable.pageSize + '_' + #tag + '_' + #category")
    public Page<BlogDTO> getBlogList(Pageable pageable, String tag, String category) {
        Page<BlogMeta> blogPage;
        if (tag != null && !tag.isEmpty()) {
            List<String> tagNames = Arrays.asList(tag.split(","));
            blogPage = blogMetaRepository.findByIsDeletedFalseAndBlogTags_Tag_NameIn(tagNames, pageable);
        } else if (category != null && !category.isEmpty()) {
            blogPage = blogMetaRepository.findByIsDeletedFalseAndCategory_Name(category, pageable);
        } else {
            blogPage = blogMetaRepository.findByIsDeletedFalse(pageable);
        }
        
        List<BlogDTO> dtoList = blogPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
                
        return new PageImpl<>(dtoList, pageable, blogPage.getTotalElements());
    }

    @Override
    // TODO: 博客详情缓存功能暂时禁用
    // @Cacheable(value = "blog", key = "#id")
    public BlogDTO getBlogById(Long id) {
        BlogMeta blogMeta = blogMetaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + id));
        return convertToDTO(blogMeta);
    }

    @Override
    @Transactional
    // TODO: 博客缓存清除功能暂时禁用
    // @CacheEvict(value = {"blog", "blogList"}, allEntries = true)
    public BlogDTO createBlog(BlogDTO blogDTO) {
        Category category = categoryRepository.findById(blogDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        BlogMeta blogMeta = new BlogMeta();
        updateBlogMetaFromDTO(blogMeta, blogDTO);

        BlogContent blogContent = new BlogContent();
        blogContent.setContent(blogDTO.getContent());
        blogContent.setBlogMeta(blogMeta);
        blogMeta.setContent(blogContent);

        // 设置分类
        blogMeta.setCategory(category);
        categoryRepository.incrementArticleCount(category.getId());

        // 设置标签
        if (blogDTO.getTagIds() != null) {
            for (Long tagId : blogDTO.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
                blogMeta.addTag(tag);
                tagRepository.incrementArticleCount(tagId);
            }
        }

        blogMeta = blogMetaRepository.save(blogMeta);
        return convertToDTO(blogMeta);
    }

    @Override
    @Transactional
    // TODO: 博客缓存清除功能暂时禁用
    // @CacheEvict(value = {"blog", "blogList"}, allEntries = true)
    public BlogDTO updateBlog(Long id, BlogDTO blogDTO) {
        BlogMeta blogMeta = blogMetaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + id));
        
        // 更新分类
        if (!blogMeta.getCategory().getId().equals(blogDTO.getCategoryId())) {
            categoryRepository.decrementArticleCount(blogMeta.getCategory().getId());
            Category newCategory = categoryRepository.findById(blogDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            categoryRepository.incrementArticleCount(newCategory.getId());
            blogMeta.setCategory(newCategory);
        }

        // 更新标签
        List<Long> oldTagIds = blogMeta.getBlogTags().stream()
                .map(bt -> bt.getTag().getId())
                .toList();
        
        // 移除旧标签
        for (Long tagId : oldTagIds) {
            if (blogDTO.getTagIds() == null || !blogDTO.getTagIds().contains(tagId)) {
                tagRepository.decrementArticleCount(tagId);
            }
        }
        blogMeta.removeTags();

        // 添加新标签
        if (blogDTO.getTagIds() != null) {
            for (Long tagId : blogDTO.getTagIds()) {
                Tag tag = tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId));
                blogMeta.addTag(tag);
                if (!oldTagIds.contains(tagId)) {
                    tagRepository.incrementArticleCount(tagId);
                }
            }
        }

        updateBlogMetaFromDTO(blogMeta, blogDTO);
        blogMeta.getContent().setContent(blogDTO.getContent());
        
        blogMeta = blogMetaRepository.save(blogMeta);
        return convertToDTO(blogMeta);
    }

    @Override
    @Transactional
    // TODO: 博客缓存清除功能暂时禁用
    // @CacheEvict(value = {"blog", "blogList"}, allEntries = true)
    public void deleteBlog(Long id) {
        BlogMeta blogMeta = blogMetaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Blog not found with id: " + id));

        // 减少分类文章计数
        categoryRepository.decrementArticleCount(blogMeta.getCategory().getId());

        // 减少标签文章计数
        for (BlogTag blogTag : blogMeta.getBlogTags()) {
            tagRepository.decrementArticleCount(blogTag.getTag().getId());
        }

        blogMeta.setIsDeleted(true);
        blogMetaRepository.save(blogMeta);
    }

    @Override
    @Transactional
    public void recordView(Long id, String ip, String userAgent) {
        // 检查博客是否存在
        if (!blogMetaRepository.existsById(id)) {
            throw new EntityNotFoundException("Blog not found with id: " + id);
        }

        // 检查是否在最近1秒内有相同的访问记录
        LocalDateTime checkTime = LocalDateTime.now().minusSeconds(1);
        boolean hasRecentView = viewLogRepository.existsByBlogIdAndIpAndUserAgentAndCreateTimeGreaterThan(
            id, ip, userAgent, checkTime
        );

        // 如果没有最近的访问记录，则记录新的访问
        if (!hasRecentView) {
            ViewLog viewLog = new ViewLog();
            viewLog.setBlogId(id);
            viewLog.setIp(ip);
            viewLog.setUserAgent(userAgent);
            viewLogRepository.save(viewLog);
        }
    }

    @Override
    public Long getViewCount(Long id) {
        return viewLogRepository.countByBlogId(id);
    }

    private BlogDTO convertToDTO(BlogMeta blogMeta) {
        BlogDTO dto = new BlogDTO();
        dto.setId(blogMeta.getId());
        dto.setTitle(blogMeta.getTitle());
        dto.setSummary(blogMeta.getSummary());
        dto.setViewCount(getViewCount(blogMeta.getId()));
        dto.setCreateTime(blogMeta.getCreateTime());
        dto.setUpdateTime(blogMeta.getUpdateTime());
        
        // 设置分类信息
        dto.setCategoryId(blogMeta.getCategory().getId());
        dto.setCategoryName(blogMeta.getCategory().getName());
        
        // 设置标签信息
        List<Long> tagIds = new ArrayList<>();
        List<String> tagNames = new ArrayList<>();
        for (BlogTag blogTag : blogMeta.getBlogTags()) {
            tagIds.add(blogTag.getTag().getId());
            tagNames.add(blogTag.getTag().getName());
        }
        dto.setTagIds(tagIds);
        dto.setTagNames(tagNames);
        
        if (blogMeta.getContent() != null) {
            dto.setContent(blogMeta.getContent().getContent());
        }
        
        return dto;
    }

    private void updateBlogMetaFromDTO(BlogMeta blogMeta, BlogDTO dto) {
        blogMeta.setTitle(dto.getTitle());
        blogMeta.setSummary(dto.getSummary());
    }
} 