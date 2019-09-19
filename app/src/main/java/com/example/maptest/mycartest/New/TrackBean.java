package com.example.maptest.mycartest.New;

/**
 * Created by ${Author} on 2018/3/13.
 * Use to
 */

public class TrackBean {
    private String terminalID;
    private long beginTime;
    private long endTime;
    private int pageNum;
    private int pageSize;
    private String locationType;

    public TrackBean(String terminalID, long beginTime, long endTime, int pageNum, int pageSize, String locationType) {
        this.terminalID = terminalID;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.locationType = locationType;
    }

}
