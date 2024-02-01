package com.project.myhouse.domain.user.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminCreateForm {

    @NotEmpty(message = "사용자ID는 필수 입력 항목입니다.")
    @Size(min = 5, max = 15, message = "사용자ID는 5자 이상 15자 이하로 입력해주세요.")
    private String userId;

    @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 2, max = 15, message = "사용자명은 2자 이상 15자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String Password1;

    @NotEmpty(message = "비밀번호 재확인은 필수입니다.")
    private String Password2;

    @NotEmpty(message = "전화번호는 필수 입력 항목입니다.")
    @Size(min = 11, max = 11, message = " 전화번호는 - 기호를 빼고 입력해주세요.")
    private String phone;

    @NotEmpty(message = "관리자 확인 패스워드 4자리를 입력해주세요.")
    @Size(min = 4, max = 4)
    private String adminPassword;
}