package com.project.myhouse.domain.calendar.service;

import com.project.myhouse.domain.calendar.entity.Calendar;
import com.project.myhouse.domain.calendar.form.CalendarForm;
import com.project.myhouse.domain.calendar.repository.CalendarRepository;
import com.project.myhouse.domain.user.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;
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
}
