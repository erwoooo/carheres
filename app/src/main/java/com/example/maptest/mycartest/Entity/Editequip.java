package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2018/3/10.
 * Use to
 */

public class Editequip {
    private int id;
    private String nickname;
    private String carNumber;

    public Editequip(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public Editequip(int id, String nickname, String carNumber) {
        this.id = id;
        this.nickname = nickname;
        this.carNumber = carNumber;
    }

    public Editequip(String carNumber, int id) {
        this.carNumber = carNumber;
        this.id = id;
    }
}
