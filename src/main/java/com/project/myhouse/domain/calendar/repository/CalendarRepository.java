package com.project.myhouse.domain.calendar.repository;

import com.project.myhouse.domain.calendar.entity.Calendar;
import com.project.myhouse.domain.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    List<Calendar> findByUser(SiteUser siteUser);
}
