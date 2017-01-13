package com.myhope.model.workschedule;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "WS_T_ATTENDANCE")
public class WsTAttendance implements java.io.Serializable {

	// Fields

	private String id;
	private String wcode;
	private String name;
	private Date time;
	private String cardSrc;

	// Property accessors
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

	@Column(name = "WCODE", length = 30)
	public String getWcode() {
		return this.wcode;
	}

	public void setWcode(String wcode) {
		this.wcode = wcode;
	}

	@Column(name = "NAME", length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TIME", length = 19)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Column(name = "CARD_SRC", length = 15)
	public String getCardSrc() {
		return this.cardSrc;
	}

	public void setCardSrc(String cardSrc) {
		this.cardSrc = cardSrc;
	}

}