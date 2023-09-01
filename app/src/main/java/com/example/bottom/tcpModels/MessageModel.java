package com.example.bottom.tcpModels;

import com.example.bottom.models.Message;

public class MessageModel {
    private int ChatId;
    private String UserId;
    private Message Message;

    public void setChatId(int chatId) {
        ChatId = chatId;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
    public void setMessage(com.example.bottom.models.Message message) {
        Message = message;
    }

    public int getChatId() {
        return ChatId;
    }
    public String getUserId() {
        return UserId;
    }
    public com.example.bottom.models.Message getMessage() {
        return Message;
    }
}
