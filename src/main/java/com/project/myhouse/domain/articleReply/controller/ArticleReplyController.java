package com.project.myhouse.domain.articleReply.controller;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.article.service.ArticleService;
import com.project.myhouse.domain.articleReply.entity.ArticleReply;
import com.project.myhouse.domain.articleReply.form.ArticleReplyForm;
import com.project.myhouse.domain.articleReply.service.ArticleReplyService;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/articleReply")
@RequiredArgsConstructor
@Controller
public class ArticleReplyController {
    private final ArticleService articleService;
    private final ArticleReplyService articleReplyService;
    private final UserService userService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(Model model, @PathVariable("id") Long id,
                         @Valid ArticleReplyForm articleReplyForm, BindingResult bindingResult, Principal principal) {

        //답변 부모 질문객체를 받아온다.
        Article article = this.articleService.getArticle(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "article_detail";
        }

        ArticleReply articleReply = this.articleReplyService.create(article, articleReplyForm.getContent(), siteUser);

        return "redirect:/article/detail/%d#articleReply_%s".formatted(id, articleReply.getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String articleReplyModify(Model model, @PathVariable("id") Long id, Principal principal) {
        ArticleReply articleReply = this.articleReplyService.getArticleReply(id);
        if (!articleReply.getUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        ArticleReplyForm articleReplyForm = new ArticleReplyForm();
        articleReplyForm.setContent(articleReply.getContent());

        model.addAttribute("articleReplyForm", articleReplyForm); // 수정 폼에 초기값으로 내용을 설정
        model.addAttribute("articleReplyId", id); // 수정 대상의 ID를 모델에 추가

        return "article_reply_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String articleReplyModify(@Valid ArticleReplyForm articleReplyForm, BindingResult bindingResult,
                                       @PathVariable("id") Long id, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "articleReply_form";
        }

        ArticleReply articleReply = this.articleReplyService.getArticleReply(id);
        if (!articleReply.getUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.articleReplyService.modify(articleReply, articleReplyForm.getContent());
        return String.format("redirect:/article/detail/%s", articleReply.getArticle().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String articleReplyDelete(Principal principal, @PathVariable("id") Long id) {
        ArticleReply articleReply = this.articleReplyService.getArticleReply(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());

        if (!articleReply.getUser().getUserId().equals(siteUser.getUserId()) && !siteUser.isCheckedAdmin()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.articleReplyService.delete(articleReply);
        return String.format("redirect:/article/detail/%s", articleReply.getArticle().getId());
    }
}
