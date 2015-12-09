package com.soundbytes;


public class User {
    String name, username, password;
    int age;

    public User(String name, int age, String username, String password){
        this.name = name.trim();
        this.age = age;
        this.username = username.trim();
        this.password = password;
    }
    public User(String username, String password){
        this.username = username.trim();
        this.password = password;
        this.age = -1;
        this.name = "";
    }
    public User(String username, String currentUser, int age){
        this.username = username.trim();
        this.password = currentUser;
        this.age = age;
        this.name = "";
    }
}

