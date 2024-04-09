package com.planner.travel.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    // ERD cloud 를 참고하여 엔티티를 작성해주세요
    @Id
    @GeneratedValue()
    private Long id;
    @Email
    private String email;

    private String password;
    private String nickname;
    private LocalDate birthday;
    private String phoneNumber;
    private String role;
    private LocalDateTime signupDate;


}

