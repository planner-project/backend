package com.planner.travel.global.webSocket;

import com.planner.travel.global.jwt.token.TokenAuthenticator;
import com.planner.travel.global.jwt.token.TokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketInterceptor implements ChannelInterceptor {
    private final TokenAuthenticator tokenAuthenticator;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = accessor.getFirstNativeHeader("Authorization");

        log.info("===========================================================================");
        log.info("Received STOMP Message: " + message);
        log.info("All headers: " + accessor.toNativeHeaderMap());
        log.info("Access Token: " + accessToken);
        log.info("Incoming message type: " + accessor.getMessageType());
        log.info("===========================================================================");

        if (SimpMessageType.CONNECT.equals(accessor.getMessageType()) ||
                SimpMessageType.MESSAGE.equals(accessor.getMessageType()) ||
                SimpMessageType.SUBSCRIBE.equals(accessor.getMessageType())) {

            log.info("===========================================================================");
            log.info("accessor: " + accessor.getMessageType());
            log.info("===========================================================================");

            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                tokenAuthenticator.getAuthenticationUsingToken(accessToken);
                accessor.getSessionAttributes().put("Authorization", accessToken);

            } else {
                log.error("Invalid or missing Authorization header");
                throw new IllegalArgumentException("Invalid or missing Authorization header");
            }
        }

        return message;
    }
}
