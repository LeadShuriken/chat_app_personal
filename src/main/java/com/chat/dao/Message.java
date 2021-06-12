package com.chat.dao;

import java.time.LocalDate;
import javax.validation.constraints.Past;

import com.chat.model.MessageStatus;
import com.chat.annotations.SQLInjectionSafe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    @SQLInjectionSafe
    @JsonProperty("content")
    private String content;

    @SQLInjectionSafe
    @JsonProperty("sender")
    private String sender;

    @Past
    @JsonProperty("timestamp")
    private LocalDate timestamp;

    @JsonProperty("type")
    private MessageStatus type;
}
