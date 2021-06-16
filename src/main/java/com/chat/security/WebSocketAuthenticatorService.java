package com.chat.security;

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

    public UsernamePasswordAuthenticationToken getAuthenticatedOrFail(final String chatName)
            throws AuthenticationException {

        if (chatName == null || chatName.trim().isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Chatname was null or empty.");
        }

        try {
            SecurityContext securityContext = SecurityContextHolder.getContext();
            Authentication authentication = securityContext.getAuthentication();

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Bad credentials for " + chatName);
            }

            return new UsernamePasswordAuthenticationToken(authentication.getName(), null,
                    authentication.getAuthorities());
        } catch (Exception e) {
            throw new BadCredentialsException("Bad credentials for " + chatName);
        }
    }
}