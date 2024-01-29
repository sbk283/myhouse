package com.project.myhouse.domain.article.repository;

import com.project.myhouse.domain.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pageable);
    Page<Article> findAll(Specification<Article> spec, Pageable pageable);
}
