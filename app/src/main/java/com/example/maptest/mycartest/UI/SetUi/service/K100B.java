package com.example.maptest.mycartest.UI.SetUi.service;

import java.io.Serializable;

/**
 * @ClassName: Setting
 * @Description: 指令设置
 * @author 兰森
 * @date 2018年2月28日 下午3:54:56
 *
 */

public class K100B implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id ;

	private String terminalID ;//设备终端号

	private String sos1 = "";//SOS号码

	private String sos2 = "" ;

	private String sos3 = "";

	private int deviceProtocol;

	private String center = "";//中心号码

	private boolean vibration = false;//震动报警状态

	private int vibrationType = 0 ;//震动报警类型

	private boolean cutElectric = false;//断电报警状态

	private int cutElectricType = 0;//断电报警类型

	private boolean lowElectric = false;//低电报警状态

	private int lowElectricType = 0;//低电报警类型

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

	private boolean dismantle = false; //拆机报警状态

	private int dismantleType = 0 ; //拆机报警类型

	private int sensitiveLevel = 0; //灵敏度级别

	private int workPattern = 0 ;//工作模式 ： 0：智能省电模式，1：间隔工作模式 ，2 ， 休眠工作模式；

	private int intervalTime = 0 ; //间隔时间 ， 间隔工作模式。时间单位为分钟，范围5-1440； 休眠工作模式。时间单位为小时，范围（1-2376）；

	private Boolean defense = false ;

	private Boolean powerSwitch = false;

	private String timerCut;

	private TireSetting tireSetting ;

	private Offline offline ;


	private OTA ota ;

	public K100B(String id, String terminalID, String sos1, String sos2, String sos3, int deviceProtocol, String center, boolean vibration, int vibrationType, boolean cutElectric, int cutElectricType, boolean lowElectric, int lowElectricType, boolean displacement, int displacementLimit, int displacementType, boolean cutOff, boolean speedLimit, int speedLimitType, int limitTime, int limitSpeed, String fenceName, int fenceLimit, int fenceModel, double fenceLat, double fenceLon, int fenceAlarmType, boolean fenceState, Long fenceSetTime, boolean dismantle, int dismantleType, int sensitiveLevel, int workPattern, int intervalTime, Boolean defense, Boolean powerSwitch, String timerCut, TireSetting tireSetting, Offline offline, OTA ota) {
		this.id = id;
		this.terminalID = terminalID;
		this.sos1 = sos1;
		this.sos2 = sos2;
		this.sos3 = sos3;
		this.deviceProtocol = deviceProtocol;
		this.center = center;
		this.vibration = vibration;
		this.vibrationType = vibrationType;
		this.cutElectric = cutElectric;
		this.cutElectricType = cutElectricType;
		this.lowElectric = lowElectric;
		this.lowElectricType = lowElectricType;
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
		this.dismantle = dismantle;
		this.dismantleType = dismantleType;
		this.sensitiveLevel = sensitiveLevel;
		this.workPattern = workPattern;
		this.intervalTime = intervalTime;
		this.defense = defense;
		this.powerSwitch = powerSwitch;
		this.timerCut = timerCut;
		this.tireSetting = tireSetting;
		this.offline = offline;
		this.ota = ota;
	}

	public K100B() {
	}

	@Override
	public String toString() {
		return "K100B{" +
				"id='" + id + '\'' +
				", terminalID='" + terminalID + '\'' +
				", sos1='" + sos1 + '\'' +
				", sos2='" + sos2 + '\'' +
				", sos3='" + sos3 + '\'' +
				", deviceProtocol=" + deviceProtocol +
				", center='" + center + '\'' +
				", vibration=" + vibration +
				", vibrationType=" + vibrationType +
				", cutElectric=" + cutElectric +
				", cutElectricType=" + cutElectricType +
				", lowElectric=" + lowElectric +
				", lowElectricType=" + lowElectricType +
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
				", dismantle=" + dismantle +
				", dismantleType=" + dismantleType +
				", sensitiveLevel=" + sensitiveLevel +
				", workPattern=" + workPattern +
				", intervalTime=" + intervalTime +
				", defense=" + defense +
				", powerSwitch=" + powerSwitch +
				", timerCut='" + timerCut + '\'' +
				", tireSetting=" + tireSetting +
				", offline=" + offline +
				", ota=" + ota +
				'}';
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Boolean getPowerSwitch() {
		return powerSwitch;
	}

	public void setPowerSwitch(Boolean powerSwitch) {
		this.powerSwitch = powerSwitch;
	}

	public String getTimerCut() {
		return timerCut;
	}

	public void setTimerCut(String timerCut) {
		this.timerCut = timerCut;
	}

	public int getDeviceProtocol() {
		return deviceProtocol;
	}

	public void setDeviceProtocol(int deviceProtocol) {
		this.deviceProtocol = deviceProtocol;
	}

	public boolean isVibration() {
		return vibration;
	}

	public boolean isCutElectric() {
		return cutElectric;
	}

	public boolean isLowElectric() {
		return lowElectric;
	}

	public boolean isCutOff() {
		return cutOff;
	}

	public boolean isDisplacement() {
		return displacement;
	}

	public boolean isSpeedLimit() {
		return speedLimit;
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

	public Boolean getVibration() {
		return vibration;
	}

	public void setVibration(Boolean vibration) {
		this.vibration = vibration;
	}

	public int getVibrationType() {
		return vibrationType;
	}

	public void setVibrationType(int vibrationType) {
		this.vibrationType = vibrationType;
	}
	public Boolean getCutElectric() {
		return cutElectric;
	}
	public void setCutElectric(Boolean cutElectric) {
		this.cutElectric = cutElectric;
	}
	public int getCutElectricType() {
		return cutElectricType;
	}
	public void setCutElectricType(int cutElectricType) {
		this.cutElectricType = cutElectricType;
	}
	public Boolean getLowElectric() {
		return lowElectric;
	}
	public void setLowElectric(Boolean lowElectric) {
		this.lowElectric = lowElectric;
	}
	public int getLowElectricType() {
		return lowElectricType;
	}
	public void setLowElectricType(int lowElectricType) {
		this.lowElectricType = lowElectricType;
	}
	public Boolean getDisplacement() {
		return displacement;
	}
	public void setDisplacement(Boolean displacement) {
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
	public Boolean getCutOff() {
		return cutOff;
	}
	public void setCutOff(Boolean cutOff) {
		this.cutOff = cutOff;
	}
	public Boolean getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(Boolean speedLimit) {
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
	public TireSetting getTireSetting() {
		return tireSetting;
	}
	public void setTireSetting(TireSetting tireSetting) {
		this.tireSetting = tireSetting;
	}
	public Offline getOffline() {
		return offline;
	}
	public void setOffline(Offline offline) {
		this.offline = offline;
	}
	public OTA getOta() {
		return ota;
	}
	public void setOta(OTA ota) {
		this.ota = ota;
	}

	public boolean isDismantle() {
		return dismantle;
	}

	public void setDismantle(boolean dismantle) {
		this.dismantle = dismantle;
	}

	public int getDismantleType() {
		return dismantleType;
	}

	public void setDismantleType(int dismantleType) {
		this.dismantleType = dismantleType;
	}

	public int getSensitiveLevel() {
		return sensitiveLevel;
	}

	public void setSensitiveLevel(int sensitiveLevel) {
		this.sensitiveLevel = sensitiveLevel;
	}

	public int getWorkPattern() {
		return workPattern;
	}

	public void setWorkPattern(int workPattern) {
		this.workPattern = workPattern;
	}

	public int getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public void setVibration(boolean vibration) {
		this.vibration = vibration;
	}

	public void setCutElectric(boolean cutElectric) {
		this.cutElectric = cutElectric;
	}

	public void setLowElectric(boolean lowElectric) {
		this.lowElectric = lowElectric;
	}

	public void setDisplacement(boolean displacement) {
		this.displacement = displacement;
	}

	public void setCutOff(boolean cutOff) {
		this.cutOff = cutOff;
	}

	public void setSpeedLimit(boolean speedLimit) {
		this.speedLimit = speedLimit;
	}

	public Boolean getDefense() {
		return defense;
	}

	public void setDefense(Boolean defense) {
		this.defense = defense;
	}


}
