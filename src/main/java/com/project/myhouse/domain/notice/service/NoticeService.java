package com.project.myhouse.domain.notice.service;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.notice.entity.Notice;
import com.project.myhouse.domain.notice.form.NoticeCreateForm;
import com.project.myhouse.domain.notice.repository.NoticeRepository;
import com.project.myhouse.domain.user.entity.SiteUser;
import jakarta.persistence.criteria.*;
import jakarta.validation.Valid;
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
public class NoticeService {
    private final NoticeRepository noticeRepository;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    public Page<Notice> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Notice> spec = search(kw);
        return this.noticeRepository.findAll(spec, pageable);
    }

    public Notice getNotice(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        } else {
            throw new RuntimeException("error");
        }
    }

    public void create(String title, String content, SiteUser user, MultipartFile thumbnail) {
        String thumbnailRelPath = "notice/" + UUID.randomUUID().toString() + ".jpg";
        File thumbnailFile = new File(fileDirPath + "/" + thumbnailRelPath);

        try {
            thumbnail.transferTo(thumbnailFile);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

        Notice notice = Notice.builder()
                .title(title)
                .content(content)
                .user(user)
                .createDate(LocalDateTime.now())
                .thumbnailImg(thumbnailRelPath)
                .build();
        this.noticeRepository.save(notice);
    }

    public void modify(Notice notice, String title, String content) {
        Notice modiftNotice = Notice.builder()
                .id(notice.getId())
                .createDate(notice.getCreateDate())
                .modifiedDate(LocalDateTime.now())
                .title(title)
                .content(content)
                .thumbnailImg(notice.getThumbnailImg())
                .user(notice.getUser())
                .build();
        this.noticeRepository.save(modiftNotice);
    }

    public void delete(Notice notice) {
        this.noticeRepository.delete(notice);
    }

    private Specification<Notice> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Notice> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Notice, SiteUser> u1 = q.join("user", JoinType.LEFT);
                return cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("nickname"), "%" + kw + "%"));    // 질문 작성자
            }
        };
    }
}
