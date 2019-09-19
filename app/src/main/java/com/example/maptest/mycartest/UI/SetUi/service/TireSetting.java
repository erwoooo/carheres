package com.example.maptest.mycartest.UI.SetUi.service;

/** 
* @ClassName: TireSetting 
* @Description: 胎压指令
* @author 兰森
* @date 2018年3月17日 下午4:14:06 
*  
*/
public class TireSetting {
	
	private int temperature ; //温度
	
	private float no1threshold ; //阀值

	private float no2threshold ; //阀值

	private float no3threshold ; //阀值

	private float no4threshold ; //阀值

	@Override
	public String toString() {
		return "TireSetting{" +
				"temperature=" + temperature +
				", no1threshold=" + no1threshold +
				", no2threshold=" + no2threshold +
				", no3threshold=" + no3threshold +
				", no4threshold=" + no4threshold +
				'}';
	}

	public TireSetting() {
	}

	public TireSetting(int temperature, float no1threshold, float no2threshold, float no3threshold, float no4threshold) {
		this.temperature = temperature;
		this.no1threshold = no1threshold;
		this.no2threshold = no2threshold;
		this.no3threshold = no3threshold;
		this.no4threshold = no4threshold;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public float getNo1threshold() {
		return no1threshold;
	}

	public void setNo1threshold(float no1threshold) {
		this.no1threshold = no1threshold;
	}

	public float getNo2threshold() {
		return no2threshold;
	}

	public void setNo2threshold(float no2threshold) {
		this.no2threshold = no2threshold;
	}

	public float getNo3threshold() {
		return no3threshold;
	}

	public void setNo3threshold(float no3threshold) {
		this.no3threshold = no3threshold;
	}

	public float getNo4threshold() {
		return no4threshold;
	}

	public void setNo4threshold(float no4threshold) {
		this.no4threshold = no4threshold;
	}
}
