package com.yiyunnetwork.blogbe.controller;

import com.yiyunnetwork.blogbe.common.Result;
import com.yiyunnetwork.blogbe.dto.BlogDTO;
import com.yiyunnetwork.blogbe.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/api/blog")
@RequiredArgsConstructor
@Validated
public class BlogController {

    private final BlogService blogService;
    private final HttpServletRequest request;

    @GetMapping("/list")
    public Result<Page<BlogDTO>> getBlogList(
            @RequestParam(defaultValue = "1") @Min(1) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "createTime") @Pattern(regexp = "createTime|viewCount", message = "排序字段不正确") String sort,
            @RequestParam(defaultValue = "desc") @Pattern(regexp = "asc|desc", message = "排序方式不正确") String order
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(direction, sort));
        return Result.success(blogService.getBlogList(pageRequest, tag, category));
    }

    @GetMapping("/{id}")
    public Result<BlogDTO> getBlogById(@PathVariable Long id) {
        return Result.success(blogService.getBlogById(id));
    }

    @PostMapping
    public Result<BlogDTO> createBlog(@RequestBody @Valid BlogDTO blogDTO) {
        return Result.success(blogService.createBlog(blogDTO));
    }

    @PutMapping("/{id}")
    public Result<BlogDTO> updateBlog(@PathVariable Long id, @RequestBody @Valid BlogDTO blogDTO) {
        return Result.success(blogService.updateBlog(id, blogDTO));
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return Result.success();
    }

    @PostMapping("/{id}/view")
    public Result<Long> recordView(@PathVariable Long id) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        blogService.recordView(id, ip, userAgent);
        return Result.success(blogService.getViewCount(id));
    }
} 