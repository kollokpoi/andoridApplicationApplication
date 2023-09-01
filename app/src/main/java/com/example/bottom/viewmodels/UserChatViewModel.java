package com.example.bottom.viewmodels;

import com.example.bottom.models.Chat;
import com.example.bottom.models.User;

import java.io.Serializable;

public class UserChatViewModel implements Serializable {
    private User User;
    private Chat PrivateChat;

    public com.example.bottom.models.User getUser() {
        return User;
    }

    public void setUser(com.example.bottom.models.User user) {
        User = user;
    }

    public com.example.bottom.models.Chat getChat() {
        return PrivateChat;
    }
}
