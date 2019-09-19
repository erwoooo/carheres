package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2019/4/27.
 * Use to
 */

public class AppVersion {


    /**
     * data : {"appName":"car","description":"车在这儿版本更新","version":"1.7.4"}
     * meta : {"message":"200","success":true}
     */

    private DataBean data;
    private MetaBean meta;

    @Override
    public String toString() {
        return "AppVersion{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public static class DataBean {
        /**
         * appName : car
         * description : 车在这儿版本更新
         * version : 1.7.4
         */

        private String appName;
        private String description;
        private String version;

        @Override
        public String toString() {
            return "DataBean{" +
                    "appName='" + appName + '\'' +
                    ", description='" + description + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
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
