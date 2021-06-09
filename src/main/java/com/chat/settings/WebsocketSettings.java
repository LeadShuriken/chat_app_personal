package com.chat.settings;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@ConfigurationProperties(prefix = "api")
public class WebsocketSettings implements WebSocketMessageBrokerConfigurer {

    private String version;
    private String topic;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
            .setApplicationDestinationPrefixes(version)
            .setUserDestinationPrefix(topic)
            .enableSimpleBroker(topic);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOrigins("*")
            .withSockJS();
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(new ObjectMapper());
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
