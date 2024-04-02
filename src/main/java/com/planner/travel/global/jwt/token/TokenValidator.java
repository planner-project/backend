package com.planner.travel.global.jwt.token;

import com.planner.travel.domain.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
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
public class TokenValidator implements InitializingBean {
    private final UserRepository userRepository;

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

    public void validateAccessToken(String token) throws ExpiredJwtException {
        token = token.substring(7);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
