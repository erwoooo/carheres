package com.example.maptest.mycartest.UI.SetUi.service;

import java.io.Serializable;

/**
 * Created by ${Author} on 2018/3/8.
 * Use to
 */

public class SendDataBean implements Serializable{
    private meta meta;
    private String data;

    @Override
    public String toString() {
        return "LoginDataBean{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }

    public SendDataBean() {
    }

    public meta getMeta() {
        return meta;
    }

    public void setMeta(meta meta) {
        this.meta = meta;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public SendDataBean(SendDataBean.meta meta, String data) {
        this.meta = meta;
        this.data = data;
    }

    public static class meta implements Serializable{
        private boolean success;
        private String message;

        @Override
        public String toString() {
            return "meta{" +
                    "success=" + success +
                    ", message='" + message + '\'' +
                    '}';
        }

        public meta() {
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public meta(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

}
