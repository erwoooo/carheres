package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2018/9/8.
 * Use to
 */

public class MyBean {
    private int xNumber;
    private int yNumber;

    @Override
    public String toString() {
        return "MyBean{" +
                "xNumber=" + xNumber +
                ", yNumber=" + yNumber +
                '}';
    }

    public MyBean(int xNumber, int yNumber) {
        this.xNumber = xNumber;
        this.yNumber = yNumber;
    }

    public int getxNumber() {
        return xNumber;
    }

    public void setxNumber(int xNumber) {
        this.xNumber = xNumber;
    }

    public int getyNumber() {
        return yNumber;
    }

    public void setyNumber(int yNumber) {
        this.yNumber = yNumber;
    }
}
