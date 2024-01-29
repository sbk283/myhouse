package com.project.myhouse.domain.user.repository;

import com.project.myhouse.domain.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    Optional<SiteUser> findByUserId(String username);
    Page<SiteUser> findAll(Specification<SiteUser> spec, Pageable pageable);
}