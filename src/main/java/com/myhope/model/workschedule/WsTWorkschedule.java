package com.myhope.model.workschedule;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.myhope.model.base.TUser;

@Entity
@Table(name = "WS_T_WORKSCHEDULE", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class WsTWorkschedule implements java.io.Serializable {

	private String id;
	private Date date;
	private String schedule;
	private TUser user = new TUser();

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

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE", length = 10)
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "schedule", length = 40)
	public String getSchedule() {
		return this.schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	@ManyToOne()
	@JoinColumn(name = "USER_ID", nullable = false)
	public TUser getUser() {
		return this.user;
	}

	public void setUser(TUser user) {
		this.user = user;
	}
}