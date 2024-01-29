package com.project.myhouse.domain.user.controller;

import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/user")
@Secured("ROLE_ADMIN")
public class AdminController {
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<SiteUser> paging = this.userService.getList(page, kw);
        SiteUser loginUser = this.userService.getUser(principal.getName());

        if(loginUser.isCheckedAdmin()) {
            model.addAttribute("request", request);
            model.addAttribute("paging",paging);
            return "user/admin_page";
        }
        return "redirect:notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String deleteUser(Principal principal, @RequestParam("userId") String userId) {
        SiteUser loginUser = this.userService.getUser(principal.getName());
        if(loginUser.isCheckedAdmin()) {
            userService.updateCheckedUserStatus(userId);
        }
        return "redirect:/admin/user/list";
    }
}

