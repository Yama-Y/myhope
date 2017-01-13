package com.myhope.service.workschedule;

import java.util.Date;
import java.util.List;

import com.myhope.model.base.TUser;
import com.myhope.model.workschedule.WsAttendance;
import com.myhope.model.workschedule.WsTSchedule;
import com.myhope.model.workschedule.WsTWorkschedule;
import com.myhope.service.BaseServiceI;

/**
 * 工作排班及考勤业务
 * 
 * @author YangMing
 * 
 */
public interface WorkscheduleServiceI extends BaseServiceI<WsTWorkschedule> {
	public List<WsTWorkschedule> findWorkschedule(String id, List<TUser> users);
	
	public List<WsTSchedule> findSchedules(TUser user);

	/**
	 * 初始化当月部门的默认排班
	 * 
	 */
	public void saveDefaultWorkschedule();

	/**
	 * 计算出勤情况
	 * 
	 */
	public List<WsAttendance> countAttendances(TUser user, Date beginDate, Date endDate);
}
