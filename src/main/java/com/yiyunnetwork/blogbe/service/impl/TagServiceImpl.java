package com.yiyunnetwork.blogbe.service.impl;

import com.yiyunnetwork.blogbe.dto.TagDTO;
import com.yiyunnetwork.blogbe.entity.Tag;
import com.yiyunnetwork.blogbe.repository.TagRepository;
import com.yiyunnetwork.blogbe.service.TagService;
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
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    @Cacheable(value = "tags")
    public List<TagDTO> getAllTags() {
        return tagRepository.findAllByOrderByArticleCountDesc()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "tag", key = "#id")
    public TagDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("标签不存在"));
        return convertToDTO(tag);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tags", "tag"}, allEntries = true)
    public TagDTO createTag(TagDTO tagDTO) {
        if (tagRepository.existsByName(tagDTO.getName())) {
            throw new IllegalArgumentException("标签名称已存在");
        }

        Tag tag = new Tag();
        updateTagFromDTO(tag, tagDTO);
        tag = tagRepository.save(tag);
        return convertToDTO(tag);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tags", "tag"}, allEntries = true)
    public TagDTO updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("标签不存在"));

        if (!tag.getName().equals(tagDTO.getName()) && 
            tagRepository.existsByName(tagDTO.getName())) {
            throw new IllegalArgumentException("标签名称已存在");
        }

        updateTagFromDTO(tag, tagDTO);
        tag = tagRepository.save(tag);
        return convertToDTO(tag);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tags", "tag"}, allEntries = true)
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("标签不存在"));

        if (tag.getArticleCount() > 0) {
            throw new IllegalStateException("该标签下还有文章，无法删除");
        }

        tagRepository.delete(tag);
    }

    private TagDTO convertToDTO(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setDescription(tag.getDescription());
        dto.setArticleCount(tag.getArticleCount());
        dto.setCreateTime(tag.getCreateTime());
        dto.setUpdateTime(tag.getUpdateTime());
        return dto;
    }

    private void updateTagFromDTO(Tag tag, TagDTO dto) {
        tag.setName(dto.getName());
        tag.setDescription(dto.getDescription());
    }
} 