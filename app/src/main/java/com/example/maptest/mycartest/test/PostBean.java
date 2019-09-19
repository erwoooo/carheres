package com.example.maptest.mycartest.test;

/**
 * Created by ${Author} on 2018/3/8.
 * Use to
 */

public class PostBean {
    private String username;
    private String password;


    public PostBean(String username) {
        this.username = username;
    }

    public PostBean() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PostBean(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
