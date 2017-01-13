package com.myhope.service.workcalendar;

import java.util.List;

import com.myhope.model.workcalendar.WcTWorkcalendar;
import com.myhope.service.BaseServiceI;
import com.myhope.util.base.HqlFilter;

/**
 * 工作日历业务
 * 
 * @author YangMing
 * 
 */
public interface WorkcalendarServiceI extends BaseServiceI<WcTWorkcalendar> {
	/**
	 * 查找记录
	 */
	public List<WcTWorkcalendar> findWorkcalendarByFilter(HqlFilter hqlFilter);
	
	/**
	 * 提醒写记录
	 */
	public void remindWriteWork();
}
