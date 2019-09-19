package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2018/3/10.
 * Use to
 */

public class EditInfo {
    private int id;
    private int role;
    private String telephone;
    private String address;

    public EditInfo(int id, int role,String telephone, String address) {
        this.id = id;
        this.role = role;
        this.telephone = telephone;
        this.address = address;
    }

    public EditInfo(int id, String telephone, int role) {
        this.id = id;
        this.telephone = telephone;
        this.role = role;
    }

    public EditInfo(int id, int role, String address) {
        this.id = id;
        this.role = role;
        this.address = address;
    }
}
