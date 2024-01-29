package com.project.myhouse.domain.user.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPasswordForm {
    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String Password1;

    @NotEmpty(message = "비밀번호 재확인은 필수입니다.")
    private String Password2;
}
