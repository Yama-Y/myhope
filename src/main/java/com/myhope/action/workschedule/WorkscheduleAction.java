package com.myhope.action.workschedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
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
import com.myhope.model.workschedule.WsAttendance;
import com.myhope.model.workschedule.WsTSchedule;
import com.myhope.model.workschedule.WsTWorkschedule;
import com.myhope.service.base.UserServiceI;
import com.myhope.service.workschedule.WorkscheduleServiceI;
import com.myhope.util.base.ConfigUtil;
import com.myhope.util.base.DateUtil;
import com.myhope.util.base.HqlFilter;

/**
 * 工作排班
 * 
 * action访问地址是/workschedule.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/")
@Action
public class WorkscheduleAction extends BaseAction<WsTWorkschedule> {

	@Autowired
	private UserServiceI userServiceI;

	@Autowired
	public void setService(WorkscheduleServiceI service) {
		this.service = service;
	}

	public void getDates() {
		// id = "2016-01-06,2016-01-06";
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		TUser user = sessionInfo.getUser();
		Object[] objects = new Object[3];
		List<TUser> users = getDates_users(user);
		objects[0] = users;
		objects[1] = getDates_workSchedules(users, id);
		objects[2] = getDates_schedules(user);
		writeJson(objects);
	}

	private List<TUser> getDates_users(TUser user) {
		List<TUser> l = new ArrayList<TUser>();
		HqlFilter hqlFilter = null;
		Set<TOrganization> organizations = user.getOrganizations();
		for (TOrganization organization : organizations) {
			hqlFilter = new HqlFilter();
			hqlFilter.addFilter("QUERY_organization#id_S_EQ", organization.getId());
			List<TUser> users = userServiceI.find(
					"select distinct t from TUser t join t.organizations organization" + hqlFilter.getWhereHql(), hqlFilter.getParams());
			l.addAll(users);
			hqlFilter = null;
		}
		l = new ArrayList<TUser>(new HashSet<TUser>(l));// 去重
		Collections.sort(l, new Comparator<TUser>() {// 排序
					@Override
					public int compare(TUser o1, TUser o2) {
						return o1.getLoginname().compareTo(o2.getLoginname());
					}
				});

		return l;
	}

	private List<WsTSchedule> getDates_schedules(TUser user) {
		return ((WorkscheduleServiceI) service).findSchedules(user);
	}

	private List<WsTWorkschedule> getDates_workSchedules(List<TUser> users, String id) {
		List<WsTWorkschedule> workschedules = ((WorkscheduleServiceI) service).findWorkschedule(id, users);

		return workschedules;
	}

	/**
	 * 保存一个对象
	 */
	public void doNotNeedSecurity_save() {
		writeJson(service.save(data));
	}

	/**
	 * 删除一个对象
	 */
	public void doNotNeedSecurity_delete() {
		if (!StringUtils.isBlank(id)) {
			WsTWorkschedule t = service.getById(id);
			service.delete(t);
		}
		writeJson(true);
	}

	/**
	 * 出勤情况报表
	 */
	public void doNotNeedSecurity_attendance_my() {
		TUser user = null;
		if (data != null && data.getUser() != null) {
			user = userServiceI.getById(data.getUser().getId());
		} else {
			SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
			user = sessionInfo.getUser();
		}

		Date beginDate = null;
		Date endDate = null;
		// id 开始时间 ids结束时间
		if (id == null && ids == null) {
			beginDate = DateUtil.getMonthBegin(new Date(), 0);
			endDate = new Date();
		} else {
			beginDate = DateUtil.stringToDate(id, "yyyy-MM-dd");
			endDate = DateUtil.stringToDate(ids, "yyyy-MM-dd");
		}
		List<WsAttendance> attendances = ((WorkscheduleServiceI) service).countAttendances(user, beginDate, endDate);
		Collections.sort(attendances, new Comparator<WsAttendance>() {// 排序
					@Override
					public int compare(WsAttendance a1, WsAttendance a2) {
						return a2.getNormalDate().compareTo(a1.getNormalDate());
					}
				});
		// 仅显示异常的过滤处理
		if (q != null && "on".equals(q)) {
			for (Iterator<WsAttendance> iterator = attendances.iterator(); iterator.hasNext();) {
				WsAttendance attendance = (WsAttendance) iterator.next();
				if (attendance.getWrongTime() == 0L) {
					iterator.remove();
				}
			}
		}
		writeJson(attendances);
	}

}
