package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2018/7/2.
 * Use to
 */

public class CommandResponse {
    private String terminalID;
    private String content;

    public CommandResponse() {
    }

    @Override
    public String toString() {
        return "CommandResponse{" +
                "terminalID='" + terminalID + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public CommandResponse(String terminalID, String content) {
        this.terminalID = terminalID;
        this.content = content;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
