package com.myhope.model.base;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.myhope.model.workschedule.WsTSchedule;

@Entity
@Table(name = "T_ORGANIZATION", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TOrganization implements java.io.Serializable {

	private String pid;// 虚拟属性，用于获得当前机构的父机构ID

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String name;
	private String address;
	private String code;
	private String iconCls;
	private Integer seq;
	private String defaultSchedule;
	private TOrganization organization;
	private Set<TOrganization> organizations = new HashSet<TOrganization>(0);
	private Set<TUser> users = new HashSet<TUser>(0);
	private Set<TResource> resources = new HashSet<TResource>(0);
	private Set<WsTSchedule> schedules = new HashSet<WsTSchedule>(0);

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

	@Column(name = "DEFAULT_SCHEDULE", length = 36)
	public String getDefaultSchedule() {
		return defaultSchedule;
	}

	public void setDefaultSchedule(String defaultSchedule) {
		this.defaultSchedule = defaultSchedule;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORGANIZATION_ID")
	public TOrganization getOrganization() {
		return this.organization;
	}

	public void setOrganization(TOrganization organization) {
		this.organization = organization;
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

	@Column(name = "NAME", length = 200)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ADDRESS", length = 200)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "CODE", length = 200)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "ICONCLS", length = 100)
	public String getIconCls() {
		return this.iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	@Column(name = "SEQ", precision = 8, scale = 0)
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "organization", cascade = CascadeType.ALL)
	public Set<TOrganization> getOrganizations() {
		return this.organizations;
	}

	public void setOrganizations(Set<TOrganization> organizations) {
		this.organizations = organizations;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_USER_ORGANIZATION", schema = "", joinColumns = { @JoinColumn(name = "ORGANIZATION_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) })
	public Set<TUser> getUsers() {
		return this.users;
	}

	public void setUsers(Set<TUser> users) {
		this.users = users;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_ORGANIZATION_RESOURCE", schema = "", joinColumns = { @JoinColumn(name = "ORGANIZATION_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false) })
	public Set<TResource> getResources() {
		return this.resources;
	}

	public void setResources(Set<TResource> resources) {
		this.resources = resources;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "WS_ORGANIZATIONS_SCHEDULE", schema = "", joinColumns = { @JoinColumn(name = "ORGANIZATIONS_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "SCHEDULE_ID", nullable = false, updatable = false) })
	public Set<WsTSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(Set<WsTSchedule> schedules) {
		this.schedules = schedules;
	}

	/**
	 * 用于业务逻辑的字段，注解@Transient代表不需要持久化到数据库中
	 * 
	 * @return
	 */
	@Transient
	public String getPid() {
		if (organization != null && !StringUtils.isBlank(organization.getId())) {
			return organization.getId();
		}
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
