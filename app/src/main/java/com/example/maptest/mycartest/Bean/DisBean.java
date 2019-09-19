package com.example.maptest.mycartest.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by ${Author} on 2017/5/12.
 * Use to 车辆距离信息对象
 */

public class DisBean implements Serializable,Parcelable{
    private String dis;     //距离

    public DisBean() {
    }

    protected DisBean(Parcel in) {
        dis = in.readString();
    }

    public static final Creator<DisBean> CREATOR = new Creator<DisBean>() {
        @Override
        public DisBean createFromParcel(Parcel in) {
            return new DisBean(in);
        }

        @Override
        public DisBean[] newArray(int size) {
            return new DisBean[size];
        }
    };

    @Override
    public String toString() {
        return "DisBean{" +
                "dis='" + dis + '\'' +
                '}';
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public DisBean(String dis) {
        this.dis = dis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dis);
    }
}
