package com.project.myhouse.domain.user.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserMypageForm {
    @NotEmpty(message = "닉네임은 필수 입력 항목입니다.")
    @Size(min = 3, max = 15, message = "사용자명은 3자 이상 15자 이하로 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "전화번호는 필수 입력 항목입니다.")
    @Size(min = 11, max = 11, message = " 전화번호는 - 기호를 빼고 입력해주세요.")
    private String phone;
}
