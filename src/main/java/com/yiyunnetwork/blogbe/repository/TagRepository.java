package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    boolean existsByName(String name);
    List<Tag> findAllByOrderByArticleCountDesc();

    @Modifying
    @Query("UPDATE Tag t SET t.articleCount = t.articleCount + 1 WHERE t.id = ?1")
    void incrementArticleCount(Long id);

    @Modifying
    @Query("UPDATE Tag t SET t.articleCount = t.articleCount - 1 WHERE t.id = ?1 AND t.articleCount > 0")
    void decrementArticleCount(Long id);
} 