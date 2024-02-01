package com.project.myhouse.domain.user.service;

import com.project.myhouse.domain.user.entity.SiteUser;

import com.project.myhouse.domain.user.repository.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SiteUser create(String username, String nickname, String password, String phone) {
        SiteUser user = SiteUser.builder()
                .userId(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .createDate(LocalDateTime.now())
                .build();
        this.userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username) {
        return this.userRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public SiteUser whenSocialLogin(String providerTypeCode, String username, String nickname) {
        Optional<SiteUser> opUser = userRepository.findByUserId(username);

        if (opUser.isPresent()) return opUser.get();

        return create(username, nickname,"", "");
    }

    // findByUserId 메소드는 이미 userRepository에서 제공되므로, 중복 코드 제거
    public Optional<SiteUser> findByUserId(String username) {
        return userRepository.findByUserId(username);
    }

    public List<SiteUser> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional
    public void modify(SiteUser user, String nickname, String phone) {
        this.userRepository.findByUserId(user.getUserId())
                .ifPresent(updateUser -> {
                    SiteUser modifyUser = SiteUser.builder()
                            .id(updateUser.getId())
                            .userId(user.getUserId())
                            .nickname(nickname)
                            .password(user.getPassword())
                            .phone(phone)
                            .createDate(user.getCreateDate())
                            .modifiedDate(LocalDateTime.now())
                            .checkedAdmin(user.isCheckedAdmin())
                            .checkedWithdrawal(user.isCheckedWithdrawal())
                            .build();
                    this.userRepository.save(modifyUser);
                });
    }

    public Page<SiteUser> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<SiteUser> spec = search(kw);
        return this.userRepository.findAll(spec, pageable);
    }

    private Specification<SiteUser> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<SiteUser> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                return cb.or(
                        cb.like(q.get("nickname"), "%" + kw + "%"),
                        cb.like(q.get("userId"), "%" + kw + "%")
                );
            };
        };
    }
    public void updateCheckedUserStatus(String userId) {
        Optional<SiteUser> optionalUser = userRepository.findByUserId(userId);
        SiteUser deleteuser = optionalUser.get();
        deleteuser.setPassword(null);
        deleteuser.setCheckedWithdrawal(true);
        this.userRepository.save(deleteuser);
    }

    public Optional<SiteUser> findById (Long id) {
        return this.userRepository.findById(id);
    }

    @Transactional
    public SiteUser adminCreate(String username, String nickname, String password, String phone, String adminpw) {
        SiteUser user = SiteUser.builder()
                .userId(username)
                .nickname(nickname)
                .password(passwordEncoder.encode(password))
                .phone(phone)
                .createDate(LocalDateTime.now())
                .checkedAdminPassword(adminpw)
                .build();
        this.userRepository.save(user);
        return user;
    }
}
