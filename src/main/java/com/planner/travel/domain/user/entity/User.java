package com.planner.travel.domain.user.entity;

import jakarta.persistence.*;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    private String password;

    private String nickname;

    private Long userTag;

    private LocalDate birthday;

    private String phoneNumber;

    private LocalDateTime signupDate;

    @Enumerated(value = EnumType.STRING)
    private Role role;

}

