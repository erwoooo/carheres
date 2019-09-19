package com.example.maptest.mycartest.Entity;

/**
 * Copyright (C)
 * <p>
 * FileName: NbDevice
 * <p>
 * Author: ${Erwoo}
 * <p>
 * Date: 2019/7/16 16:21
 * <p>
 * Description: ${DESCRIPTION}
 */
public class NbDevice {
   private String data;
   private mete mete;

    public NbDevice() {
    }

    @Override
    public String toString() {
        return "NbDevice{" +
                "data='" + data + '\'' +
                ", mete=" + mete +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NbDevice(String data, mete mete) {
        this.data = data;
        this.mete = mete;
    }

    public static class mete{
      private   String message;
       private boolean success;

        @Override
        public String toString() {
            return "mete{" +
                    "message='" + message + '\'' +
                    ", success=" + success +
                    '}';
        }

        public mete(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public mete() {
        }
    }
}
