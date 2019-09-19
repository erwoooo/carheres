package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2017/9/7.
 * Use to
 */

public class PriceBean {
    private String id;
    private String user_id;
    private float oneYearPrice;
    private float twoYearPrice;
    private float foreverPrice;
    private float oneDayPrice;

    @Override
    public String toString() {
        return "PriceBean{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", oneYearPrice=" + oneYearPrice +
                ", twoYearPrice=" + twoYearPrice +
                ", foreverPrice=" + foreverPrice +
                ", oneDayPrice=" + oneDayPrice +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public float getOneYearPrice() {
        return oneYearPrice;
    }

    public void setOneYearPrice(float oneYearPrice) {
        this.oneYearPrice = oneYearPrice;
    }

    public float getTwoYearPrice() {
        return twoYearPrice;
    }

    public void setTwoYearPrice(float twoYearPrice) {
        this.twoYearPrice = twoYearPrice;
    }

    public float getForeverPrice() {
        return foreverPrice;
    }

    public void setForeverPrice(float foreverPrice) {
        this.foreverPrice = foreverPrice;
    }

    public float getOneDayPrice() {
        return oneDayPrice;
    }

    public void setOneDayPrice(float oneDayPrice) {
        this.oneDayPrice = oneDayPrice;
    }

    public PriceBean(String id, String user_id, float oneYearPrice, float twoYearPrice, float foreverPrice, float oneDayPrice) {
        this.id = id;
        this.user_id = user_id;
        this.oneYearPrice = oneYearPrice;
        this.twoYearPrice = twoYearPrice;
        this.foreverPrice = foreverPrice;
        this.oneDayPrice = oneDayPrice;
    }

    public PriceBean() {
    }
}
