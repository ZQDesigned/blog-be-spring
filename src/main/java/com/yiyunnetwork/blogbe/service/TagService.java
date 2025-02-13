package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.TagDTO;

import java.util.List;

public interface TagService {
    List<TagDTO> getAllTags();
    TagDTO getTagById(Long id);
    TagDTO createTag(TagDTO tagDTO);
    TagDTO updateTag(Long id, TagDTO tagDTO);
    void deleteTag(Long id);
} 