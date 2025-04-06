package com.yiyunnetwork.blogbe.repository;

import com.yiyunnetwork.blogbe.entity.FooterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FooterProfileRepository extends JpaRepository<FooterProfile, Long> {
    FooterProfile findFirstByOrderByIdAsc();
} 