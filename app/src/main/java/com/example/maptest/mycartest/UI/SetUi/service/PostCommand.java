package com.example.maptest.mycartest.UI.SetUi.service;

/**
 * Created by ${Author} on 2018/5/25.
 * Use to
 */

public class PostCommand {
    private String terminalID ; //设备IMEI号

    private String content ; //指令内容

    private String agentName = ""; //下发代理商名称

    private String backContent = ""; //回复内容

    public PostCommand(String terminalID, String content, String agentName, String backContent) {
        this.terminalID = terminalID;
        this.content = content;
        this.agentName = agentName;
        this.backContent = backContent;
    }

    public PostCommand(String terminalID, String content, String agentName) {
        this.terminalID = terminalID;
        this.content = content;
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return "PostCommand{" +
                "terminalID='" + terminalID + '\'' +
                ", content='" + content + '\'' +
                ", agentName='" + agentName + '\'' +
                ", backContent='" + backContent + '\'' +
                '}';
    }
}
