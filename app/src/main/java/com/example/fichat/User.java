package com.example.fichat;

public class User {
    private String email;
    private String name;
    private String userid;

    public User(String email, String name, String userid) {
        this.email = email;
        this.name = name;
        this.userid = userid;
    }

    public User() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
