package com.lawstack.app.configuration.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final  String[] origins = { "http://localhost:4200", "https://checkout.stripe.com",
                        "http://139.59.215.241",
                        "http://lawtasks.pro", "https://lawtasks.pro", "https://139.59.215.241" };
   @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
        .addEndpoint("/ws")
        .setAllowedOriginPatterns(origins)
        .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/chatroom","/userChat");
        registry.setUserDestinationPrefix("/userChat");
    }
}