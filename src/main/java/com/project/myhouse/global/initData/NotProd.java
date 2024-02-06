package com.project.myhouse.global.initData;

import com.project.myhouse.domain.article.service.ArticleService;
import com.project.myhouse.domain.notice.service.NoticeService;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class NotProd {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner init(UserService userService, NoticeService noticeService, ArticleService articleService) {
        return args -> {
//            userService.adminCreate("admin1", "관리자1", "admin", "01000000001",true);
//            userService.adminCreate("admin2", "관리자2", "admin", "01000000002",true);
//            userService.create("user1", "유저1","123123","01011111111");
//            userService.create("user2", "유저2","123123","01022222222");
//            userService.create("user3", "유저3","123123","01033333333");
//            userService.create("user4", "유저4","123123","01044444444");
//            userService.create("user5", "유저5","123123","01055555555");
//            userService.create("user6", "유저6","123123","01066666666");
//            userService.create("user7", "유저7","123123","01077777777");
//            userService.create("user8", "유저8","123123","01088888888");
//            userService.create("user9", "유저9","123123","01099999999");
//            userService.create("user10", "유저10","123123","01010101010");

        };
    }
}