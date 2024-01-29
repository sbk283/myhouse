package com.project.myhouse.domain.user.controller;

import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.form.UserCreateForm;
import com.project.myhouse.domain.user.form.UserMypageForm;
import com.project.myhouse.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup_form";
        }

        validatePassword(userCreateForm.getPassword1(), userCreateForm.getPassword2(), bindingResult);

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getNickname(), userCreateForm.getPassword1(), userCreateForm.getPhone());
        } catch (DataIntegrityViolationException e) {
            handleUserCreationError(bindingResult);
        } catch (Exception e) {
            handleUnexpectedError(bindingResult, e);
        }

        return "redirect:/";
    }

    private void validatePassword(String password1, String password2, BindingResult bindingResult) {
        if (!password1.equals(password2)) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
        }
    }

    private void handleUserCreationError(BindingResult bindingResult) {
        bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
    }

    private void handleUnexpectedError(BindingResult bindingResult, Exception e) {
        bindingResult.reject("signupFailed", "회원가입 중에 오류가 발생했습니다.");
    }

    @GetMapping("/login")
    public String login() {
        return "user/login_form";
    }

    @GetMapping("/mypage")
    public String mypage(UserMypageForm userMypageForm, Principal principal, Model model) {
        String username = principal.getName();
        SiteUser siteUser = this.userService.getUser(username);
        if (!siteUser.getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        model.addAttribute("username", siteUser.getUserId());
        model.addAttribute("nickname", siteUser.getNickname());
        model.addAttribute("phone", siteUser.getPhone());
        return "user/mypage_form";
    }

    @GetMapping("/showmypage")
    public String showmypage(UserMypageForm userMypageForm, Principal principal, Model model) {
        String username = principal.getName();
        SiteUser siteUser = this.userService.getUser(username);
        if (!siteUser.getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        model.addAttribute("username", siteUser.getUserId());
        model.addAttribute("nickname", siteUser.getNickname());
        model.addAttribute("phone", siteUser.getPhone());
        return "user/mypage_detail";
    }
}