package com.myhope.service.workcalendar.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TUser;
import com.myhope.model.workcalendar.WcTWorkcalendar;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.service.workcalendar.WorkcalendarServiceI;
import com.myhope.util.base.DateUtil;
import com.myhope.util.base.HqlFilter;
import com.myhope.util.base.MailUtil;

/**
 * 工作日历业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class WorkcalendarServiceImpl extends BaseServiceImpl<WcTWorkcalendar> implements WorkcalendarServiceI {

	@Autowired
	private BaseDaoI<TUser> userDao;

	@Override
	public List<WcTWorkcalendar> findWorkcalendarByFilter(HqlFilter hqlFilter) {
		String hql = "select distinct t from WcTWorkcalendar t join t.user user";
		return find(hql + hqlFilter.getWhereAndOrderHql(), hqlFilter.getParams());
	}

	@Override
	public void remindWriteWork() {
		Calendar cal = Calendar.getInstance();
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w == 1 || w == 2 || w == 3 || w == 4 || w == 5) {
			MailUtil mailUtil = new MailUtil();
			List<TUser> users = userDao.find("select distinct t from TUser t");
			StringBuffer sb = null;
			for (TUser tUser : users) {
				if (tUser != null && tUser.getMail() != null && !"".equals(tUser.getMail())) {
					sb = new StringBuffer();
					if ("吕鑫".equals(tUser.getName())) {
						if (!isWriteOverToday(tUser)) {
							sb.append("您今天的工作记录好像忘写了哦。<br>");
						}
					}
					if (w == 5) {
						sb.append("今天是【星期五】，别忘了完善、下载并发送周报哦。<br>");
						Map<String, Long> timeMap = writeOverWeekTime(tUser);
						Set<String> keySet = timeMap.keySet();
						boolean isOver = true;
						StringBuffer noOverSB = new StringBuffer();
						for (String key : keySet) {
							Long time = timeMap.get(key);
							if (time < (8 * 1000 * 60 * 60)) {
								isOver = false;
								noOverSB.append("时间：" + key + "&nbsp;&nbsp;" + ((double) (time / (1000 * 60 * 60))) + "小时。<br>");
							}
						}
						if (!isOver) {
							sb.append("友情提示：<br>您在以下日期的周报不足工作标准时间（8小时）：<br>");
							sb.append(noOverSB);
						}
					}
					mailUtil.add(tUser.getMail(), sb.toString());
					sb = null;
				}
			}
			Thread t = new Thread(mailUtil);
			t.start();
		}
	}

	private Boolean isWriteOverToday(TUser tUser) {
		Calendar cal = Calendar.getInstance();
		String startDate = DateUtil.dateToString(cal.getTime(), "yyyy-MM-dd") + " 00:00:00";
		String endDate = DateUtil.dateToString(cal.getTime(), "yyyy-MM-dd") + " 23:59:59";

		HqlFilter hqlFilter = new HqlFilter();
		hqlFilter.addFilter("QUERY_user#id_S_EQ", tUser.getId());
		hqlFilter.addFilter("QUERY_t#start_D_GE", startDate);
		hqlFilter.addFilter("QUERY_t#end_D_LE", endDate);
		List<WcTWorkcalendar> workcalendars = findWorkcalendarByFilter(hqlFilter);
		if (workcalendars != null && workcalendars.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private Map<String, Long> writeOverWeekTime(TUser tUser) {
		Date nowDate = new Date();
		String startDate = DateUtil.dateToString(DateUtil.getWeekBegin(nowDate, 0), "yyyy-MM-dd") + " 00:00:00";
		String endDate = DateUtil.dateToString(DateUtil.getWeekBegin(nowDate, 4), "yyyy-MM-dd") + " 23:59:59";

		HqlFilter hqlFilter = new HqlFilter();
		hqlFilter.addFilter("QUERY_user#id_S_EQ", tUser.getId());
		hqlFilter.addFilter("QUERY_t#start_D_GE", startDate);
		hqlFilter.addFilter("QUERY_t#end_D_LE", endDate);
		List<WcTWorkcalendar> workcalendars = findWorkcalendarByFilter(hqlFilter);

		Map<String, Long> timeMap = new HashMap<String, Long>();
		for (int i = 0; i < 5; i++) {
			Date workDate = DateUtil.getWeekBegin(nowDate, i);
			String workDateString = DateUtil.dateToString(workDate, "yyyy-MM-dd");
			timeMap.put(workDateString, 0L);
			for (WcTWorkcalendar workcalendar : workcalendars) {
				if ("A".equals(workcalendar.getType())) {
					if (workDateString.equals(DateUtil.dateToString(workcalendar.getStart(), "yyyy-MM-dd"))) {
						Long timeDifference = workcalendar.getEnd().getTime() - workcalendar.getStart().getTime();
						Long oldtimeD = timeMap.get(workDateString);
						timeMap.remove(workDateString);
						timeMap.put(workDateString, oldtimeD + timeDifference);
					}
				}
			}
		}
		return timeMap;
	}
}
