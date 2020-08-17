package com.example.fichat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String messageText;
    private String messageUser;
    private String messageTime;
    private String messageIcon;

    public Message(String messageText, String messageUser, String messageIcon) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageIcon = messageIcon;
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        messageTime = formatForDateNow.format(new Date());
    }

    public Message() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageIcon() {
        return messageIcon;
    }

    public void setMessageIcon(String messageIcon) {
        this.messageIcon = messageIcon;
    }
}
