package com.planner.travel.global.jwt.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenExtractor {
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String accessTokenFromHeader = request.getHeader("Authorization")
                .substring(7);

        log.info("===========================================================================");
        log.info("AccessToken from header: " + accessTokenFromHeader);
        log.info("===========================================================================");

        if (!accessTokenFromHeader.isEmpty()) {
            return accessTokenFromHeader;
        }

        return null;
    }
}
