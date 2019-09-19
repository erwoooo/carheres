package com.example.maptest.mycartest.Entity;

/**
 * Created by ${Author} on 2018/8/30.
 * Use to
 */

public class EightOrderBean {
    private String id ;

    private String terminalID = "";//设备终端号

    private String sos1 = "";//SOS号码

    private String sos2 = "" ;

    private String sos3 = "";

    private String center = "";//中心号码

    private boolean vibration = false;//震动报警状态

    private int vibrationType = 0 ;//震动报警类型

    private boolean cutElectric = false;//断电报警状态

    private int cutElectricType = 0;//断电报警类型

    private boolean displacement = false;//位移报警类型

    private int displacementLimit = 0 ;//位移范围

    private int displacementType = 0;//位移报警类型

    private boolean cutOff = false;//断油电状态

    private boolean speedLimit = false;//超速报警状态

    private int speedLimitType = 0 ;//超速报警类型

    private int limitTime = 0;//超速时间

    private int limitSpeed = 0;//超速范围

    private String fenceName = "" ;//围栏名称

    private int fenceLimit = 0 ;//围栏范围

    private int fenceModel =0;//围栏模式

    private double fenceLat =0d ;//围栏纬度

    private double fenceLon =0d;//围栏经度

    private int fenceAlarmType =0 ;//围栏报警类型

    private boolean fenceState  = false;//围栏状态

    private Long fenceSetTime ;//围栏设置时间

    private int mileage = 100; //行驶里程数

    private int deviceProtocol;

    public EightOrderBean() {
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

    public String getSos1() {
        return sos1;
    }

    public void setSos1(String sos1) {
        this.sos1 = sos1;
    }

    public String getSos2() {
        return sos2;
    }

    public void setSos2(String sos2) {
        this.sos2 = sos2;
    }

    public String getSos3() {
        return sos3;
    }

    public void setSos3(String sos3) {
        this.sos3 = sos3;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public boolean isVibration() {
        return vibration;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public int getVibrationType() {
        return vibrationType;
    }

    public void setVibrationType(int vibrationType) {
        this.vibrationType = vibrationType;
    }

    public boolean isCutElectric() {
        return cutElectric;
    }

    public void setCutElectric(boolean cutElectric) {
        this.cutElectric = cutElectric;
    }

    public int getCutElectricType() {
        return cutElectricType;
    }

    public void setCutElectricType(int cutElectricType) {
        this.cutElectricType = cutElectricType;
    }

    public boolean isDisplacement() {
        return displacement;
    }

    public void setDisplacement(boolean displacement) {
        this.displacement = displacement;
    }

    public int getDisplacementLimit() {
        return displacementLimit;
    }

    public void setDisplacementLimit(int displacementLimit) {
        this.displacementLimit = displacementLimit;
    }

    public int getDisplacementType() {
        return displacementType;
    }

    public void setDisplacementType(int displacementType) {
        this.displacementType = displacementType;
    }

    public boolean isCutOff() {
        return cutOff;
    }

    public void setCutOff(boolean cutOff) {
        this.cutOff = cutOff;
    }

    public boolean isSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(boolean speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getSpeedLimitType() {
        return speedLimitType;
    }

    public void setSpeedLimitType(int speedLimitType) {
        this.speedLimitType = speedLimitType;
    }

    public int getLimitTime() {
        return limitTime;
    }

    public void setLimitTime(int limitTime) {
        this.limitTime = limitTime;
    }

    public int getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(int limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public int getFenceLimit() {
        return fenceLimit;
    }

    public void setFenceLimit(int fenceLimit) {
        this.fenceLimit = fenceLimit;
    }

    public int getFenceModel() {
        return fenceModel;
    }

    public void setFenceModel(int fenceModel) {
        this.fenceModel = fenceModel;
    }

    public double getFenceLat() {
        return fenceLat;
    }

    public void setFenceLat(double fenceLat) {
        this.fenceLat = fenceLat;
    }

    public double getFenceLon() {
        return fenceLon;
    }

    public void setFenceLon(double fenceLon) {
        this.fenceLon = fenceLon;
    }

    public int getFenceAlarmType() {
        return fenceAlarmType;
    }

    public void setFenceAlarmType(int fenceAlarmType) {
        this.fenceAlarmType = fenceAlarmType;
    }

    public boolean isFenceState() {
        return fenceState;
    }

    public void setFenceState(boolean fenceState) {
        this.fenceState = fenceState;
    }

    public Long getFenceSetTime() {
        return fenceSetTime;
    }

    public void setFenceSetTime(Long fenceSetTime) {
        this.fenceSetTime = fenceSetTime;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getDeviceProtocol() {
        return deviceProtocol;
    }

    public void setDeviceProtocol(int deviceProtocol) {
        this.deviceProtocol = deviceProtocol;
    }

    @Override
    public String toString() {
        return "EightOrderBean{" +
                "id='" + id + '\'' +
                ", terminalID='" + terminalID + '\'' +
                ", sos1='" + sos1 + '\'' +
                ", sos2='" + sos2 + '\'' +
                ", sos3='" + sos3 + '\'' +
                ", center='" + center + '\'' +
                ", vibration=" + vibration +
                ", vibrationType=" + vibrationType +
                ", cutElectric=" + cutElectric +
                ", cutElectricType=" + cutElectricType +
                ", displacement=" + displacement +
                ", displacementLimit=" + displacementLimit +
                ", displacementType=" + displacementType +
                ", cutOff=" + cutOff +
                ", speedLimit=" + speedLimit +
                ", speedLimitType=" + speedLimitType +
                ", limitTime=" + limitTime +
                ", limitSpeed=" + limitSpeed +
                ", fenceName='" + fenceName + '\'' +
                ", fenceLimit=" + fenceLimit +
                ", fenceModel=" + fenceModel +
                ", fenceLat=" + fenceLat +
                ", fenceLon=" + fenceLon +
                ", fenceAlarmType=" + fenceAlarmType +
                ", fenceState=" + fenceState +
                ", fenceSetTime=" + fenceSetTime +
                ", mileage=" + mileage +
                ", deviceProtocol=" + deviceProtocol +
                '}';
    }

    public EightOrderBean(String id, String terminalID, String sos1, String sos2, String sos3, String center, boolean vibration, int vibrationType, boolean cutElectric, int cutElectricType, boolean displacement, int displacementLimit, int displacementType, boolean cutOff, boolean speedLimit, int speedLimitType, int limitTime, int limitSpeed, String fenceName, int fenceLimit, int fenceModel, double fenceLat, double fenceLon, int fenceAlarmType, boolean fenceState, Long fenceSetTime, int mileage, int deviceProtocol) {
        this.id = id;
        this.terminalID = terminalID;
        this.sos1 = sos1;
        this.sos2 = sos2;
        this.sos3 = sos3;
        this.center = center;
        this.vibration = vibration;
        this.vibrationType = vibrationType;
        this.cutElectric = cutElectric;
        this.cutElectricType = cutElectricType;
        this.displacement = displacement;
        this.displacementLimit = displacementLimit;
        this.displacementType = displacementType;
        this.cutOff = cutOff;
        this.speedLimit = speedLimit;
        this.speedLimitType = speedLimitType;
        this.limitTime = limitTime;
        this.limitSpeed = limitSpeed;
        this.fenceName = fenceName;
        this.fenceLimit = fenceLimit;
        this.fenceModel = fenceModel;
        this.fenceLat = fenceLat;
        this.fenceLon = fenceLon;
        this.fenceAlarmType = fenceAlarmType;
        this.fenceState = fenceState;
        this.fenceSetTime = fenceSetTime;
        this.mileage = mileage;
        this.deviceProtocol = deviceProtocol;
    }
}
