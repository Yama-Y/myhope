package com.myhope.model.workschedule;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.myhope.model.base.TOrganization;
import com.myhope.util.base.DateUtil;

@Entity
@Table(name = "WS_T_SCHEDULE", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class WsTSchedule implements java.io.Serializable {

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String name;
	private String remark;
	private String type;// 类型：A：工作班次；B：休假班次
	private String color;
	private Integer scheduleDetailsSize;// 规则数
	private Double workTime; // 工作时间（下班时间与上班时间差。如果有中间休息，则去除休息时间。）
	private Set<WsTScheduleDetail> scheduleDetails = new HashSet<WsTScheduleDetail>(0);
	private Set<TOrganization> organizations = new HashSet<TOrganization>(0);

	@Id
	@Column(name = "ID", unique = true, nullable = false, length = 36)
	public String getId() {
		if (!StringUtils.isBlank(this.id)) {
			return this.id;
		}
		return UUID.randomUUID().toString();
	}

	public void setId(String id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDATETIME", length = 7)
	public Date getCreatedatetime() {
		if (this.createdatetime != null)
			return this.createdatetime;
		return new Date();
	}

	public void setCreatedatetime(Date createdatetime) {
		this.createdatetime = createdatetime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATEDATETIME", length = 7)
	public Date getUpdatedatetime() {
		if (this.updatedatetime != null)
			return this.updatedatetime;
		return new Date();
	}

	public void setUpdatedatetime(Date updatedatetime) {
		this.updatedatetime = updatedatetime;
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "REMARK", length = 300)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "TYPE", length = 1)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "COLOR", length = 7)
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@OneToMany(mappedBy = "schedule", cascade = { CascadeType.ALL })
	public Set<WsTScheduleDetail> getScheduleDetails() {
		return scheduleDetails;
	}

	public void setScheduleDetails(Set<WsTScheduleDetail> scheduleDetails) {
		this.scheduleDetails = scheduleDetails;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "WS_ORGANIZATIONS_SCHEDULE", schema = "", joinColumns = { @JoinColumn(name = "SCHEDULE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ORGANIZATIONS_ID", nullable = false, updatable = false) })
	public Set<TOrganization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(Set<TOrganization> organizations) {
		this.organizations = organizations;
	}

	@Transient
	public Integer getScheduleDetailsSize() {
		scheduleDetailsSize = 0;
		for (WsTScheduleDetail scheduleDetail : scheduleDetails) {
			if ("A".equals(scheduleDetail.getIsCard())) {
				scheduleDetailsSize++;
			}
		}
		return scheduleDetailsSize;
	}

	@Transient
	public Double getWorkTime() {
		Long beginTime = 0L;
		Long endTime = 0L;
		Long c_beginTime = 0L;
		Long c_endTime = 0L;
		String dateString = null;
		String cardType = null;
		for (WsTScheduleDetail scheduleDetail : scheduleDetails) {
			cardType = scheduleDetail.getCardType();
			if ("A".equals(scheduleDetail.getTimeType())) {
				dateString = "1970-01-01 ";
			} else if ("B".equals(scheduleDetail.getTimeType())) {
				dateString = "1970-01-02 ";
			}
			if ("A".equals(cardType)) {
				beginTime = DateUtil.stringToDate(dateString + scheduleDetail.getTime(), "yyyy-MM-dd HH:mm:ss").getTime();
			} else if ("B".equals(cardType)) {
				endTime = DateUtil.stringToDate(dateString + scheduleDetail.getTime(), "yyyy-MM-dd HH:mm:ss").getTime();
			} else if ("C".equals(cardType)) {
				c_beginTime = DateUtil.stringToDate(dateString + scheduleDetail.getTime(), "yyyy-MM-dd HH:mm:ss").getTime();
			} else if ("D".equals(cardType)) {
				c_endTime = DateUtil.stringToDate(dateString + scheduleDetail.getTime(), "yyyy-MM-dd HH:mm:ss").getTime();
			}
		}
		Long workTimeL = (endTime - c_endTime) + (c_beginTime - beginTime);
		if (workTimeL < 0) {
			workTimeL = 0L;
		}
		workTime = ((double) (workTimeL)) / (1000 * 60 * 60);

		return workTime;
	}

}