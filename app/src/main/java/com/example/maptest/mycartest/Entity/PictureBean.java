package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2019/4/13.
 * Use to
 */

public class PictureBean {


    /**
     * data : http://api.carhere.net/images/2019-04-13/864296100117539.jpg
     * meta : {"message":"200","success":true}
     */

    private String data;
    private MetaBean meta;

    @Override
    public String toString() {
        return "PictureBean{" +
                "data='" + data + '\'' +
                ", meta=" + meta +
                '}';
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public static class MetaBean {
        /**
         * message : 200
         * success : true
         */

        private String message;
        private boolean success;

        @Override
        public String toString() {
            return "MetaBean{" +
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
    }
}
