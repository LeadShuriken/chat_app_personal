package com.chat.settings;

import com.chat.dao.Message;
import com.chat.model.MessageStatus;
import com.chat.repository.TokenDataService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketChatEventListener {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    private TokenDataService tokenDataService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String chatname = (String) headerAccessor.getSessionAttributes().get("chatname");
        String usertype = (String) headerAccessor.getSessionAttributes().get("usertype");

        try {
            if (chatname != null) {
                if (username != null) {
                    Message chatMessage = new Message();
                    chatMessage.setType(MessageStatus.LEAVE);
                    chatMessage.setSender(username);
                    messagingTemplate.convertAndSend("/chat/" + chatname, chatMessage);
                }
                // User closed tab or is an admin
                if (usertype.equals("admin")) {
                    tokenDataService.removeToken(chatname);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}