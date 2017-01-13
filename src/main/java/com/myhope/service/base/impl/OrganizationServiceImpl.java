package com.myhope.service.base.impl;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TResource;
import com.myhope.model.base.TUser;
import com.myhope.service.base.OrganizationServiceI;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.util.base.BeanUtils;
import com.myhope.util.base.HqlFilter;

/**
 * 机构业务
 * 
 * @author YangMing
 * 
 */
@Service
public class OrganizationServiceImpl extends BaseServiceImpl<TOrganization> implements OrganizationServiceI {

	@Autowired
	private BaseDaoI<TResource> resourceDao;

	@Autowired
	private BaseDaoI<TUser> userDao;

	@Override
	public void updateOrganization(TOrganization organization) {
		if (!StringUtils.isBlank(organization.getId())) {
			TOrganization t = getById(organization.getId());
			TOrganization oldParent = t.getOrganization();
			BeanUtils.copyNotNullProperties(organization, t, new String[] { "createdatetime" });
			if (organization.getOrganization() != null) {// 说明要修改的节点选中了上级节点
				TOrganization pt = getById(organization.getOrganization().getId());// 上级节点
				isParentToChild(t, pt, oldParent);// 说明要将当前节点修改到当前节点的子或者孙子下
				t.setOrganization(pt);
			} else {
				t.setOrganization(null);
			}
		}
	}

	/**
	 * 判断是否是将当前节点修改到当前节点的子节点下
	 * 
	 * @param t
	 *            当前节点
	 * @param pt
	 *            要修改到的节点
	 * 
	 * @param oldParent
	 *            原上级节点
	 * @return
	 */
	private boolean isParentToChild(TOrganization t, TOrganization pt, TOrganization oldParent) {
		if (pt != null && pt.getOrganization() != null) {
			if (StringUtils.equals(pt.getOrganization().getId(), t.getId())) {
				pt.setOrganization(oldParent);
				return true;
			} else {
				return isParentToChild(t, pt.getOrganization(), oldParent);
			}
		}
		return false;
	}

	@Override
	public void grant(String id, String resourceIds) {
		TOrganization organization = getById(id);
		if (organization != null) {
			organization.setResources(new HashSet<TResource>());
			for (String resourceId : resourceIds.split(",")) {
				if (!StringUtils.isBlank(resourceId)) {
					TResource resource = resourceDao.getById(TResource.class, resourceId);
					if (resource != null) {
						organization.getResources().add(resource);
					}
				}
			}
		}
	}

	@Override
	public List<TOrganization> findOrganizationByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from TOrganization t join t.users user";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public void saveOrganization(TOrganization organization, String userId) {
		save(organization);

		TUser user = userDao.getById(TUser.class, userId);
		user.getOrganizations().add(organization);
	}

}
