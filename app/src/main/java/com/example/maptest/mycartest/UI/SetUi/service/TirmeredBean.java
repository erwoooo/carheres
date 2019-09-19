package com.example.maptest.mycartest.UI.SetUi.service;

/**
 * Created by ${Author} on 2018/3/29.
 * Use to
 */

public class TirmeredBean {
    private String terminalID;
    private int deviceProtocol;

    public TirmeredBean(String terminalID) {
        this.terminalID = terminalID;
    }

    public TirmeredBean() {
    }

    public TirmeredBean(String terminalID, int deviceProtocol) {
        this.terminalID = terminalID;
        this.deviceProtocol = deviceProtocol;
    }

}
