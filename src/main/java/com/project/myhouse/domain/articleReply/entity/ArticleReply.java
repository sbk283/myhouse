package com.project.myhouse.domain.articleReply.entity;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ArticleReply extends BaseEntity {
    @ManyToOne
    private SiteUser user;//작성자 정보

    @ManyToOne
    private Article article;

    @Column(columnDefinition = "TEXT")
    private String content;//댓글 내용
}
