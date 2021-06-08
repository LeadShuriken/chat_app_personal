package com.chat.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@ConfigurationProperties(prefix = "api")
public class WebsocketSettings implements WebSocketMessageBrokerConfigurer {

    private String version;
    private String topic;
    private String entry;
    private String origins;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(version).enableSimpleBroker(topic);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(entry).setAllowedOrigins(origins).withSockJS();
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public void setOrigins(String origins) {
        this.origins = origins;
    }
}
