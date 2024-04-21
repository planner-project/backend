package com.planner.travel.global.jwt;

import com.planner.travel.global.jwt.token.TokenAuthenticator;
import com.planner.travel.global.jwt.token.TokenExtractor;
import com.planner.travel.global.jwt.token.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final TokenExtractor tokenExtractor;
    private final TokenValidator tokenValidator;
    private final TokenAuthenticator tokenAuthenticator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.equals("/api/v1/auth/signup") ||
                requestURI.equals("/api/v1/auth/login") ||
                requestURI.startsWith("/api/v1/auth/token") ||
                requestURI.startsWith("/docs")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessTokenFromHeader = tokenExtractor.getAccessTokenFromHeader(request);
        String accessToken = null;

        if (accessTokenFromHeader != null) {
            accessToken = accessTokenFromHeader;
        }

        tokenValidator.validateAccessToken(accessToken);
        tokenAuthenticator.getAuthenticationUsingToken(accessToken);
        filterChain.doFilter(request, response);
    }
}
