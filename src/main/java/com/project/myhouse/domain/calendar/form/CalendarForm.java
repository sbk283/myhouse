package com.project.myhouse.domain.calendar.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@ToString
public class CalendarForm {
    @NotEmpty(message = "일정을 입력해주세요")
    private String title;
    @NotEmpty(message = "")
    private String content;
    @NotEmpty(message = "시작일을 선택해주세요")
    private String start;
    @NotEmpty(message = "종료일을 선택해주세요")
    private String end;
}
