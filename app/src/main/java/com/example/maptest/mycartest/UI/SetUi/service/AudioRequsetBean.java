package com.example.maptest.mycartest.UI.SetUi.service;

import java.util.List;

/**
 * Created by ${Author} on 2018/7/17.
 * Use to
 */

public class AudioRequsetBean {
    private List<AvideoBean> data;
    private meta meta;

    @Override
    public String toString() {
        return "AudioRequsetBean{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    public AudioRequsetBean() {
    }

    public AudioRequsetBean(List<AvideoBean> data, AudioRequsetBean.meta meta) {
        this.data = data;
        this.meta = meta;
    }

    public List<AvideoBean> getData() {
        return data;
    }

    public void setData(List<AvideoBean> data) {
        this.data = data;
    }

    public AudioRequsetBean.meta getMeta() {
        return meta;
    }

    public void setMeta(meta meta) {
        this.meta = meta;
    }
    public static class meta{
        private int message;
        private boolean success;
    }
}
