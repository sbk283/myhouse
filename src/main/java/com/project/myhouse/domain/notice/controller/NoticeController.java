package com.project.myhouse.domain.notice.controller;

import com.project.myhouse.domain.notice.entity.Notice;
import com.project.myhouse.domain.notice.form.NoticeCreateForm;
import com.project.myhouse.domain.notice.service.NoticeService;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, Principal principal,@RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw, HttpServletRequest request, SiteUser user) {
        Page<Notice> paging = this.noticeService.getList(page, kw);
//        Member loginUser = this.memberService.getMember(principal.getName());
        model.addAttribute("paging", paging);
//        model.addAttribute("loginUser",loginUser);
        return "notice/notice_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, HttpServletRequest request,
                         Principal principal) {
        Notice notice = this.noticeService.getNotice(id);
        SiteUser loginUser = this.userService.getUser(principal.getName());
        model.addAttribute("notice", notice);
        model.addAttribute("loginUser", loginUser);
        return "notice/notice_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model,
                         HttpServletRequest request,
                         Principal principal) {

        SiteUser loginUser = this.userService.getUser(principal.getName());

        if(loginUser.isCheckedAdmin()) {
            model.addAttribute("noticeCreateForm", new NoticeCreateForm());
            model.addAttribute("request", request);
            return "notice/notice_form";
        }
        return "redirect:notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid NoticeCreateForm noticeCreateForm, BindingResult bindingResult, Principal principal, @RequestParam("thumbnail") MultipartFile thumbnail,
                         HttpServletRequest request, Model model) {

        SiteUser loginUser = userService.getUser(principal.getName());
        if (loginUser.isCheckedAdmin()) {
            if (bindingResult.hasErrors()) {

                model.addAttribute("request", request);
                model.addAttribute("noticeCreateForm", noticeCreateForm);
                return "notice/notice_form";
            }
            if (noticeCreateForm.getTitle() == null) {
                bindingResult.rejectValue("title", "NullTitle", "제목을 입력해주세요.");
            }

            this.noticeService.create(noticeCreateForm.getTitle(), noticeCreateForm.getContent(), loginUser, thumbnail);
            return "redirect:/notice/list";
        }
        return "redirect:/notice/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String noticeModify(NoticeCreateForm noticeCreateForm, @PathVariable("id") Long id, Principal principal, BindingResult bindingResult) {
        Notice notice = this.noticeService.getNotice(id);
        if(!notice.getUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        noticeCreateForm.setTitle(notice.getTitle());
        noticeCreateForm.setContent(notice.getContent());
        return "notice/notice_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String noticeModify(@Valid NoticeCreateForm noticeCreateForm, BindingResult bindingResult,
                               Principal principal, @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            return "notice/notice_form";
        }
        Notice notice = this.noticeService.getNotice(id);
        if(!notice.getUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.noticeService.modify(notice, noticeCreateForm.getTitle(), noticeCreateForm.getContent());
        return String.format("redirect:/notice/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String noticeDelete(Principal principal, @PathVariable("id") Long id) {
        Notice notice = this.noticeService.getNotice(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if(!siteUser.isCheckedAdmin()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.noticeService.delete(notice);
        return "redirect:/";
    }
}
