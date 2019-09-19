package com.example.maptest.mycartest.UI.warn;


/**
 * Created by ${Author} on 2018/3/15.
 * Use to
 */

public class SaveAlarmBean {
    private String terminalID ;
    private String id;
    private Boolean isRead ;

    public SaveAlarmBean(String terminalID, String id, Boolean isRead) {
        this.terminalID = terminalID;
        this.id = id;
        this.isRead = isRead;
    }

    public SaveAlarmBean(String id, Boolean isRead) {
        this.id = id;
        this.isRead = isRead;
    }

    public SaveAlarmBean(String id) {
        this.id = id;
    }
}
