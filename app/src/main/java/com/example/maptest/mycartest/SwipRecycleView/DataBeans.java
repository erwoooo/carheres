package com.example.maptest.mycartest.SwipRecycleView;

/**
 * Created by ${Author} on 2017/3/9.
 * Use to
 */

public class DataBeans {
    private String name;
    private String numb;

    public DataBeans() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumb() {
        return numb;
    }

    public void setNumb(String numb) {
        this.numb = numb;
    }

    public DataBeans(String name, String numb) {
        this.name = name;
        this.numb = numb;
    }
}
