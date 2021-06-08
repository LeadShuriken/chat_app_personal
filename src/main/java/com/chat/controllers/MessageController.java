package com.chat.controllers;

import com.chat.annotations.SQLInjectionSafe;
import com.chat.dao.Message;
import com.chat.services.ApplicationDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.version}")
public class MessageController {

    @Autowired
    private ApplicationDataService applicationDataService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Value("${api.destination}")
    private String destination;

    @MessageMapping(value = "${api.entry}/{to}")
    public void sendMessage(@DestinationVariable @SQLInjectionSafe String to, @Valid Message message) {
        System.out.println("Handling send message: " + message + " to " + to);

        if (applicationDataService.getAllUsers().contains(to)) {
            simpMessagingTemplate.convertAndSend(destination + to, message);
        }
    }
}
