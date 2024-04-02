package com.planner.travel.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    // 나중에 지워주세요!
    @GetMapping(value = "/signup")
    public String test() {
        return "테스트";
    }
}
