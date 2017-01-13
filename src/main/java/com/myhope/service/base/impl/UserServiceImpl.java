package com.myhope.service.base.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TRole;
import com.myhope.model.base.TUser;
import com.myhope.service.base.UserServiceI;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.util.base.HqlFilter;


/**
 * 用户业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements UserServiceI {

	@Autowired
	private BaseDaoI<TRole> roleDao;

	@Autowired
	private BaseDaoI<TOrganization> organizationDao;

	@Override
	public void grantRole(String id, String roleIds) {
		TUser user = getById(id);
		if (user != null) {
			user.setRoles(new HashSet<TRole>());
			for (String roleId : roleIds.split(",")) {
				if (!StringUtils.isBlank(roleId)) {
					TRole role = roleDao.getById(TRole.class, roleId);
					if (role != null) {
						user.getRoles().add(role);
					}
				}
			}
		}
	}

	@Override
	public void grantOrganization(String id, String organizationIds) {
		TUser user = getById(id);
		if (user != null) {
			user.setOrganizations(new HashSet<TOrganization>());
			for (String organizationId : organizationIds.split(",")) {
				if (!StringUtils.isBlank(organizationId)) {
					TOrganization organization = organizationDao.getById(TOrganization.class, organizationId);
					if (organization != null) {
						user.getOrganizations().add(organization);
					}
				}
			}
		}
	}

	@Override
	public List<Long> userCreateDatetimeChart() {
		List<Long> l = new ArrayList<Long>();
		int k = 0;
		for (int i = 0; i < 12; i++) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("a", k);
			params.put("b", k + 2);
			k = k + 2;
			l.add(count("select count(*) from TUser t where HOUR(t.createdatetime)>=:a and HOUR(t.createdatetime)<:b", params));
		}
		return l;
	}

	@Override
	public Long countUserByRoleId(String roleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("roleId", roleId);
		String hql = "select count(*) from TUser t join t.roles role where role.id = :roleId";
		return count(hql, params);
	}

	@Override
	public Long countUserByNotRoleId() {
		String hql = "select count(*) from TUser t left join t.roles role where role.id is null";
		return count(hql);
	}
	
	@Override
	public List<TUser> findProjectUserByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from TUser t join t.projects project";
		return find(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
	}
}
