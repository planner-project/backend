package com.planner.travel.domain.user.controller;

import com.planner.travel.domain.user.dto.request.LoginRequest;
import com.planner.travel.domain.user.dto.response.SignupRequest;
import com.planner.travel.domain.user.service.LoginService;
import com.planner.travel.domain.user.service.SignupService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final SignupService signupService;
    private final LoginService loginService;

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        signupService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        loginService.login(loginRequest, response);
        return ResponseEntity.ok().build();
    }
}
