package com.project.myhouse.domain.user.controller;

import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.form.AdminCreateForm;
import com.project.myhouse.domain.user.form.UserCreateForm;
import com.project.myhouse.domain.user.form.UserMypageForm;
import com.project.myhouse.domain.user.form.UserPasswordForm;
import com.project.myhouse.domain.user.service.PasswordService;
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
    private final PasswordService passwordService;
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
            userService.create(userCreateForm.getUserId(), userCreateForm.getNickname(), userCreateForm.getPassword1(), userCreateForm.getPhone());
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/showmypage")
    public String showmypage(UserMypageForm userMypageForm, UserCreateForm userCreateForm, Principal principal) {
        String username = principal.getName();
        SiteUser siteUser = this.userService.getUser(username);
        if (!siteUser.getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        userCreateForm.setUserId(siteUser.getUserId());
        userMypageForm.setNickname(siteUser.getNickname());
        userMypageForm.setPhone(siteUser.getPhone());

        return "user/mypage_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mypage")
    public String mypage(UserMypageForm userMypageForm, UserCreateForm userCreateForm, Principal principal) {
        String username = principal.getName();
        SiteUser siteUser = this.userService.getUser(username);
        if (!siteUser.getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        userCreateForm.setUserId(siteUser.getUserId());
        userMypageForm.setNickname(siteUser.getNickname());
        userMypageForm.setPhone(siteUser.getPhone());
        return "user/mypage_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/mypage")
    public String userModify(@Valid UserMypageForm userMypageForm,
                             BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user/mypage_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if (!siteUser.getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        try {
            userService.modify(siteUser, userMypageForm.getNickname(), userMypageForm.getPhone());
            return "redirect:/user/showmypage";
        } catch (Exception e) {
            e.printStackTrace();
            return "user/mypage_form";
        }
    }

//    @GetMapping("/admin/signup")
//    public String adminSignup(AdminCreateForm adminCreateForm) {
//        return "user/admin_signup_form";
//    }
//
//    @PostMapping("/admin/signup")
//    public String adminSignup(@Valid AdminCreateForm adminCreateForm, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "user/admin_signup_form";
//        }
//
////        validatePassword(adminCreateForm.getPassword1(), adminCreateForm.getPassword2(), bindingResult);
//
//        try {
//            userService.adminCreate(adminCreateForm.getUserId(), adminCreateForm.getNickname(), adminCreateForm.getPassword1(), adminCreateForm.getPhone(), adminCreateForm.getAdminPassword());
//        } catch (DataIntegrityViolationException e) {
//            handleUserCreationError(bindingResult);
//        } catch (Exception e) {
//            handleUnexpectedError(bindingResult, e);
//        }
//
//        return "redirect:/";
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/changePw")
    public String changePw(UserPasswordForm userPasswordForm) {
        return "user/change_password";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/changePw")
    public String changePassword(@Valid UserPasswordForm userPasswordForm, BindingResult bindingResult, Principal principal, Model model) {
        String username = principal.getName();
        SiteUser siteUser = userService.getUser(username);

        // 기존 비밀번호 확인
        if (!passwordEncoder.matches(userPasswordForm.getCurrentPassword(), siteUser.getPassword())) {
            bindingResult.rejectValue("currentPassword", "passwordInCorrect", "기존 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (!userPasswordForm.getNewPassword().equals(userPasswordForm.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "passwordInCorrect", "새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "user/change_password"; // 에러가 있으면 변경 페이지로 다시 이동
        }

        // 비밀번호 변경
        try {
            userService.changePw(siteUser, userPasswordForm.getNewPassword());
            return "redirect:/user/showmypage"; // 성공 페이지로 리다이렉트 또는 성공 메시지를 표시하는 뷰로 이동
        } catch (RuntimeException e) {
            model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
            return "redirect:/user/changePw"; // 에러 페이지로 리다이렉트 또는 에러 메시지를 표시하는 뷰로 이동
        }
    }
}