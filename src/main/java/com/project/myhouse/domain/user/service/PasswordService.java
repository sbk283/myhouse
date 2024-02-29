package com.project.myhouse.domain.user.service;

import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void changePassword(String currentPassword, String newPassword, SiteUser user) {
        Optional<SiteUser> optionalUser = userRepository.findByUserId(user.getUserId());

        if (optionalUser.isPresent()) {
            SiteUser existingUser = optionalUser.get();

            // 기존 비밀번호가 일치하는지 확인
            if (passwordEncoder.matches(currentPassword, existingUser.getPassword())) {
                // 새로운 비밀번호로 업데이트
                String hashedNewPassword = passwordEncoder.encode(newPassword);
                SiteUser modifiedUser = existingUser.builder()
                        .password(hashedNewPassword)
                        .modifiedDate(LocalDateTime.now())
                        .build();
                userRepository.save(modifiedUser);
            } else {
                // 기존 비밀번호가 일치하지 않으면 예외 처리 또는 다른 처리 로직 추가
                throw new RuntimeException("Invalid current password");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
