package com.project.myhouse.domain.article.controller;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.article.form.ArticleForm;
import com.project.myhouse.domain.article.service.ArticleService;
import com.project.myhouse.domain.articleReply.form.ArticleReplyForm;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.service.UserService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Article> paging = this.articleService.getList(page, kw);
        model.addAttribute("paging", paging);
        return "article/article_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Long id, ArticleReplyForm articleReplyForm) {
        Article article = this.articleService.getArticle(id);
        model.addAttribute("article", article);
        return "article/article_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String articleCreate(ArticleForm articleForm) {
        return "article/article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String articleCreate(@Valid ArticleForm articleForm, BindingResult bindingResult, Principal principal, @RequestParam("thumbnail") MultipartFile thumbnail) {
        if (bindingResult.hasErrors()) {
            return "article/article_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.articleService.create(articleForm.getTitle(), articleForm.getContent(), siteUser, thumbnail);
        return "redirect:/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String articleModify(ArticleForm articleForm, @PathVariable("id") Long id, Principal principal) {
        Article article = this.articleService.getArticle(id);
        if(!article.getUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());
        return "article/article_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String articleModify(@Valid ArticleForm articleForm, BindingResult bindingResult,
                                  Principal principal, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "article/article_form";
        }
        Article article = this.articleService.getArticle(id);
        if (!article.getUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.articleService.modify(article, articleForm.getTitle(), articleForm.getContent());
        return String.format("redirect:/article/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String articleDelete(Principal principal, @PathVariable("id") Long id) {
        Article article = this.articleService.getArticle(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        // 사용자가 게시물의 작성자이거나 관리자인지 확인
        if (!article.getUser().getUserId().equals(siteUser.getUserId()) && !siteUser.isCheckedAdmin()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        this.articleService.delete(article);
        return "redirect:/article/list";
    }
}