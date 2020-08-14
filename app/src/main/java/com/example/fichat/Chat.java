package com.example.fichat;

import java.util.ArrayList;

public class Chat {
    private ArrayList<Message> chatlist;
    private String chatusers;

    public Chat(ArrayList<Message> chatlist, String chatusers) {
        this.chatlist = chatlist;
        this.chatusers = chatusers;
    }

    public Chat() {
    }

    public ArrayList<Message> getChatlist() {
        return chatlist;
    }

    public void setChatlist(ArrayList<Message> chatlist) {
        this.chatlist = chatlist;
    }

    public String getChatusers() {
        return chatusers;
    }

    public void setChatusers(String chatusers) {
        this.chatusers = chatusers;
    }
}
