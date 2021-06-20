package com.chat.security;

import com.chat.repository.TokenDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthenticatorService {

    @Autowired
    private TokenDataService tokenDataService;

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String chatName)
            throws AuthenticationException {

        if (chatName == null || chatName.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Chatname was null or empty.");
        }

        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("");
            }

            final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    authentication.getName(), null, authentication.getAuthorities());

            if (((String) authentication.getPrincipal()).startsWith("user")) {
                return authenticationToken;
            }

            if (authentication.getPrincipal().equals("admin") && tokenDataService.bellowSessionLimit()
                    && !tokenDataService.tokenExists(chatName)) {
                tokenDataService.setToken(chatName);
                return authenticationToken;
            } else {
                throw new RuntimeException();
            }
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad credentials for chat '" + chatName + "' !");
        } catch (RuntimeException e) {
            throw new RuntimeException("Too Full for chat '" + chatName + "' !");
        }
    }
}