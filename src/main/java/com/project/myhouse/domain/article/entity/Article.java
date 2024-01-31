package com.project.myhouse.domain.article.entity;

import com.project.myhouse.domain.articleReply.entity.ArticleReply;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article extends BaseEntity {
    @ManyToOne
    private SiteUser user; //작성자 정보

    @Column(length = 50)
    private String title; //게시글 제목

    @Column(columnDefinition = "TEXT")
    private String content;//게시글 내용

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleReply> replyList = new ArrayList<>();

    private String thumbnailImg;
}