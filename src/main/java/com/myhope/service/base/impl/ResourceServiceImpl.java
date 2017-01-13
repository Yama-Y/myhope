package com.myhope.service.base.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TResource;
import com.myhope.model.base.TRole;
import com.myhope.model.base.TUser;
import com.myhope.service.base.ResourceServiceI;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.util.base.BeanUtils;
import com.myhope.util.base.HqlFilter;


/**
 * 资源业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class ResourceServiceImpl extends BaseServiceImpl<TResource> implements ResourceServiceI {

	@Autowired
	private BaseDaoI<TUser> userDao;

	/**
	 * 由于角色与资源关联，机构也与资源关联，所以查询用户能看到的资源菜单应该查询两次，最后合并到一起。
	 */
	@Override
	public List<TResource> getMainMenu(HqlFilter hqlFilter) {
		List<TResource> l = new ArrayList<TResource>();
		String hql = "select distinct t from TResource t join t.roles role join role.users user";
		List<TResource> resource_role = find(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
		l.addAll(resource_role);
		hql = "select distinct t from TResource t join t.organizations organization join organization.users user";
		List<TResource> resource_organization = find(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
		l.addAll(resource_organization);
		l = new ArrayList<TResource>(new HashSet<TResource>(l));// 去重
		Collections.sort(l, new Comparator<TResource>() {// 排序
					@Override
					public int compare(TResource o1, TResource o2) {
						if (o1.getSeq() == null) {
							o1.setSeq(1000);
						}
						if (o2.getSeq() == null) {
							o2.setSeq(1000);
						}
						return o1.getSeq().compareTo(o2.getSeq());
					}
				});
		return l;
	}

	@Override
	public List<TResource> resourceTreeGrid(HqlFilter hqlFilter) {
		List<TResource> l = new ArrayList<TResource>();
		String hql = "select distinct t from TResource t join t.roles role join role.users user";
		List<TResource> resource_role = find(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
		l.addAll(resource_role);
		hql = "select distinct t from TResource t join t.organizations organization join organization.users user";
		List<TResource> resource_organization = find(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
		l.addAll(resource_organization);
		l = new ArrayList<TResource>(new HashSet<TResource>(l));// 去重
		Collections.sort(l, new Comparator<TResource>() {// 排序
					@Override
					public int compare(TResource o1, TResource o2) {
						if (o1.getSeq() == null) {
							o1.setSeq(1000);
						}
						if (o2.getSeq() == null) {
							o2.setSeq(1000);
						}
						return o1.getSeq().compareTo(o2.getSeq());
					}
				});
		return l;
	}

	@Override
	public void updateResource(TResource resource) {
		if (!StringUtils.isBlank(resource.getId())) {
			TResource t = getById(resource.getId());
			TResource oldParent = t.getResource();
			BeanUtils.copyNotNullProperties(resource, t, new String[] { "createdatetime" });
			if (resource.getResource() != null) {// 说明要修改的节点选中了上级节点
				TResource pt = getById(resource.getResource().getId());// 上级节点
				isParentToChild(t, pt, oldParent);// 说明要将当前节点修改到当前节点的子或者孙子下
				t.setResource(pt);
			} else {
				t.setResource(null);
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
	private boolean isParentToChild(TResource t, TResource pt, TResource oldParent) {
		if (pt != null && pt.getResource() != null) {
			if (StringUtils.equals(pt.getResource().getId(), t.getId())) {
				pt.setResource(oldParent);
				return true;
			} else {
				return isParentToChild(t, pt.getResource(), oldParent);
			}
		}
		return false;
	}

	/**
	 * 由于新添加的资源，当前用户的角色或者机构并没有访问此资源的权限，所以这个地方重写save方法，将新添加的资源放到用户的第一个角色里面或者第一个机构里面
	 */
	@Override
	public void saveResource(TResource resource, String userId) {
		save(resource);

		TUser user = userDao.getById(TUser.class, userId);
		Set<TRole> roles = user.getRoles();
		if (roles != null && !roles.isEmpty()) {// 如果用户有角色，就将新资源放到用户的第一个角色里面
			List<TRole> l = new ArrayList<TRole>();
			l.addAll(roles);
			Collections.sort(l, new Comparator<TRole>() {
				@Override
				public int compare(TRole o1, TRole o2) {
					if (o1.getCreatedatetime().getTime() > o2.getCreatedatetime().getTime()) {
						return 1;
					}
					if (o1.getCreatedatetime().getTime() < o2.getCreatedatetime().getTime()) {
						return -1;
					}
					return 0;
				}
			});
			l.get(0).getResources().add(resource);
		} else {// 如果用户没有角色
			Set<TOrganization> organizations = user.getOrganizations();
			if (organizations != null && !organizations.isEmpty()) {// 如果用户没有角色，但是有机构，那就将新资源放到第一个机构里面
				List<TOrganization> l = new ArrayList<TOrganization>();
				l.addAll(organizations);
				Collections.sort(l, new Comparator<TOrganization>() {
					@Override
					public int compare(TOrganization o1, TOrganization o2) {
						if (o1.getCreatedatetime().getTime() > o2.getCreatedatetime().getTime()) {
							return 1;
						}
						if (o1.getCreatedatetime().getTime() < o2.getCreatedatetime().getTime()) {
							return -1;
						}
						return 0;
					}
				});
				l.get(0).getResources().add(resource);
			}
		}
	}

	@Override
	public List<TResource> findResourceByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from TResource t left join t.roles role left join t.organizations organization";
		return find(hql + hqlFilter.getWhereHql(), hqlFilter.getParams());
	}

}
