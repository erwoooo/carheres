package com.example.maptest.mycartest.UI.warn;

import java.io.Serializable;

/**
 * Created by ${Author} on 2018/3/16.
 * Use to
 */

public class WarnListBean implements Serializable{
    private boolean acc;
    private String alarmAddress;
    private int alarmState;
    private double bdLat;
    private double bdLon;
    private int course;
    private int electric;
    private String expireTime;
    private int gsm;
    private String id;
    private boolean isRead;
    private String locationType;
    private int speed;
    private String terminalID;
    private long utcTime;

    @Override
    public String toString() {
        return "WarnListBean{" +
                "acc=" + acc +
                ", alarmAddress='" + alarmAddress + '\'' +
                ", alarmState=" + alarmState +
                ", bdLat=" + bdLat +
                ", bdLon=" + bdLon +
                ", course=" + course +
                ", electric=" + electric +
                ", expireTime='" + expireTime + '\'' +
                ", gsm=" + gsm +
                ", id='" + id + '\'' +
                ", isRead=" + isRead +
                ", locationType='" + locationType + '\'' +
                ", speed=" + speed +
                ", terminalID='" + terminalID + '\'' +
                ", utcTime=" + utcTime +
                '}';
    }

    public boolean isAcc() {
        return acc;
    }

    public void setAcc(boolean acc) {
        this.acc = acc;
    }

    public String getAlarmAddress() {
        return alarmAddress;
    }

    public void setAlarmAddress(String alarmAddress) {
        this.alarmAddress = alarmAddress;
    }

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public double getBdLat() {
        return bdLat;
    }

    public void setBdLat(double bdLat) {
        this.bdLat = bdLat;
    }

    public double getBdLon() {
        return bdLon;
    }

    public void setBdLon(double bdLon) {
        this.bdLon = bdLon;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public int getElectric() {
        return electric;
    }

    public void setElectric(int electric) {
        this.electric = electric;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getGsm() {
        return gsm;
    }

    public void setGsm(int gsm) {
        this.gsm = gsm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getTerminalID() {
        return terminalID;
    }

    public void setTerminalID(String terminalID) {
        this.terminalID = terminalID;
    }

    public long getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(long utcTime) {
        this.utcTime = utcTime;
    }

    public WarnListBean() {
    }

    public WarnListBean(boolean acc, String alarmAddress, int alarmState, double bdLat, double bdLon, int course, int electric, String expireTime, int gsm, String id, boolean isRead, String locationType, int speed, String terminalID, long utcTime) {
        this.acc = acc;
        this.alarmAddress = alarmAddress;
        this.alarmState = alarmState;
        this.bdLat = bdLat;
        this.bdLon = bdLon;
        this.course = course;
        this.electric = electric;
        this.expireTime = expireTime;
        this.gsm = gsm;
        this.id = id;
        this.isRead = isRead;
        this.locationType = locationType;
        this.speed = speed;
        this.terminalID = terminalID;
        this.utcTime = utcTime;
    }
}
