package com.example.bottom.viewmodels;

import com.example.bottom.models.Chat;
import com.example.bottom.models.ObjectClass;

import java.util.List;

public class ChatsViewModel {
    private Chat MainChat;
    private List<ObjectClass> ObjectChats;
    private List<UserChatViewModel> PrivateChats;



    public Chat getMainChat() {
        return MainChat;
    }
    public List<UserChatViewModel> getPrivateChats() {
        return PrivateChats;
    }
    public List<ObjectClass> getObjectChats() {
        return ObjectChats;
    }
}
