package com.myhope.action.workschedule;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.action.BaseAction;
import com.myhope.model.easyui.Json;
import com.myhope.model.workschedule.WsTSchedule;
import com.myhope.service.workschedule.ScheduleServiceI;
import com.myhope.util.base.HqlFilter;

/**
 * 班次
 * 
 * action访问地址是/schedule.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/")
@Action
public class ScheduleAction extends BaseAction<WsTSchedule> {

	@Autowired
	public void setService(ScheduleServiceI service) {
		this.service = service;
	}

	/**
	 * 授权机构
	 */
	public void grantOrganization() {
		Json json = new Json();
		((ScheduleServiceI) service).grantOrganization(id, ids);
		json.setSuccess(true);
		writeJson(json);
	}

	/**
	 * 根据机构查找班次
	 */
	public void doNotNeedSecurity_findSchedule() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		hqlFilter.addFilter("QUERY_organization#id_S_EQ", id);
		String hql = "select distinct t from WsTSchedule t join t.organizations organization";
		List<WsTSchedule> schedules = service.find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
		writeJson(schedules);
	}

}
