package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.BlogMeta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlogMetaRepository extends JpaRepository<BlogMeta, Long> {
    Page<BlogMeta> findByIsDeletedFalse(Pageable pageable);
    
    @Query("SELECT DISTINCT b FROM BlogMeta b JOIN b.blogTags bt JOIN bt.tag t " +
           "WHERE b.isDeleted = false AND t.name IN ?1")
    Page<BlogMeta> findByIsDeletedFalseAndBlogTags_Tag_NameIn(List<String> tagNames, Pageable pageable);
    
    Page<BlogMeta> findByIsDeletedFalseAndCategory_Name(String categoryName, Pageable pageable);
    
    Long countByIsDeletedFalse();
} 