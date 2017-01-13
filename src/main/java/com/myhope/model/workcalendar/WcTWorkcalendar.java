package com.myhope.model.workcalendar;

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
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.myhope.model.base.TUser;
import com.myhope.model.project.PrTProject;

@Entity
@Table(name = "WC_T_WORKCALENDAR", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class WcTWorkcalendar implements java.io.Serializable {

	private String id;
	private String type;// 种类：A：记录；B：计划
	private String title;
	private String content;
	private Date start;
	private Date end;
	private TUser user;
	private PrTProject project;

	private String cellVal;

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

	@Column(name = "TYPE", nullable = false, length = 1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ManyToOne()
	@JoinColumn(name = "USER_ID", nullable = false)
	public TUser getUser() {
		return this.user;
	}

	public void setUser(TUser user) {
		this.user = user;
	}

	@ManyToOne()
	@JoinColumn(name = "PROJECT_ID", nullable = false)
	public PrTProject getProject() {
		return project;
	}

	public void setProject(PrTProject project) {
		this.project = project;
	}

	@Column(name = "TITLE", nullable = false, length = 100)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT", length = 300)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START", nullable = false, length = 7)
	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END", length = 7)
	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Transient
	public String getCellVal() {
		cellVal = "[" + getTitle() + "]" + getContent();
		return cellVal;
	}

	public void setCellVal(String cellVal) {
		this.cellVal = cellVal;
	}

}