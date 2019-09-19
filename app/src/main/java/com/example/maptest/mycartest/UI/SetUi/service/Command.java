package com.example.maptest.mycartest.UI.SetUi.service;

import java.io.Serializable;
import java.util.Date;

/** 
* @ClassName: Command 
* @Description: 指令记录
* @author 兰森
* @date 2018年2月28日 下午3:54:01 
*  
*/

public class Command implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String id ;

	private String terminalID ; //设备IMEI号

	private String content ; //指令内容

	private String agentName = ""; //下发代理商名称

	private String backContent = ""; //回复内容

	private Long issTime = new Date().getTime(); //下发时间

	private Long excuteTime = new Date().getTime(); //执行时间

	private Date expireTime = new Date();
	
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
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getBackContent() {
		return backContent;
	}
	
	public void setBackContent(String backContent) {
		this.backContent = backContent;
	}
	
	public Long getIssTime() {
		return issTime;
	}
	
	public void setIssTime(Long issTime) {
		this.issTime = issTime;
	}
	
	public Long getExcuteTime() {
		return excuteTime;
	}
	
	public void setExcuteTime(Long excuteTime) {
		this.excuteTime = excuteTime;
	}
	
	public Date getExpireTime() {
		return expireTime;
	}
	
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "Command [id=" + id + ", terminalID=" + terminalID + ", content=" + content + ", agentName=" + agentName
				+ ", backContent=" + backContent + ", issTime=" + issTime + ", excuteTime=" + excuteTime
				+ ", expireTime=" + expireTime + "]";
	}

	public Command() {
	}

	public Command(String id, String terminalID, String content, String agentName, String backContent, Long issTime, Long excuteTime, Date expireTime) {
		this.id = id;
		this.terminalID = terminalID;
		this.content = content;
		this.agentName = agentName;
		this.backContent = backContent;
		this.issTime = issTime;
		this.excuteTime = excuteTime;
		this.expireTime = expireTime;
	}


}
