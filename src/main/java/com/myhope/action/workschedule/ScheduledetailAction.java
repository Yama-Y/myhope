package com.myhope.action.workschedule;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.action.BaseAction;
import com.myhope.model.easyui.Grid;
import com.myhope.model.workschedule.WsTScheduleDetail;
import com.myhope.service.workschedule.ScheduledetailServiceI;
import com.myhope.util.base.HqlFilter;

/**
 * 班次规则
 * 
 * action访问地址是/scheduledetail.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/")
@Action
public class ScheduledetailAction extends BaseAction<WsTScheduleDetail> {

	@Autowired
	public void setService(ScheduledetailServiceI service) {
		this.service = service;
	}

	@Override
	public void grid() {
		Grid grid = new Grid();
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		hqlFilter.addFilter("QUERY_schedule#id_S_EQ", id);
		grid.setTotal(service.countByFilter(hqlFilter));
		grid.setRows(service.findByFilter(hqlFilter, page, rows));
		writeJson(grid);
	}

}
