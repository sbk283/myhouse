package com.project.myhouse.domain.calendar.repository;

import com.project.myhouse.domain.calendar.entity.Calendar;
import com.project.myhouse.domain.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Long> {
    List<Calendar> findByUser(SiteUser siteUser);

    Page<Calendar> findAll(Specification<Calendar> spec, Pageable pageable);
}
