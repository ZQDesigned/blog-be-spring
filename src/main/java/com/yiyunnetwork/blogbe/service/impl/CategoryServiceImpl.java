package com.yiyunnetwork.blogbe.service.impl;

import com.yiyunnetwork.blogbe.dto.CategoryDTO;
import com.yiyunnetwork.blogbe.entity.Category;
import com.yiyunnetwork.blogbe.repository.CategoryRepository;
import com.yiyunnetwork.blogbe.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllByOrderByArticleCountDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "category", key = "#id")
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("分类不存在"));
        return convertToDTO(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "category"}, allEntries = true)
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("分类名称已存在");
        }

        Category category = new Category();
        updateCategoryFromDTO(category, categoryDTO);
        category = categoryRepository.save(category);
        return convertToDTO(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "category"}, allEntries = true)
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("分类不存在"));

        if (!category.getName().equals(categoryDTO.getName()) && 
            categoryRepository.existsByName(categoryDTO.getName())) {
            throw new IllegalArgumentException("分类名称已存在");
        }

        updateCategoryFromDTO(category, categoryDTO);
        category = categoryRepository.save(category);
        return convertToDTO(category);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"categories", "category"}, allEntries = true)
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("分类不存在"));

        if (category.getArticleCount() > 0) {
            throw new IllegalStateException("该分类下还有文章，无法删除");
        }

        categoryRepository.delete(category);
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setArticleCount(category.getArticleCount());
        dto.setCreateTime(category.getCreateTime());
        dto.setUpdateTime(category.getUpdateTime());
        return dto;
    }

    private void updateCategoryFromDTO(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }
} 