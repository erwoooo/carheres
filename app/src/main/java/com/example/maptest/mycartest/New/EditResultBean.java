package com.example.maptest.mycartest.New;



/**
 * Created by ${Author} on 2018/3/14.
 * Use to
 */

public class EditResultBean {
    private String data;
    private meta meta;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public meta getMeta() {
        return meta;
    }

    public void setMeta(meta meta) {
        this.meta = meta;
    }

    public EditResultBean() {
    }

    @Override
    public String toString() {
        return "EditResultBean{" +
                "data='" + data + '\'' +
                ", meta=" + meta +
                '}';
    }

    public EditResultBean(String data, meta meta) {
        this.data = data;
        this.meta = meta;
    }
    public static class meta {
        private String message;
        private boolean success;

        @Override
        public String toString() {
            return "meta{" +
                    "message='" + message + '\'' +
                    ", success=" + success +
                    '}';
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

        public meta(String message, boolean success) {
            this.message = message;
            this.success = success;
        }

        public meta() {
        }
    }
}
