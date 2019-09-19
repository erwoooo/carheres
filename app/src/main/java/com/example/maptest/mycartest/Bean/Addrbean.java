package com.example.maptest.mycartest.Bean;

import com.example.maptest.mycartest.New.LocationListBean;

/**
 * Created by ${Author} on 2017/3/17.
 * Use 包含当前车辆信息和在列表中的位置
 */

public class Addrbean {
    private String addrs;       //反地理编码获取地理位置
    private LocationListBean useBean;       //用户位置信息和用户信息
    int i ;                               //在集合中的位置

    public String getAddrs() {
        return addrs;
    }

    public void setAddrs(String addrs) {
        this.addrs = addrs;
    }

    public LocationListBean getUseBean() {
        return useBean;
    }

    public void setUseBean(LocationListBean useBean) {
        this.useBean = useBean;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "Addrbean{" +
                "addrs='" + addrs + '\'' +
                ", useBean=" + useBean +
                ", i=" + i +
                '}';
    }

    public Addrbean(String addrs, LocationListBean useBean, int i) {
        this.addrs = addrs;
        this.useBean = useBean;
        this.i = i;
    }
}
