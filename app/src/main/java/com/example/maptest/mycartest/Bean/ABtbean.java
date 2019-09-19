package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2017/10/7.
 * Use to
 */

public class ABtbean {
    private String name;
    private String mac;
    private String statue;

    @Override
    public String toString() {
        return "ABtbean{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", statue='" + statue + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public ABtbean() {
    }

    public ABtbean(String name, String mac, String statue) {
        this.name = name;
        this.mac = mac;
        this.statue = statue;
    }
}
