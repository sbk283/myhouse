package com.project.myhouse.domain.user.entity;

import com.project.myhouse.global.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.sql.ConnectionBuilder;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class SiteUser extends BaseEntity {
    @Column(unique = true)
    private String userId;

    @Column(unique = true)
    private String nickname;

    @Setter
    private String password;

    @Column(unique = true)
    private String phone;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean checkedAdmin;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    @Setter
    private boolean checkedWithdrawal;
}