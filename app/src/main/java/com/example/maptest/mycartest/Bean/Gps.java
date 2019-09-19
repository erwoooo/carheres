package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2017/4/25.
 * Use to经纬度对象
 */

public class Gps {
    private double wgLat;   //纬度
    private double wgLon;   //经度

    public Gps(double wgLat, double wgLon) {
        setWgLat(wgLat);
        setWgLon(wgLon);
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }

    public String toString() {
        return wgLat + "," + wgLon;
    }
}
