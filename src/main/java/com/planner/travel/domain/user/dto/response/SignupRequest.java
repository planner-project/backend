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
    private LocalDate birthday;
    private String phoneNumber;
}
