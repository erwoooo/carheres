package com.example.maptest.mycartest.UI.SetUi.service;


/** 
* @ClassName: Offline 
* @Description: 离线指令
* @author 兰森
* @date 2018年3月17日 下午4:40:41 
*  
*/
public class Offline {
	
	
	private Boolean cutoffState = false;
	
	private int modelState = 0;
	
	private String interval = "12:00" ;
	
	private int time = 0 ; 

	public Boolean getCutoffState() {
		return cutoffState;
	}

	public void setCutoffState(Boolean cutoffState) {
		this.cutoffState = cutoffState;
	}

	public int getModelState() {
		return modelState;
	}

	public void setModelState(int modelState) {
		this.modelState = modelState;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Offline [cutoffState=" + cutoffState + ", modelState=" + modelState + ", interval=" + interval
				+ ", time=" + time + "]";
	}

	
	
	
}
