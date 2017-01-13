package com.myhope.model.workcalendar;

import java.util.Date;

import com.myhope.util.base.DateUtil;

public class WcWorkXls {

	private Integer headerRow = 1;
	private Integer headerCell = 0;
	private String deptName;
	private String writeName;
	private String readName;
	private String date;
	private String headerValue;

	private Integer planRow = 5;
	private Integer planCell = 1;//11

	private Integer workRow = 12;
	private Integer workCell = 3;//11

	public Integer getHeaderRow() {
		return headerRow;
	}

	public void setHeaderRow(Integer headerRow) {
		this.headerRow = headerRow;
	}

	public Integer getHeaderCell() {
		return headerCell;
	}

	public void setHeaderCell(Integer headerCell) {
		this.headerCell = headerCell;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = "部门：" + deptName;
	}

	public String getWriteName() {
		return writeName;
	}

	public void setWriteName(String writeName) {
		this.writeName = "报告填写人：" + writeName;
	}

	public String getReadName() {
		return readName;
	}

	public void setReadName(String readName) {
		this.readName = "审阅人：" + readName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		Date nowDate = DateUtil.stringToDate(date, "yyyy-MM-dd");
		Date beginDate = DateUtil.getWeekBegin(nowDate);
		Date endDate = DateUtil.getWeekBegin(nowDate, 4);
		Integer weedNumber = DateUtil.getWeekNumber(beginDate);
		this.date = "时间： " + DateUtil.dateToString(beginDate, "yyyy年MM月") + " 第 " + weedNumber + " 周（"
				+ DateUtil.dateToString(beginDate, "yyyy年MM月dd日") + "－" + DateUtil.dateToString(endDate, "yyyy年MM月dd日") + "）";
	}

	public String getHeaderValue() {
		headerValue = deptName + "    " + writeName + "    " + date;
		return headerValue;
	}

	public void setHeaderValue(String headerValue) {
		this.headerValue = headerValue;
	}

	public Integer getPlanRow() {
		return planRow;
	}

	public void setPlanRow(Integer planRow) {
		this.planRow = planRow;
	}

	public Integer getPlanCell() {
		return planCell;
	}

	public void setPlanCell(Integer planCell) {
		this.planCell = planCell;
	}

	public Integer getWorkRow() {
		return workRow;
	}

	public void setWorkRow(Integer workRow) {
		this.workRow = workRow;
	}

	public Integer getWorkCell() {
		return workCell;
	}

	public void setWorkCell(Integer workCell) {
		this.workCell = workCell;
	}

}