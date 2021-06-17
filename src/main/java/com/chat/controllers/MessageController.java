package com.chat.controllers;

import com.chat.annotations.SQLInjectionSafe;
import com.chat.dao.Message;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.version}")
public class MessageController {

    @GetMapping("/join")
    public ModelAndView join(@RequestParam @SQLInjectionSafe String id, ModelMap model) {
        model.addAttribute("chatname", id);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("user", null,
                Collections.singleton((GrantedAuthority) () -> "USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ModelAndView("forward:/index", model);
    }

    @MessageMapping("/register/{chatName}")
    @SendTo("/chat/{chatName}")
    public Message register(@Valid @Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor,
            @DestinationVariable @SQLInjectionSafe String chatName) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("chatname", chatName);
        return chatMessage;
    }

    @MessageMapping("/send/{chatName}")
    @SendTo("/chat/{chatName}")
    public Message sendMessage(@Valid @Payload Message chatMessage) {
        return chatMessage;
    }
}
