package com.planner.travel.global.webSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

@Configuration
@EnableWebSocketSecurity
public class MessageSecurityConfiguration {
    @Bean
    AuthorizationManager<Message<?>> authorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .nullDestMatcher().permitAll()
                .simpTypeMatchers(
                        SimpMessageType.CONNECT,
                        SimpMessageType.HEARTBEAT,
                        SimpMessageType.UNSUBSCRIBE,
                        SimpMessageType.DISCONNECT
                ).permitAll()
                .simpTypeMatchers(
                        SimpMessageType.MESSAGE,
                        SimpMessageType.SUBSCRIBE
                ).authenticated()
                .simpDestMatchers("/pub/**").authenticated()
                .simpDestMatchers("/sub/**").authenticated()
                .anyMessage().denyAll();

        return messages.build();
    }

    @Bean("csrfChannelInterceptor")
    public ChannelInterceptor csrfChannelInterceptor() {
        return new ChannelInterceptor() {
        };
    }
}
