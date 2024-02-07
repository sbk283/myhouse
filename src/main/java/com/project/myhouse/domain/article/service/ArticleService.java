package com.project.myhouse.domain.article.service;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.article.repository.ArticleRepository;
import com.project.myhouse.domain.user.entity.SiteUser;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    public Page<Article> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Article> spec = search(kw);
        return this.articleRepository.findAll(spec, pageable);
    }

    public Article getArticle(Long id) {
        Optional<Article> article = this.articleRepository.findById(id);
        if (article.isPresent()) {
            return article.get();
        } else {
            throw new RuntimeException("error");
        }
    }

    public void create(String title, String content, SiteUser user, MultipartFile thumbnail) {
        String thumbnailRelPath = null;

        if (!thumbnail.isEmpty()) {
            thumbnailRelPath = "article/" + UUID.randomUUID().toString() + ".jpg";
            File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

            try {
                thumbnail.transferTo(thumbnailFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Article article = Article.builder()
                .title(title)
                .content(content)
                .user(user)
                .createDate(LocalDateTime.now())
                .thumbnailImg(thumbnailRelPath)
                .build();
        this.articleRepository.save(article);
    }

    @Transactional
    public void modify(Article article, String title, String content, MultipartFile newThumbnail) {
        String thumbnailRelPath = article.getThumbnailImg();

        // 새로운 이미지가 업로드되었을 경우
        if (newThumbnail != null && !newThumbnail.isEmpty()) {
            // 기존 이미지 삭제
            deleteImageFile(thumbnailRelPath);

            // 새로운 이미지 업로드
            thumbnailRelPath = uploadNewThumbnail(newThumbnail);
        }

        // 게시글 업데이트
        article = Article.builder()
                .id(article.getId())
                .title(title)
                .content(content)
                .thumbnailImg(thumbnailRelPath)
                .modifiedDate(LocalDateTime.now())
                .createDate(article.getCreateDate())
                .user(article.getUser())
                .build();

        articleRepository.save(article);
    }

    public void deleteImageFile(String imagePath) {
        if (imagePath != null) {
            File imageFile = new File(fileDirPath + "/" + imagePath);

            if (imageFile.exists() && !imageFile.delete()) {
                throw new RuntimeException("Failed to delete image file: " + imagePath);
            }
        }
    }

    private String uploadNewThumbnail(MultipartFile newThumbnail) {
        String thumbnailRelPath = "article/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        try {
            newThumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return thumbnailRelPath;
    }

    public void delete(Article article) {
        this.articleRepository.delete(article);
    }

    private Specification<Article> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Article> articleRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Article, SiteUser> userJoin = articleRoot.join("user", JoinType.LEFT);

                return cb.or(
                        cb.like(articleRoot.get("title"), "%" + kw + "%"), // 제목
                        cb.like(articleRoot.get("content"), "%" + kw + "%"), // 내용
                        cb.like(userJoin.get("nickname"), "%" + kw + "%") // 질문 작성자
                );
            }
        };
    }
}
