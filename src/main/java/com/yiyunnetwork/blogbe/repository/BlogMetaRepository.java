package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.BlogMeta;
import com.yiyunnetwork.blogbe.dto.BlogSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogMetaRepository extends JpaRepository<BlogMeta, Long> {
    String VISIBLE_FILTER = """
        FROM BlogMeta b LEFT JOIN b.category c WHERE b.isDeleted = false
          AND (:category IS NULL OR c.name = :category)
          AND (:filterTags = false OR EXISTS (
            SELECT bt.id FROM BlogTag bt WHERE bt.blog = b AND bt.tag.name IN :tags
          ))
          AND (:keyword IS NULL OR LOWER(b.title) LIKE :keyword ESCAPE '!'
            OR LOWER(b.summary) LIKE :keyword ESCAPE '!'
            OR EXISTS (
              SELECT bc.id FROM BlogContent bc
              WHERE bc.blogMeta = b AND LOWER(bc.content) LIKE :keyword ESCAPE '!'
            ))
        """;

    @Query("SELECT b " + VISIBLE_FILTER)
    Page<BlogMeta> searchVisibleBlogs(
            @Param("category") String category,
            @Param("filterTags") boolean filterTags,
            @Param("tags") List<String> tags,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query("SELECT new com.yiyunnetwork.blogbe.dto.BlogSearchDTO(b.id, b.title, b.summary, c.name, b.updateTime) " + VISIBLE_FILTER)
    List<BlogSearchDTO> searchSummaries(
            @Param("category") String category,
            @Param("filterTags") boolean filterTags,
            @Param("tags") List<String> tags,
            @Param("keyword") String keyword,
            Pageable pageable);

    Page<BlogMeta> findByIsDeletedFalse(Pageable pageable);
    
    @Query("SELECT DISTINCT b FROM BlogMeta b JOIN b.blogTags bt JOIN bt.tag t " +
           "WHERE b.isDeleted = false AND t.name IN ?1")
    Page<BlogMeta> findByIsDeletedFalseAndBlogTags_Tag_NameIn(List<String> tagNames, Pageable pageable);
    
    Page<BlogMeta> findByIsDeletedFalseAndCategory_Name(String categoryName, Pageable pageable);
    
    Long countByIsDeletedFalse();
}
