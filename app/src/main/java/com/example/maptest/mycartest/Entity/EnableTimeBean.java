package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2019/2/13.
 * Use to
 */

public class EnableTimeBean extends BaseEntity {
    private data data;
    private mete mete;

    public EnableTimeBean() {
    }

    public EnableTimeBean(data data, mete mete) {
        this.data = data;
        this.mete = mete;
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public mete getMete() {
        return mete;
    }

    public void setMete(mete mete) {
        this.mete = mete;
    }

    @Override
    public String toString() {
        return "EnableTimeBean{" +
                "data=" + data +
                ", mete=" + mete +
                '}';
    }

    public static class data{
        private long enableTime;
        private String id;
        private String terminalID;

        @Override
        public String toString() {
            return "data{" +
                    "enableTime=" + enableTime +
                    ", id='" + id + '\'' +
                    ", terminalID='" + terminalID + '\'' +
                    '}';
        }

        public long getEnableTime() {
            return enableTime;
        }

        public void setEnableTime(long enableTime) {
            this.enableTime = enableTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTerminalID() {
            return terminalID;
        }

        public void setTerminalID(String terminalID) {
            this.terminalID = terminalID;
        }

        public data() {
        }

        public data(long enableTime, String id, String terminalID) {
            this.enableTime = enableTime;
            this.id = id;
            this.terminalID = terminalID;
        }
    }

    public static class mete{
        private String message;
        private boolean success;

        public mete() {
        }

        @Override
        public String toString() {
            return "mete{" +
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
