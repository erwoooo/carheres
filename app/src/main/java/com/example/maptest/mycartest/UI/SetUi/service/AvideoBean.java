package com.example.maptest.mycartest.UI.SetUi.service;


import java.util.Date;


/**
* @ClassName: Video 
* @Description: 影音文件存储表
* @author 兰森
* @date 2018年6月30日 上午11:26:17 
*
 */

public class AvideoBean {

	private String id ;

	private String terminalID ; //设备号

	private String fileName ; //文件名

	private String source ; //文件上传的路径地址

	private Boolean isRead ; //是否查看

	private Long utcTime ; //上传时间

	private Date expireTime ; //文件过期时间

	private boolean select = false;

	private boolean click = false;

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public Boolean getRead() {
		return isRead;
	}

	public void setRead(Boolean read) {
		isRead = read;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public Long getUtcTime() {
		return utcTime;
	}

	public void setUtcTime(Long utcTime) {
		this.utcTime = utcTime;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Override
	public String toString() {
		return "AvideoBean{" +
				"id='" + id + '\'' +
				", terminalID='" + terminalID + '\'' +
				", fileName='" + fileName + '\'' +
				", source='" + source + '\'' +
				", isRead=" + isRead +
				", utcTime=" + utcTime +
				", expireTime=" + expireTime +
				", select=" + select +
				'}';
	}


}
