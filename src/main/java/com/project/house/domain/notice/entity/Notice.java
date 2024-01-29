package com.project.myhouse.domain.notice.entity;

import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntity {
    @ManyToOne
    private SiteUser user;

    @Column(columnDefinition = "TEXT")
    private String title;

    private String content;

    private LocalDateTime modifiedDate;
}