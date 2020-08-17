package com.example.fichat;

import java.util.ArrayList;

public class Chat {
    private ArrayList<Message> chatlist;
    private ArrayList<User> users;

    public Chat(ArrayList<Message> chatlist, ArrayList<User> users) {
        this.chatlist = chatlist;
        this.users = users;
    }

    public Chat() {
    }

    public ArrayList<Message> getChatlist() {
        return chatlist;
    }

    public void setChatlist(ArrayList<Message> chatlist) {
        this.chatlist = chatlist;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
