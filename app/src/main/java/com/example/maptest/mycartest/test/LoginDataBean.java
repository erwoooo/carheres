package com.example.maptest.mycartest.test;

import java.io.Serializable;
import java.util.Arrays;

import static android.R.attr.data;

/**
 * Created by ${Author} on 2018/3/8.
 * Use to
 */

public class LoginDataBean implements Serializable{
    private meta meta;
    private data data;

    @Override
    public String toString() {
        return "LoginDataBean{" +
                "meta=" + meta +
                ", data=" + data +
                '}';
    }

    public LoginDataBean() {
    }

    public meta getMeta() {
        return meta;
    }

    public void setMeta(meta meta) {
        this.meta = meta;
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public LoginDataBean(meta meta, data data) {
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
    public static class data implements Serializable{
        private int id;
        private String username;
        private String nickname;
        private String password;
        private String address;
        private String telephone;
        private String terminalID;
        private String carNumber;
        private int user_id;
        private String createTime;
        private String picture;
        private String salt;
        private int role;
        private String devices[];

        @Override
        public String toString() {
            return "data{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", password='" + password + '\'' +
                    ", address='" + address + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", terminalID='" + terminalID + '\'' +
                    ", carNumber='" + carNumber + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", picture='" + picture + '\'' +
                    ", salt='" + salt + '\'' +
                    ", role=" + role +
                    ", devices=" + Arrays.toString(devices) +
                    '}';
        }

        public data() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getTerminalID() {
            return terminalID;
        }

        public void setTerminalID(String terminalID) {
            this.terminalID = terminalID;
        }

        public String getCarNumber() {
            return carNumber;
        }

        public void setCarNumber(String carNumber) {
            this.carNumber = carNumber;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public String[] getDevices() {
            return devices;
        }

        public void setDevices(String[] devices) {
            this.devices = devices;
        }

        public data(int id, String username, String nickname, String password, String address, String telephone, String terminalID, String carNumber, int user_id, String createTime, String picture, String salt, int role, String[] devices) {
            this.id = id;
            this.username = username;
            this.nickname = nickname;
            this.password = password;
            this.address = address;
            this.telephone = telephone;
            this.terminalID = terminalID;
            this.carNumber = carNumber;
            this.user_id = user_id;
            this.createTime = createTime;
            this.picture = picture;
            this.salt = salt;
            this.role = role;
            this.devices = devices;
        }
    }
}
