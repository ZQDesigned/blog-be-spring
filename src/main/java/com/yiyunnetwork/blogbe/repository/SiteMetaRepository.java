package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.SiteMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteMetaRepository extends JpaRepository<SiteMeta, Long> {
    SiteMeta findFirstByOrderByIdAsc();
} 