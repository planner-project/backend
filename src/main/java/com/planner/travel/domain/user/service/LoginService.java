package com.planner.travel.domain.user.service;

import com.planner.travel.domain.user.dto.request.LoginRequest;
import com.planner.travel.domain.user.entity.User;
import com.planner.travel.domain.user.repository.UserRepository;
import com.planner.travel.global.jwt.token.TokenGenerator;
import com.planner.travel.global.jwt.token.TokenType;
import com.planner.travel.global.util.CookieUtil;
import com.planner.travel.global.util.RedisUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final CookieUtil cookieUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public void login(LoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(EntityNotFoundException::new);

        if (!user.isWithdrawal()) {
            authenticateUser(loginRequest);

            addAccessTokenToHeader(user.getId(), response);
            addRefreshTokenToCookieAndRedis(user.getId(), response);

        } else {
            throw new IllegalArgumentException();
        }
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
