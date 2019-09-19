package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2017/10/7.
 * Use to
 */

public class AMatchBean {
    private String name;
    private String mac;

    @Override
    public String toString() {
        return "AMatchBean{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
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

    public AMatchBean() {
    }

    public AMatchBean(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }
}
