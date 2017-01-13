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

@Entity
@Table(name = "T_RESOURCE", schema = "")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TResource implements java.io.Serializable {

	private String pid;// 虚拟属性，用于获得当前资源的父资源ID

	private String id;
	private Date createdatetime;
	private Date updatedatetime;
	private String name;
	private String url;
	private String description;
	private String iconCls;
	private Integer seq;
	private String target;
	private TResourcetype resourcetype;
	private TResource resource;
	private Set<TRole> roles = new HashSet<TRole>(0);
	private Set<TOrganization> organizations = new HashSet<TOrganization>(0);
	private Set<TResource> resources = new HashSet<TResource>(0);

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCETYPE_ID")
	public TResourcetype getResourcetype() {
		return this.resourcetype;
	}

	public void setResourcetype(TResourcetype resourcetype) {
		this.resourcetype = resourcetype;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "RESOURCE_ID")
	public TResource getResource() {
		return this.resource;
	}

	public void setResource(TResource resource) {
		this.resource = resource;
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

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "URL", length = 200)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "DESCRIPTION", length = 200)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	@Column(name = "TARGET", length = 100)
	public String getTarget() {
		return this.target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_ROLE_RESOURCE", schema = "", joinColumns = { @JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", nullable = false, updatable = false) })
	public Set<TRole> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<TRole> roles) {
		this.roles = roles;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_ORGANIZATION_RESOURCE", schema = "", joinColumns = { @JoinColumn(name = "RESOURCE_ID", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ORGANIZATION_ID", nullable = false, updatable = false) })
	public Set<TOrganization> getOrganizations() {
		return this.organizations;
	}

	public void setOrganizations(Set<TOrganization> organizations) {
		this.organizations = organizations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "resource", cascade = CascadeType.ALL)
	public Set<TResource> getResources() {
		return this.resources;
	}

	public void setResources(Set<TResource> resources) {
		this.resources = resources;
	}

	@Transient
	public String getPid() {
		if (resource != null && !StringUtils.isBlank(resource.getId())) {
			return resource.getId();
		}
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
