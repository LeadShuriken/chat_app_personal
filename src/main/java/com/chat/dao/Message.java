package com.chat.dao;

import com.chat.annotations.SQLInjectionSafe;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    @SQLInjectionSafe
    @JsonProperty("message")
    private final String message;

    public String getMessage() {
        return message;
    }

    public String getFromLogin() {
        return fromLogin;
    }

    @SQLInjectionSafe
    @JsonProperty("fromLogin")
    private final String fromLogin;

    public Message(String message, String fromLogin) {
        this.fromLogin = fromLogin;
        this.message = message;
    }

    public Message() {
        this.fromLogin = null;
        this.message = null;
    }

    @Override
    public String toString() {
        return "Message [fromLogin=" + fromLogin + ", message=" + message + "]";
    }
}
