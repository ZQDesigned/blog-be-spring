package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);
    List<Category> findAllByOrderByArticleCountDesc();

    @Modifying
    @Query("UPDATE Category c SET c.articleCount = c.articleCount + 1 WHERE c.id = ?1")
    void incrementArticleCount(Long id);

    @Modifying
    @Query("UPDATE Category c SET c.articleCount = c.articleCount - 1 WHERE c.id = ?1 AND c.articleCount > 0")
    void decrementArticleCount(Long id);
} 