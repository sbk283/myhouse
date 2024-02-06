package com.project.myhouse.domain.calendar.service;

import com.project.myhouse.domain.calendar.entity.Calendar;
import com.project.myhouse.domain.calendar.form.CalendarForm;
import com.project.myhouse.domain.calendar.repository.CalendarRepository;
import com.project.myhouse.domain.notice.entity.Notice;
import com.project.myhouse.domain.user.entity.SiteUser;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public Page<Calendar> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Calendar> spec = search(kw);
        return this.calendarRepository.findAll(spec, pageable);
    }
    public List<Calendar> findAll() {
        return this.calendarRepository.findAll();
    }

    public void save(CalendarForm calendarForm, SiteUser user) {
        Calendar calendar = Calendar.builder()
                .user(user)
                .title(calendarForm.getTitle())
                .content(calendarForm.getContent())
                .createDate(LocalDateTime.now())
                .startDate(LocalDate.parse(calendarForm.getStart()))
                .endDate(LocalDate.parse(calendarForm.getEnd()))
                .build();
        this.calendarRepository.save(calendar);
    }

    public List<Calendar> findByUser(SiteUser siteUser) {
        return this.calendarRepository.findByUser(siteUser);
    }

    public Calendar getCalendar(Long id) {
        Optional<Calendar> os = this.calendarRepository.findById(id);
        return os.get();
    }

    public void delete(Calendar calendar) {
        this.calendarRepository.delete(calendar);
    }

    public void modify(Calendar calendar ,CalendarForm calendarForm, Long id) {
        Calendar beforeCalendar = this.calendarRepository.findById(id).get();

        Calendar modifyCalendar = Calendar.builder()
                .id(beforeCalendar.getId())
                .user(beforeCalendar.getUser())
                .title(calendarForm.getTitle())
                .content(calendarForm.getContent())
                .startDate(LocalDate.parse(calendarForm.getStart()))
                .endDate(LocalDate.parse(calendarForm.getEnd()))
                .createDate(calendar.getCreateDate())
                .build();
        this.calendarRepository.save(modifyCalendar);
    }

    private Specification<Calendar> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Calendar> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Notice, SiteUser> u1 = q.join("user", JoinType.LEFT);
                return cb.or(cb.like(q.get("title"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("nickname"), "%" + kw + "%"));    // 질문 작성자
            }
        };
    }
}
