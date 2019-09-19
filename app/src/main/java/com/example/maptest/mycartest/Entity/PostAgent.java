package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2018/5/26.
 * Use to
 */

public class PostAgent {
    private String terminalID;
    private int userId;
    private String nickname;
    private String carNumber;

    public PostAgent(String terminalID, int userId, String nickname, String carNumber) {
        this.terminalID = terminalID;
        this.userId = userId;
        this.nickname = nickname;
        this.carNumber = carNumber;
    }

    @Override
    public String toString() {
        return "PostAgent{" +
                "terminalID='" + terminalID + '\'' +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", carNumber='" + carNumber + '\'' +
                '}';
    }
}
