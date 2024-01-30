package com.project.myhouse.domain.calendar.entity;

import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Calendar extends BaseEntity {
    @ManyToOne
    private SiteUser user;

    private String title;

    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime modifiedDate;

}
