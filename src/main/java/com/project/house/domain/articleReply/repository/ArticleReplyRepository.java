package com.project.myhouse.domain.articleReply.repository;

import com.project.myhouse.domain.articleReply.entity.ArticleReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleReplyRepository extends JpaRepository<ArticleReply, Long> {
}

