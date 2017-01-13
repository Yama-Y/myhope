package com.myhope.service.workschedule;

import com.myhope.model.workschedule.WsTSchedule;
import com.myhope.service.BaseServiceI;

/**
 * 班次业务
 * 
 * @author YangMing
 * 
 */
public interface ScheduleServiceI extends BaseServiceI<WsTSchedule> {

	public void grantOrganization(String id, String ids);
}
