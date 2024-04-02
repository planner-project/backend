package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.request.LoginRequest;
import com.planner.travel.global.jwt.token.TokenGenerator;
import com.planner.travel.global.jwt.token.TokenType;
import com.planner.travel.global.util.CookieUtil;
import com.planner.travel.global.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    // 아래에 유저 repository 를 주입해주세요.
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;

    public void login(LoginRequest loginRequest, HttpServletResponse response) {
        /*
        1. 리퀘스트로 받은 값 중, 이메일을 이용하여 유저를 찾아주세요. 유저의 형태는 Optional 이며,
        유저가 존재하지 않을 시 entitynotfoundexception 을 던져주세요.
        2. 위에서 찾은 유저를 이용하여, 유저의 인덱스를 찾아주세요. 그리고 이를 사용하여 accessToken 과 refreshToken 을 만들어주세요.
            - TokenGenerator.java 를 참고해주세요.
        3. 아래 private 으로 작성한 메서드들을 순차적으로 불러오고, 알맞은 argument 를 넣어주세요.
         */
    }

    private void authenticateUser(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
    }

    private void addAccessTokenToHeader(Long userId, HttpServletResponse response) {
        String accessToken = tokenGenerator.generateToken(TokenType.ACCESS, String.valueOf(userId));
        response.setHeader("Authorization", accessToken);
    }

    private void addRefreshTokenToCookieAndRedis(Long userId, HttpServletResponse response) {
        String refreshToken = tokenGenerator.generateToken(TokenType.REFRESH, String.valueOf(userId));

        cookieUtil.setCookie("refreshToken", refreshToken, response);
        redisUtil.setData(String.valueOf(userId), refreshToken);
    }
}
