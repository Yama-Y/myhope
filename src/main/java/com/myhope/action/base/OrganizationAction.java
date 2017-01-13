package com.myhope.action.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.action.BaseAction;
import com.myhope.model.base.SessionInfo;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TUser;
import com.myhope.model.easyui.Json;
import com.myhope.service.base.OrganizationServiceI;
import com.myhope.service.base.UserServiceI;
import com.myhope.util.base.ConfigUtil;
import com.myhope.util.base.HqlFilter;

/**
 * 机构
 * 
 * 访问地址：/base/organization.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/base")
@Action
public class OrganizationAction extends BaseAction<TOrganization> {

	@Autowired
	private UserServiceI userService;

	/**
	 * 注入业务逻辑，使当前action调用service.xxx的时候，直接是调用基础业务逻辑
	 * 
	 * 如果想调用自己特有的服务方法时，请使用((TServiceI) service).methodName()这种形式强转类型调用
	 * 
	 * @param service
	 */
	@Autowired
	public void setService(OrganizationServiceI service) {
		this.service = service;
	}

	/**
	 * 保存一个机构
	 */
	public void save() {
		Json json = new Json();
		if (data != null) {
			SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
			((OrganizationServiceI) service).saveOrganization(data, sessionInfo.getUser().getId());
			json.setSuccess(true);
		}
		writeJson(json);
	}

	/**
	 * 更新机构
	 */
	public void update() {
		Json json = new Json();
		if (!StringUtils.isBlank(data.getId())) {
			if (data.getOrganization() != null && StringUtils.equals(data.getId(), data.getOrganization().getId())) {
				json.setMsg("父机构不可以是自己！");
			} else {
				((OrganizationServiceI) service).updateOrganization(data);
				json.setSuccess(true);
			}
		}
		writeJson(json);
	}

	/**
	 * 获得机构下拉树
	 */
	public void doNotNeedSecurity_comboTree() {
		HqlFilter hqlFilter = new HqlFilter();
		writeJson(service.findByFilter(hqlFilter));
	}

	/**
	 * 机构授权
	 */
	public void grant() {
		Json json = new Json();
		((OrganizationServiceI) service).grant(id, ids);
		json.setSuccess(true);
		writeJson(json);
	}

	/**
	 * 获得当前用户能看到的所有机构树
	 */
	public void doNotNeedSecurity_getOrganizationsTree() {
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		TUser user = userService.getById(sessionInfo.getUser().getId());
		Set<TOrganization> organizations = user.getOrganizations();
		List<TOrganization> l = new ArrayList<TOrganization>(organizations);
		Collections.sort(l, new Comparator<TOrganization>() {// 排序
					@Override
					public int compare(TOrganization o1, TOrganization o2) {
						if (o1.getSeq() == null) {
							o1.setSeq(1000);
						}
						if (o2.getSeq() == null) {
							o2.setSeq(1000);
						}
						return o1.getSeq().compareTo(o2.getSeq());
					}
				});
		writeJson(l);
	}

	/**
	 * 获得当前用户的机构
	 */
	public void doNotNeedSecurity_getOrganizationByUserId() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		hqlFilter.addFilter("QUERY_user#id_S_EQ", id);
		List<TOrganization> organizations = ((OrganizationServiceI) service).findOrganizationByFilter(hqlFilter);
		writeJson(organizations);
	}

	/**
	 * 获得当前班次的机构
	 */
	public void doNotNeedSecurity_getOrganizationByScheduleId() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		hqlFilter.addFilter("QUERY_schedule#id_S_EQ", id);
		String hql = "select distinct t from TOrganization t join t.schedules schedule";
		List<TOrganization> organizations = service.find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
		writeJson(organizations);
	}

}
