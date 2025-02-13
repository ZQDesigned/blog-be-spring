package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.TagDTO;
import com.yiyunnetwork.blogbe.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/list")
    public Result<List<TagDTO>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    @GetMapping("/{id}")
    public Result<TagDTO> getTagById(@PathVariable Long id) {
        return Result.success(tagService.getTagById(id));
    }

    @PostMapping
    public Result<TagDTO> createTag(@RequestBody @Valid TagDTO tagDTO) {
        return Result.success(tagService.createTag(tagDTO));
    }

    @PutMapping("/{id}")
    public Result<TagDTO> updateTag(@PathVariable Long id, @RequestBody @Valid TagDTO tagDTO) {
        return Result.success(tagService.updateTag(id, tagDTO));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return Result.success();
    }
} 