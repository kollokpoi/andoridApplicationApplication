package com.example.bottom.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Message implements Serializable {
    private int id;
    private Chat Chat;
    private String MessageText;
    private String TimeStamp;
    private User User;
    private List<MessageData> MessageData;

    public void setChat(com.example.bottom.models.Chat chat) {
        Chat = chat;
    }
    public void setTimeStamp(String timeStamp) {
        TimeStamp = timeStamp;
    }
    public void setMessageText(String messageText) {
        MessageText = messageText;
    }
    public void setMessageData(List<MessageData> messageData) {
        this.MessageData = messageData;
    }
    public void setUser(com.example.bottom.models.User user) {
        User = user;
    }

    public List<MessageData> getMessageData() {
        return MessageData;
    }
    public String getDateTime() {
        return TimeStamp;
    }
    public String getMessageText() {
        return MessageText;
    }
    public com.example.bottom.models.User getUser() {
        return User;
    }
    public String GetFullMessageText(){
        if (!MessageText.isEmpty()){
            return User.GetFullName()+": " + MessageText;
        } else if (!MessageData.isEmpty()) {
            int filesCount = MessageData.size();
            if (filesCount==1){
                return "файл";
            }else if (filesCount>1 && filesCount<=4){
                return filesCount + " файла";
            }else return filesCount + " файлов";
        }
        else return "";
    }
}
