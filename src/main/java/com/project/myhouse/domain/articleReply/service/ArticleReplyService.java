package com.project.myhouse.domain.articleReply.service;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.articleReply.entity.ArticleReply;
import com.project.myhouse.domain.articleReply.repository.ArticleReplyRepository;
import com.project.myhouse.domain.user.entity.SiteUser;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleReplyService {
    private final ArticleReplyRepository articleReplyRepository;

    @Transactional
    public ArticleReply create(Article article, String content, SiteUser user) {

        ArticleReply createArticleReply = ArticleReply.builder()
                .content(content)
                .article(article)
                .user(user)
                .build();
        this.articleReplyRepository.save(createArticleReply);
        return createArticleReply;
    }

    public ArticleReply getArticleReply(Long id) {
        return this.articleReplyRepository.findById(id)
                .orElseThrow(() -> new ExecutionException("답변을 찾을 수 없습니다."));
    }

    public void modify(ArticleReply articleReply, String content) {
        ArticleReply modifyArticleReply = articleReply.toBuilder()
                .content(content)
                .build();
        this.articleReplyRepository.save(modifyArticleReply);
    }
    public void delete(ArticleReply articleReply) {
        this.articleReplyRepository.delete(articleReply);
    }
}

