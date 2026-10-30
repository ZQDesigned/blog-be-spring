package com.yiyunnetwork.blogbe.service;

import com.yiyunnetwork.blogbe.dto.BlogDTO;
import com.yiyunnetwork.blogbe.dto.BlogSearchDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    List<BlogSearchDTO> searchBlogs(String keyword, int limit);
    Page<BlogDTO> getBlogList(Pageable pageable, String tag, String category, String keyword);
    BlogDTO getBlogById(Long id);
    BlogDTO createBlog(BlogDTO blogDTO);
    BlogDTO updateBlog(Long id, BlogDTO blogDTO);
    void deleteBlog(Long id);
    void recordView(Long id, String ip, String userAgent);
    Long getViewCount(Long id);
}
