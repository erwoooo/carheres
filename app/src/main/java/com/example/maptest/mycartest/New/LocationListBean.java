package com.example.maptest.mycartest.New;

import java.io.Serializable;

/**
 * Created by ${Author} on 2018/3/14.
 * Use to
 */

public class LocationListBean implements Serializable{
    private String address;
    private String carNumber;
    private String createTime;
    private device device;
    private boolean expire;
    private int id;
    private location location;
    private String nickname;
    private int role;
    private int state;
    private String telephone;
    private String terminalID;
    private String username;
    private int user_id;
    private String picture;

    @Override
    public String toString() {
        return "LocationListBean{" +
                "address='" + address + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", createTime='" + createTime + '\'' +
                ", device=" + device +
                ", expire=" + expire +
                ", id=" + id +
                ", location=" + location +
                ", nickname='" + nickname + '\'' +
                ", role=" + role +
                ", state=" + state +
                ", telephone='" + telephone + '\'' +
                ", terminalID='" + terminalID + '\'' +
                ", username='" + username + '\'' +
                ", user_id=" + user_id +
                ", picture='" + picture + '\'' +
                '}';
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LocationListBean(String address, String carNumber, String createTime, LocationListBean.device device, boolean expire, int id, LocationListBean.location location, String nickname, int role, int state, String telephone, String terminalID, String username, int user_id, String picture) {
        this.address = address;
        this.carNumber = carNumber;
        this.createTime = createTime;
        this.device = device;
        this.expire = expire;
        this.id = id;
        this.location = location;
        this.nickname = nickname;
        this.role = role;
        this.state = state;
        this.telephone = telephone;
        this.terminalID = terminalID;
        this.username = username;
        this.user_id = user_id;
        this.picture = picture;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public boolean isExpire() {
        return expire;
    }

    public void setExpire(boolean expire) {
        this.expire = expire;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public device getDevice() {
        return device;
    }

    public void setDevice(device device) {
        this.device = device;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public location getLocation() {
        return location;
    }

    public void setLocation(location location) {
        this.location = location;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocationListBean() {
    }

    public LocationListBean(String address, String carNumber, String createTime, device device, int id, LocationListBean.location location, String nickname, int state, String telephone, String terminalID, String username) {
        this.address = address;
        this.carNumber = carNumber;
        this.createTime = createTime;
        this.device = device;
        this.id = id;
        this.location = location;
        this.nickname = nickname;
        this.state = state;
        this.telephone = telephone;
        this.terminalID = terminalID;
        this.username = username;
    }

    public static class location implements Serializable{
        private float voltage = -1f;
        private boolean acc;
        private double bdLat;
        private double bdLon;
        private int course;
        private String deviceNumber;
        private int deviceProtocol;
        private float electric;
        private int gsm;
        private String networkProtocol;
        private int speed;
        private long stateTime;
        private long utcTime;
        private String locationType;

        public boolean isAcc() {
            return acc;
        }

        public void setAcc(boolean acc) {
            this.acc = acc;
        }

        public String getLocationType() {
            return locationType;
        }

        public void setLocationType(String locationType) {
            this.locationType = locationType;
        }

        public float getVoltage() {
            return voltage;
        }

        public void setVoltage(float voltage) {
            this.voltage = voltage;
        }

        public location(float voltage, boolean acc, double bdLat, double bdLon, int course, String deviceNumber, int deviceProtocol, float electric, int gsm, String networkProtocol, int speed, long stateTime, long utcTime, String locationType) {
            this.voltage = voltage;
            this.acc = acc;
            this.bdLat = bdLat;
            this.bdLon = bdLon;
            this.course = course;
            this.deviceNumber = deviceNumber;
            this.deviceProtocol = deviceProtocol;
            this.electric = electric;
            this.gsm = gsm;
            this.networkProtocol = networkProtocol;
            this.speed = speed;
            this.stateTime = stateTime;
            this.utcTime = utcTime;
            this.locationType = locationType;
        }

        @Override
        public String toString() {
            return "location{" +
                    "voltage=" + voltage +
                    ", acc=" + acc +
                    ", bdLat=" + bdLat +
                    ", bdLon=" + bdLon +
                    ", course=" + course +
                    ", deviceNumber='" + deviceNumber + '\'' +
                    ", deviceProtocol=" + deviceProtocol +
                    ", electric=" + electric +
                    ", gsm=" + gsm +
                    ", networkProtocol='" + networkProtocol + '\'' +
                    ", speed=" + speed +
                    ", stateTime=" + stateTime +
                    ", utcTime=" + utcTime +
                    ", locationType='" + locationType + '\'' +
                    '}';
        }



        public double getBdLat() {
            return bdLat;
        }

        public void setBdLat(double bdLat) {
            this.bdLat = bdLat;
        }

        public double getBdLon() {
            return bdLon;
        }

        public void setBdLon(double bdLon) {
            this.bdLon = bdLon;
        }

        public int getCourse() {
            return course;
        }

        public void setCourse(int course) {
            this.course = course;
        }

        public String getDeviceNumber() {
            return deviceNumber;
        }

        public void setDeviceNumber(String deviceNumber) {
            this.deviceNumber = deviceNumber;
        }

        public int getDeviceProtocol() {
            return deviceProtocol;
        }

        public void setDeviceProtocol(int deviceProtocol) {
            this.deviceProtocol = deviceProtocol;
        }

        public float getElectric() {
            return electric;
        }

        public void setElectric(float electric) {
            this.electric = electric;
        }

        public String getNetworkProtocol() {
            return networkProtocol;
        }

        public void setNetworkProtocol(String networkProtocol) {
            this.networkProtocol = networkProtocol;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public long getStateTime() {
            return stateTime;
        }

        public void setStateTime(long stateTime) {
            this.stateTime = stateTime;
        }

        public long getUtcTime() {
            return utcTime;
        }

        public void setUtcTime(long utcTime) {
            this.utcTime = utcTime;
        }

        public int getGsm() {
            return gsm;
        }

        public void setGsm(int gsm) {
            this.gsm = gsm;
        }

        public location() {
        }
    }
    public static class device implements Serializable {
        private String createTime;
        private String deviceNumber;
        private String deviceType;
        private String deviceId;
        private String expirationTime;
        private String hireExpirationTime;
        private String terminalID;
        private int user_id;
        private int iconType;

        @Override
        public String toString() {
            return "device{" +
                    "createTime='" + createTime + '\'' +
                    ", deviceNumber='" + deviceNumber + '\'' +
                    ", deviceType='" + deviceType + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", expirationTime='" + expirationTime + '\'' +
                    ", hireExpirationTime='" + hireExpirationTime + '\'' +
                    ", terminalID='" + terminalID + '\'' +
                    ", user_id=" + user_id +
                    ", iconType=" + iconType +
                    '}';
        }

        public int getIconType() {
            return iconType;
        }

        public void setIconType(int iconType) {
            this.iconType = iconType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDeviceNumber() {
            return deviceNumber;
        }

        public void setDeviceNumber(String deviceNumber) {
            this.deviceNumber = deviceNumber;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }


        public String getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(String expirationTime) {
            this.expirationTime = expirationTime;
        }

        public String getHireExpirationTime() {
            return hireExpirationTime;
        }

        public void setHireExpirationTime(String hireExpirationTime) {
            this.hireExpirationTime = hireExpirationTime;
        }


        public String getTerminalID() {
            return terminalID;
        }

        public void setTerminalID(String terminalID) {
            this.terminalID = terminalID;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public device() {
        }

        public device(String createTime, String deviceNumber, String deviceType, String expirationTime, String hireExpirationTime, String terminalID, int user_id, int iconType) {
            this.createTime = createTime;
            this.deviceNumber = deviceNumber;
            this.deviceType = deviceType;
            this.expirationTime = expirationTime;
            this.hireExpirationTime = hireExpirationTime;
            this.terminalID = terminalID;
            this.user_id = user_id;
            this.iconType = iconType;
        }
    }
}
