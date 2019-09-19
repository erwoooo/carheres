package com.example.maptest.mycartest.New;

import java.util.Arrays;

/**
 * Created by ${Author} on 2018/3/15.
 * Use to
 */

public class TrickListBean {
        private boolean acc;
        private double bdLat;
        private double bdLon;
        private int course;
        private int gsm;
        private String loationType;
        private long serverTime;
        private int speed;
        private long stateTime;
        private boolean supplement;
        private String terminalID;
        private long utcTime;

    public TrickListBean() {
    }

    @Override
    public String toString() {
        return "TrickListBean{" +
                "acc=" + acc +
                ", bdLat=" + bdLat +
                ", bdLon=" + bdLon +
                ", course=" + course +
                ", gsm=" + gsm +
                ", loationType='" + loationType + '\'' +
                ", serverTime=" + serverTime +
                ", speed=" + speed +
                ", stateTime=" + stateTime +
                ", supplement=" + supplement +
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

        public int getGsm() {
            return gsm;
        }

        public void setGsm(int gsm) {
            this.gsm = gsm;
        }

        public String getLoationType() {
            return loationType;
        }

        public void setLoationType(String loationType) {
            this.loationType = loationType;
        }

        public long getServerTime() {
            return serverTime;
        }

        public void setServerTime(long serverTime) {
            this.serverTime = serverTime;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public long getStateTime() {
            return stateTime;
        }

        public void setStateTime(long stateTime) {
            this.stateTime = stateTime;
        }

        public boolean isSupplement() {
            return supplement;
        }

        public void setSupplement(boolean supplement) {
            this.supplement = supplement;
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

    public TrickListBean(boolean acc, double bdLat, double bdLon, int course, int gsm, String loationType, long serverTime, int speed, long stateTime, boolean supplement, String terminalID, long utcTime) {
        this.acc = acc;
        this.bdLat = bdLat;
        this.bdLon = bdLon;
        this.course = course;
        this.gsm = gsm;
        this.loationType = loationType;
        this.serverTime = serverTime;
        this.speed = speed;
        this.stateTime = stateTime;
        this.supplement = supplement;
        this.terminalID = terminalID;
        this.utcTime = utcTime;
    }
}
