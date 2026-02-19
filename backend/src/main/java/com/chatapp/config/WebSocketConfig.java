package com.chatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 🆕 Read CORS origins from environment variable
    @Value("${cors.allowed.origins}")
    private String corsOrigins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable simple broker for /topic and /queue destinations
        config.enableSimpleBroker("/topic", "/queue", "/user");
        // Prefix for messages FROM client TO server
        config.setApplicationDestinationPrefixes("/app");
        // Prefix for user-specific destinations
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 🆕 Split comma-separated origins from environment variable
        String[] origins = corsOrigins.split(",");
        
        registry.addEndpoint("/ws")
                .setAllowedOrigins(origins)  // Use environment variable origins
                .withSockJS();
    }
}
