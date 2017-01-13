package com.myhope.service.workschedule.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TUser;
import com.myhope.model.workschedule.WsAttendance;
import com.myhope.model.workschedule.WsTAttendance;
import com.myhope.model.workschedule.WsTSchedule;
import com.myhope.model.workschedule.WsTScheduleDetail;
import com.myhope.model.workschedule.WsTWorkschedule;
import com.myhope.service.base.OrganizationServiceI;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.service.workschedule.AttendanceServiceI;
import com.myhope.service.workschedule.ScheduleServiceI;
import com.myhope.service.workschedule.WorkscheduleServiceI;
import com.myhope.util.base.DateUtil;
import com.myhope.util.base.HqlFilter;

/**
 * 工作排班及考勤业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class WorkscheduleServiceImpl extends BaseServiceImpl<WsTWorkschedule> implements WorkscheduleServiceI {
	@Autowired
	private OrganizationServiceI organizationService;
	@Autowired
	private ScheduleServiceI scheduleServiceI;
	@Autowired
	private AttendanceServiceI attendanceServiceI;

	@Override
	public List<WsTWorkschedule> findWorkschedule(String id, List<TUser> users) {
		String[] dates = id.split(",");
		String beginDate = dates[0];
		String endDate = dates[1];
		List<WsTWorkschedule> l = new ArrayList<WsTWorkschedule>();
		for (TUser tUser : users) {
			HqlFilter hqlFilter = new HqlFilter();
			hqlFilter.addFilter("QUERY_t#date_D_GE", beginDate);
			hqlFilter.addFilter("QUERY_t#date_D_LE", endDate);
			hqlFilter.addFilter("QUERY_user#id_S_EQ", tUser.getId());
			String hql = "select distinct t from WsTWorkschedule t join t.user user";
			l.addAll(find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams()));
		}

		return l;
	}

	@Override
	public List<WsTSchedule> findSchedules(TUser user) {
		List<WsTSchedule> scheduleList = new ArrayList<WsTSchedule>();
		Set<TOrganization> organizations = user.getOrganizations();
		for (TOrganization organization : organizations) {
			TOrganization organization2 = organizationService.getById(organization.getId());
			Set<WsTSchedule> schedules = organization2.getSchedules();
			scheduleList.addAll(schedules);
		}
		scheduleList = new ArrayList<WsTSchedule>(new HashSet<WsTSchedule>(scheduleList));
		Collections.sort(scheduleList, new Comparator<WsTSchedule>() {// 排序
					@Override
					public int compare(WsTSchedule s1, WsTSchedule s2) {
						return s1.getCreatedatetime().compareTo(s2.getCreatedatetime());
					}
				});
		return scheduleList;
	}

	@Override
	public void saveDefaultWorkschedule() {
		List<TOrganization> organizations = organizationService.find();
		for (TOrganization organization : organizations) {
			if (organization.getDefaultSchedule() != null && !"".equals(organization.getDefaultSchedule())) {
				String defaultScheduleId = organization.getDefaultSchedule();
				Set<TUser> users = organization.getUsers();

				Calendar c = Calendar.getInstance();
				Date dateBegin = DateUtil.getMonthBegin(new Date(), 0);
				Date dateEnd = DateUtil.getMonthEnd(new Date(), 0);
				WsTWorkschedule workschedule = null;
				while (dateBegin.compareTo(dateEnd) <= 0) {
					c.setTime(dateBegin);
					int w = c.get(Calendar.DAY_OF_WEEK) - 1;
					if (w == 1 || w == 2 || w == 3 || w == 4 || w == 5) {
						for (TUser user : users) {
							workschedule = new WsTWorkschedule();
							workschedule.setDate(dateBegin);
							workschedule.setUser(user);
							workschedule.setSchedule(defaultScheduleId);
							save(workschedule);
						}
					}
					c.add(Calendar.DATE, 1);
					dateBegin = c.getTime();
				}
			}
		}
	}

	@Override
	public List<WsAttendance> countAttendances(TUser user, Date beginDate, Date endDate) {
		List<WsAttendance> attendances = new ArrayList<WsAttendance>();
		WsAttendance attendance = null;
		HqlFilter hqlFilter = new HqlFilter();
		hqlFilter.addFilter("QUERY_t#date_D_GE", DateUtil.dateToString(beginDate, "yyyy-MM-dd"));
		hqlFilter.addFilter("QUERY_t#date_D_LE", DateUtil.dateToString(endDate, "yyyy-MM-dd"));
		hqlFilter.addFilter("QUERY_user#id_S_EQ", user.getId());
		String hql = "select distinct t from WsTWorkschedule t join t.user user";
		List<WsTWorkschedule> workschedules = find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
		// 循环周期内的排班数据
		for (WsTWorkschedule workschedule : workschedules) {
			Date date = workschedule.getDate();
			WsTSchedule schedule = scheduleServiceI.getById(workschedule.getSchedule());
			Set<WsTScheduleDetail> scheduleDetails = schedule.getScheduleDetails();
			// 循环排班规则
			for (WsTScheduleDetail scheduleDetail : scheduleDetails) {
				// 如果需要打卡
				if ("A".equals(scheduleDetail.getIsCard())) {
					attendance = new WsAttendance();
					// 运算后的打卡信息
					attendance = findTrueTAttendance(user, date, scheduleDetail);
					attendances.add(attendance);
				}
			}
		}

		return attendances;
	}

	/**
	 * 运算单条考勤规则
	 * 
	 */
	private WsAttendance findTrueTAttendance(TUser user, Date date, WsTScheduleDetail scheduleDetail) {
		WsAttendance attendance = new WsAttendance();
		String type = scheduleDetail.getCardType();
		attendance.setType(type);

		// 处理日期。如果是次日的规则，推后一天。
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if ("B".equals(scheduleDetail.getTimeType())) {
			c.add(Calendar.DATE, 1);
		}
		String beginDateString = DateUtil.dateToString(c.getTime(), "yyyy-MM-dd ") + scheduleDetail.getBegintime();
		String endDateString = DateUtil.dateToString(c.getTime(), "yyyy-MM-dd ") + scheduleDetail.getEndtime();

		String normalDateString = DateUtil.dateToString(c.getTime(), "yyyy-MM-dd ") + scheduleDetail.getTime();
		attendance.setNormalDate(DateUtil.stringToDate(normalDateString));

		HqlFilter hqlFilter = new HqlFilter();
		hqlFilter.addFilter("QUERY_t#time_D_GE", beginDateString);
		hqlFilter.addFilter("QUERY_t#time_D_LE", endDateString);
		hqlFilter.addFilter("QUERY_t#name_S_EQ", user.getName());
		List<WsTAttendance> attendances = attendanceServiceI.findByFilter(hqlFilter);
		// 未出现有效打卡信息，返回null
		if (attendances == null || attendances.size() == 0) {
//			attendance.setIsDo("N");
			attendance.setWrongTime(-1L);
		} else {
//			attendance.setIsDo("Y");
			// A:上班；D:中间休息结束 取最早有效数据
			// B:下班；C:中间休息开始 取最晚有效数据
			if ("A".equals(type) || "D".equals(type)) {
				for (WsTAttendance tAttendance : attendances) {
					if (attendance.getDoDate() == null) {
						attendance.setDoDate(tAttendance.getTime());
					} else {
						if (tAttendance.getTime().before(attendance.getDoDate())) {
							attendance.setDoDate(tAttendance.getTime());
						}
					}
				}
				if (attendance.getDoDate().before(attendance.getNormalDate())) {
					attendance.setWrongTime(0L);
				} else {
					attendance.setWrongTime(attendance.getDoDate().getTime() - attendance.getNormalDate().getTime());
				}
			} else if ("B".equals(type) || "C".equals(type)) {
				for (WsTAttendance tAttendance : attendances) {
					if (attendance.getDoDate() == null) {
						attendance.setDoDate(tAttendance.getTime());
					} else {
						if (tAttendance.getTime().after(attendance.getDoDate())) {
							attendance.setDoDate(tAttendance.getTime());
						}
					}
				}
				if (attendance.getDoDate().after(attendance.getNormalDate())) {
					attendance.setWrongTime(0L);
				} else {
					attendance.setWrongTime(attendance.getNormalDate().getTime() - attendance.getDoDate().getTime());
				}
			}
		}

		return attendance;
	}

}
