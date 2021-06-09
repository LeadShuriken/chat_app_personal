package com.chat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ChatRoomService {

        private final UserDataService repoService;

        @Autowired
        public ChatRoomService(UserDataService repoService) {
                this.repoService = repoService;
        }

        public String getChatId(String senderId, String recipientId, boolean createIfNotExist) {
                return "";
        }
}