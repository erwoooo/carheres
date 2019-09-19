package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2018/3/10.
 * Use to
 */

public class EditPass {
    private int id;
    private int roles;
    private String password;
    private String username;

    public EditPass(int id, int roles, String password, String username) {
        this.id = id;
        this.roles = roles;
        this.password = password;
        this.username = username;
    }

    public EditPass(int id, int roles, String password) {
        this.id = id;
        this.roles = roles;
        this.password = password;
    }
}
