package com.planner.travel.global.jwt.token;

import com.planner.travel.global.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenGenerator implements InitializingBean {
    private final RedisUtil redisUtil;
    static final long ACCESS_TOKEN_VALID_TIME = 15 * 60 * 1000L; // 15 분간 유효.
    static final long REFRESH_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30 분간 유효.

    @Value("${spring.secret.key}")
    private String SECRET_KEY;
    private Key key;

    @Override
    public void afterPropertiesSet() {
        generateKey();
    }

    private void generateKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // tokenGenerator
    public String generateToken (TokenType tokenType, String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        long extraTime = 0L;

        if (tokenType.equals(TokenType.ACCESS)) {
            extraTime = ACCESS_TOKEN_VALID_TIME;
        } else if (tokenType.equals(TokenType.REFRESH)) {
            extraTime = REFRESH_TOKEN_VALID_TIME;
        }

        log.info("===========================================================================");
        log.info("ExtraTime: " + extraTime);
        log.info("===========================================================================");

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + extraTime))
                .signWith(key)
                .compact();

        if (tokenType.equals(TokenType.ACCESS)) {
            String accessToken = "Bearer " + token;
            log.info("===========================================================================");
            log.info("New accessToken: " + accessToken);
            log.info("===========================================================================");

            return accessToken;

        } else if (tokenType.equals(TokenType.REFRESH)) {
            redisUtil.setData(userId, token);
            log.info("===========================================================================");
            log.info("New refreshToken: " + token);
            log.info("===========================================================================");

            return token;
        }

        return null;
    }
}
