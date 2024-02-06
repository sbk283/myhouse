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
import java.time.format.DateTimeParseException;
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
        Specification<Calendar> spec = searchSpecification(kw);
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

    private Specification<Calendar> searchSpecification(String kw) {
        return new Specification<Calendar>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Calendar> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);
                Join<Calendar, SiteUser> u1 = q.join("user", JoinType.LEFT);

                Predicate titlePredicate = cb.like(q.get("title"), "%" + kw + "%");
                Predicate contentPredicate = cb.like(q.get("content"), "%" + kw + "%");
                Predicate nicknamePredicate = cb.like(u1.get("nickname"), "%" + kw + "%");

                try {
                    // 검색어가 날짜 형식인 경우
                    LocalDate searchDate = LocalDate.parse(kw);

                    // startDate 또는 endDate에 대한 검색 조건 추가
                    Predicate startDatePredicate = cb.equal(q.get("startDate"), searchDate);
                    Predicate endDatePredicate = cb.equal(q.get("endDate"), searchDate);

                    // 제목, 내용, 닉네임, 시작일, 종료일 중 하나라도 매치되면 반환
                    return cb.or(titlePredicate, contentPredicate, nicknamePredicate, startDatePredicate, endDatePredicate);
                } catch (DateTimeParseException e) {
                    // 검색어가 날짜 형식이 아니면 startDate, endDate 검색 조건을 제외하고 나머지만 고려
                    return cb.or(titlePredicate, contentPredicate, nicknamePredicate);
                }
            }
        };
    }

}
