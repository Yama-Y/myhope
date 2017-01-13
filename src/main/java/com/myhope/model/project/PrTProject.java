package com.myhope.model.project;

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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.myhope.model.base.TUser;
import com.myhope.model.workcalendar.WcTWorkcalendar;

@Entity
@Table(name = "PR_T_PROJECT", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PrTProject implements java.io.Serializable {

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String name;
	private String content;
	private String state;// 状态：A：开始；B：暂停；C：结束
	private Set<TUser> users = new HashSet<TUser>(0);
	private Set<WcTWorkcalendar> workcalendars = new HashSet<WcTWorkcalendar>(0);

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

	@Column(name = "CONTENT", length = 300)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "STATE", nullable = false, length = 2)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Pr_USER_PROJECT", schema = "", joinColumns = { @JoinColumn(name = "PROJECT_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) })
	public Set<TUser> getUsers() {
		return this.users;
	}

	public void setUsers(Set<TUser> users) {
		this.users = users;
	}
	
	@OneToMany(mappedBy = "project", cascade = { CascadeType.ALL })  
	public Set<WcTWorkcalendar> getWorkcalendars() {
		return workcalendars;
	}

	public void setWorkcalendars(Set<WcTWorkcalendar> workcalendars) {
		this.workcalendars = workcalendars;
	}

}