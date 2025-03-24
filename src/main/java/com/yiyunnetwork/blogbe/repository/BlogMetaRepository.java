package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.BlogMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogMetaRepository extends JpaRepository<BlogMeta, Long> {
    Page<BlogMeta> findByIsDeletedFalse(Pageable pageable);
    
    Page<BlogMeta> findByIsDeletedFalseAndBlogTags_Tag_Name(String tagName, Pageable pageable);
    
    Page<BlogMeta> findByIsDeletedFalseAndCategory_Name(String categoryName, Pageable pageable);
    
    Long countByIsDeletedFalse();
} 