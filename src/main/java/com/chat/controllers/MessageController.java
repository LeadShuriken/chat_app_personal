package com.chat.controllers;

import com.chat.annotations.SQLInjectionSafe;
import com.chat.dao.Message;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.version}")
public class MessageController {

    @GetMapping("/join")
    public void join(@RequestParam @SQLInjectionSafe String id) {
        System.out.println(id);
    }

    @MessageMapping("/register/{chatName}")
    @SendTo("/chat/{chatName}")
    public Message register(@Valid @Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/send/{chatName}")
    @SendTo("/chat/{chatName}")
    public Message sendMessage(@Valid @Payload Message chatMessage) {
        return chatMessage;
    }
}
