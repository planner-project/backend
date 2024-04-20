package com.planner.travel.domain.user.dto.response;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @Email
    private String email;

    @Pattern(regexp = "^[A-Za-z\\d~!@#$%^&*()_\\-+=\\[\\]{}|\\\\;:'\",.<>?/]{8,20}$")
    // 소문자, 특수문자, 8-20 자리 비밀번호
    private String password;

    @Pattern(regexp = "^[a-zA-Z가-힣\\d]+$")
    @Size(min = 2, max = 12)
    // 특수문자 포함하지 않는 2-12 자 닉네임
    private String nickname;

    private LocalDate birthday;
//    private String phoneNumber;
}
