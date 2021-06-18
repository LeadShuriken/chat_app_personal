package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenDataService {

    private final TokenDataRepository tokenDataRepository;
    private final int maxTokenSessions;

    @Autowired
    public TokenDataService(TokenDataRepository tokenDataRepository,
            @Value("${maxTokenSessions}") int maxTokenSessions) {
        this.tokenDataRepository = tokenDataRepository;
        this.maxTokenSessions = maxTokenSessions;
    }

    public void setToken(String token) {
        tokenDataRepository.setToken(token);
    }

    public void removeToken(String token) {
        tokenDataRepository.removeToken(token);
    }

    public boolean tokenExists(String token) {
        return tokenDataRepository.tokenExists(token);
    }

    public boolean bellowSessionLimit() {
        return maxTokenSessions > tokenDataRepository.getTokenCount();
    }
}
