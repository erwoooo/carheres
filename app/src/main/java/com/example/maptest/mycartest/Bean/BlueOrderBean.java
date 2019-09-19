package com.example.maptest.mycartest.Bean;

import java.io.Serializable;

/**
 * Created by ${Author} on 2017/10/10.
 * Use to
 */

public class BlueOrderBean implements Serializable{
    private String address;
    private boolean status;

    @Override
    public String toString() {
        return "BlueOrderBean{" +
                "address='" + address + '\'' +
                ", status=" + status +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public BlueOrderBean() {
    }

    public BlueOrderBean(String address, boolean status) {
        this.address = address;
        this.status = status;
    }
}
