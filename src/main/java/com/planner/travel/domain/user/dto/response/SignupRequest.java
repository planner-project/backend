package com.planner.travel.domain.user.dto.response;

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
    private String email;
    private String nickname;
    private String password;
    private LocalDate birthday; // birthday 필드 추가
    private Long userTag; // userTag 필드를 Long 타입으로 변경
    private String phoneNumber; // phoneNumber 필드 추가
}
