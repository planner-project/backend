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
@RequestMapping(value = "api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final SignupService signupService;
    private final LoginService loginService;

    // 나중에 지워주세요!
    @GetMapping(value = "/signup")
    public String test() {
        return "테스트";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        try {
            signupService.signup(signupRequest);
            return ResponseEntity.ok().body("회원가입이 성공적으로 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            loginService.login(loginRequest, response);
            return ResponseEntity.ok().body("로그인에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("로그인 실패: " + e.getMessage());
        }
    }


}
