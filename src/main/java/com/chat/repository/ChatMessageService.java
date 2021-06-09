package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

import com.chat.dao.Message;
import com.chat.model.MessageStatus;

@Service
public class ChatMessageService {

    private final ChatRoomService chatRoomService;
    private final UserDataService repoService;

    @Autowired
    public ChatMessageService(UserDataService repoService, ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
        this.repoService = repoService;
    }

    public Message save(Message chatMessage) {
        return chatMessage;
    }

    public long newMessages(String senderId, String recipientId) {
        return 1l;
    }

    public List<Message> getChatMessages(String senderId, String recipientId) {
        return new ArrayList<Message>();
    }

    public Message findMessageById(String id) {
        return new Message();
    };

    public void updateStatuses(String senderId, String recipientId, MessageStatus status) {
    }
}