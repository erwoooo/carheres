package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2017/3/30.
 * Use to 下拉列表的对象
 */

public class SpineBean {
    private String name;    //下拉框
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public SpineBean(String name) {
        this.name = name;
    }
}
