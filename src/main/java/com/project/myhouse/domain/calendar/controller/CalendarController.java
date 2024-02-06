package com.project.myhouse.domain.calendar.controller;

import com.project.myhouse.domain.article.entity.Article;
import com.project.myhouse.domain.calendar.entity.Calendar;
import com.project.myhouse.domain.calendar.form.CalendarForm;
import com.project.myhouse.domain.calendar.service.CalendarService;
import com.project.myhouse.domain.user.entity.SiteUser;
import com.project.myhouse.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = {"/list"})
    public String list(Model model, HttpServletRequest request,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        if (request != null) {
            // calendar logic
            List<Calendar> calendarList = this.calendarService.findAll();
            List<CalendarForm> calendarFormList = new ArrayList<>();
            for (Calendar calendar : calendarList) {
                CalendarForm sc1 = new CalendarForm();
                sc1.setTitle(calendar.getTitle());
                sc1.setStart(calendar.getStartDate().toString());
                sc1.setEnd(calendar.getEndDate().plusDays(1).toString());
                calendarFormList.add(sc1);
            }

            model.addAttribute("calendarList", calendarFormList);
            model.addAttribute("request", request);
            return "calendar/calendar_list";
        } else {
            Page<Calendar> paging = this.calendarService.getList(page, kw);
            model.addAttribute("paging", paging);
            return "article/article_list";
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model,
                         HttpServletRequest request) {
        model.addAttribute("calendarForm", new CalendarForm());
        model.addAttribute("request", request);
        return "calendar/calendar_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@Valid CalendarForm calendarForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "calendar/calendar_form";
        }
        SiteUser siteUser = userService.getUser(principal.getName());
        this.calendarService.save(calendarForm, siteUser);
        return "redirect:/calendar/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/manage")
    public String myCalendar(Model model, Principal principal,
                             HttpServletRequest request) {
        SiteUser siteUser = userService.getUser(principal.getName());
        List<Calendar> calendarList;
//        if (siteUser.isCheckedAdmin()) {
//            calendarList = this.calendarService.findAll();
//        } else {
//            calendarList = this.calendarService.findByUser(siteUser);
//        }
        calendarList = this.calendarService.findAll();
        model.addAttribute("myCalendar", calendarList);
        model.addAttribute("request", request);
        return "calendar/my_calendar";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(Principal principal, @PathVariable("id") Long id) {
        SiteUser loginUser = this.userService.getUser(principal.getName());
        Calendar calendar = this.calendarService.getCalendar(id);
        if (loginUser.isCheckedAdmin() || calendar.getUser().getUserId().equals(principal.getName())) {
            this.calendarService.delete(calendar);
            return "redirect:/calendar/manage";
        } else {
            return "redirect:/calendar/list";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(Principal principal,
                         @PathVariable("id") Long id,
                         Model model,
                         HttpServletRequest request) {

        Calendar calendar = this.calendarService.getCalendar(id);
        SiteUser loginUser = this.userService.getUser(principal.getName());
        if (loginUser.isCheckedAdmin() || calendar.getUser().getUserId().equals(principal.getName())) {

            CalendarForm modifyCalendarForm = new CalendarForm();
            modifyCalendarForm.setTitle(calendar.getTitle());
            modifyCalendarForm.setContent(calendar.getContent());
            modifyCalendarForm.setEnd(String.valueOf(calendar.getEndDate()));
            modifyCalendarForm.setStart(String.valueOf(calendar.getStartDate()));

            model.addAttribute("calendarForm", modifyCalendarForm);
            model.addAttribute("request", request);
            return "calendar/calendar_form";
        } else {
            return "redirect:/calendar/list";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@Valid CalendarForm calendarForm, Calendar calendar,
                         BindingResult bindingResult,
                         Principal principal,
                         @PathVariable("id") Long id) {
        this.calendarService.modify(calendar, calendarForm, id);
        return "redirect:/calendar/manage";
    }
}