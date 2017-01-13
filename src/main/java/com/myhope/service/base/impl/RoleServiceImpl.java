package com.myhope.service.base.impl;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TResource;
import com.myhope.model.base.TRole;
import com.myhope.model.base.TUser;
import com.myhope.service.base.RoleServiceI;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.util.base.HqlFilter;


/**
 * 角色业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<TRole> implements RoleServiceI {

	@Autowired
	private BaseDaoI<TUser> userDao;
	@Autowired
	private BaseDaoI<TResource> resourceDao;

	@Override
	public List<TRole> findRoleByFilter(HqlFilter hqlFilter, int page, int rows) {
		String hql = "select distinct t from TRole t join t.users user";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams(), page, rows);
	}

	@Override
	public List<TRole> findRoleByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from TRole t join t.users user";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public Long countRoleByFilter(HqlFilter hqlFilter) {
		String hql = "select count(distinct t) from TRole t join t.users user";
		return count(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
	}

	@Override
	public void saveRole(TRole role, String userId) {
		save(role);

		TUser user = userDao.getById(TUser.class, userId);
		user.getRoles().add(role);// 把新添加的角色与当前用户关联
	}

	@Override
	public void grant(String id, String resourceIds) {
		TRole role = getById(id);
		if (role != null) {
			role.setResources(new HashSet<TResource>());
			for (String resourceId : resourceIds.split(",")) {
				if (!StringUtils.isBlank(resourceId)) {
					TResource resource = resourceDao.getById(TResource.class, resourceId);
					if (resource != null) {
						role.getResources().add(resource);
					}
				}
			}
		}
	}

}
