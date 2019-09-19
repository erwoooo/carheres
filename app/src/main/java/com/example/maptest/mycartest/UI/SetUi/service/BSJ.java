package com.example.maptest.mycartest.UI.SetUi.service;

import java.io.Serializable;
import java.util.Arrays;


public class BSJ implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int deviceProtocol;
	private String id ;

	private String terminalID ;//设备终端号

	private Boolean clockState =false;//是否开启闹钟模式

	private  String[] groupState ;//闹钟选项
	
	private Boolean timingState =false;//定时模式是否开启
	
	private int timingValue =0; //定时时间


	private Boolean[] weekState ;
	
	private String weekValue ="";

	public int getDeviceProtocol() {
		return deviceProtocol;
	}

	public void setDeviceProtocol(int deviceProtocol) {
		this.deviceProtocol = deviceProtocol;
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

	public Boolean getClockState() {
		return clockState;
	}

	public void setClockState(Boolean clockState) {
		this.clockState = clockState;
	}

	public String[] getGroupState() {
		return groupState;
	}

	public void setGroupState(String[] groupState) {
		this.groupState = groupState;
	}

	public Boolean[] getWeekState() {
		return weekState;
	}

	public void setWeekState(Boolean[] weekState) {
		this.weekState = weekState;
	}

	public Boolean getTimingState() {
		return timingState;
	}

	public void setTimingState(Boolean timingState) {
		this.timingState = timingState;
	}

	public int getTimingValue() {
		return timingValue;
	}

	public void setTimingValue(int timingValue) {
		this.timingValue = timingValue;
	}

	public String getWeekValue() {
		return weekValue;
	}

	public void setWeekValue(String weekValue) {
		this.weekValue = weekValue;
	}

	public BSJ(int deviceProtocol, String id, String terminalID, Boolean clockState, String[] groupState, Boolean timingState, int timingValue, Boolean[] weekState, String weekValue) {
		this.deviceProtocol = deviceProtocol;
		this.id = id;
		this.terminalID = terminalID;
		this.clockState = clockState;
		this.groupState = groupState;
		this.timingState = timingState;
		this.timingValue = timingValue;
		this.weekState = weekState;
		this.weekValue = weekValue;
	}

	@Override
	public String toString() {
		return "BSJ{" +
				"deviceProtocol=" + deviceProtocol +
				", id='" + id + '\'' +
				", terminalID='" + terminalID + '\'' +
				", clockState=" + clockState +
				", groupState=" + Arrays.toString(groupState) +
				", timingState=" + timingState +
				", timingValue=" + timingValue +
				", weekState=" + Arrays.toString(weekState) +
				", weekValue='" + weekValue + '\'' +
				'}';
	}

	public BSJ() {
	}
}
