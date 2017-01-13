package com.myhope.model.workschedule;

import java.util.Date;

public class WsAttendance {
	private Date normalDate;// 标准打卡时间
//	private String isDo;// 是否进行了打卡 Y:是；N:否
	private Date doDate;// 打卡时间
	private String type;// 打卡类型 A:上班；B:下班；C:中间休息开始；D:中间休息结束
	private Long wrongTime;// 错误时间（毫秒）0为正常； A:上班迟到时间；B:下班早退时间；C:中间休息开始早退时间；D:中间休息结束迟到时间

	public Date getNormalDate() {
		return normalDate;
	}

	public void setNormalDate(Date normalDate) {
		this.normalDate = normalDate;
	}

//	public String getIsDo() {
//		return isDo;
//	}
//
//	public void setIsDo(String isDo) {
//		this.isDo = isDo;
//	}

	public Date getDoDate() {
		return doDate;
	}

	public void setDoDate(Date doDate) {
		this.doDate = doDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getWrongTime() {
		return wrongTime;
	}

	public void setWrongTime(Long wrongTime) {
		this.wrongTime = wrongTime;
	}

}
