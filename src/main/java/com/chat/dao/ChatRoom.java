package com.chat.dao;

import java.util.UUID;
import com.chat.annotations.UUIDA;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRoom {

    @UUIDA
    @JsonProperty("id")
    private UUID id;

    @UUIDA
    @JsonProperty("chatId")
    private UUID chatId;

    @UUIDA
    @JsonProperty("senderId")
    private UUID senderId;

    @UUIDA
    @JsonProperty("recipientId")
    private UUID recipientId;
}