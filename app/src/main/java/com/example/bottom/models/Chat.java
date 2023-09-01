package com.example.bottom.models;

import android.media.Image;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable {
    private int Id;

    public List<Message> Message;
    public ArrayList<User> Users;

    public ArrayList<User> getMembers() {
        return Users;
    }

    public List<Message> getMessages() {
        return Message;
    }

    public String GetLastMessage(){
        if (Message!=null){
            Message lastMessage = this.Message.get(this.getMessages().size()-1);
            return lastMessage.GetFullMessageText();
        }
        else return "";
    }

    public int getId() {
        return Id;
    }

    public void setMessage(List<com.example.bottom.models.Message> message) {
        Message = message;
    }
}
