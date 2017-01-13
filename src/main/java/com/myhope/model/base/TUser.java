package com.myhope.model.base;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.myhope.model.project.PrTProject;

@Entity
@Table(name = "T_USER", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TUser implements java.io.Serializable {

	private String ip;// 此属性不存数据库，虚拟属性

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String loginname;
	private String pwd;
	private String name;
	private String sex;
	private Integer age;
	private String photo;
	private String qq;
	private String mail;
	private Set<TOrganization> organizations = new HashSet<TOrganization>(0);
	private Set<TRole> roles = new HashSet<TRole>(0);
	private Set<PrTProject> projects = new HashSet<PrTProject>(0);

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

	@Column(name = "LOGINNAME", nullable = false, length = 100)
	public String getLoginname() {
		return this.loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	@Column(name = "PWD", length = 100)
	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SEX", length = 1)
	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "AGE", precision = 8, scale = 0)
	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "PHOTO", length = 200)
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Column(name = "QQ", length = 20)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "Mail", length = 50)
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_USER_ORGANIZATION", schema = "", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ORGANIZATION_ID", nullable = false, updatable = false) })
	public Set<TOrganization> getOrganizations() {
		return this.organizations;
	}

	public void setOrganizations(Set<TOrganization> organizations) {
		this.organizations = organizations;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_USER_ROLE", schema = "", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) })
	public Set<TRole> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<TRole> roles) {
		this.roles = roles;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "Pr_USER_PROJECT", schema = "", joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "PROJECT_ID", nullable = false, updatable = false) })
	public Set<PrTProject> getProjects() {
		return this.projects;
	}

	public void setProjects(Set<PrTProject> projects) {
		this.projects = projects;
	}

	@Transient
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
