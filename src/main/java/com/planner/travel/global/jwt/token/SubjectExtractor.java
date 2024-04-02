package com.planner.travel.global.jwt.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubjectExtractor implements InitializingBean {

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

    public Long getUesrIdFromToken(String token) {
        if (token.contains("Bearer")) {
            token = token.substring(7);
        }

        String userIdFromToken = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        Long userId = Long.parseLong(userIdFromToken);
        log.info("===========================================================================");
        log.info("New refreshToken: " + token);
        log.info("===========================================================================");

        return userId;
    }
}
