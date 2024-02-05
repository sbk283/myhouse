package com.project.myhouse.domain.user.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class UserPasswordForm  {
    @NotEmpty(message = "기존 패스워드를 입력해주세요.")
    private String currentPassword;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String newPassword;

    @NotEmpty(message = "비밀번호 재확인은 필수입니다.")
    private String confirmPassword;
}
