package com.myhope.service.workschedule.impl;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TOrganization;
import com.myhope.model.workschedule.WsTSchedule;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.service.workschedule.ScheduleServiceI;

/**
 * 班次业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class ScheduleServiceImpl extends BaseServiceImpl<WsTSchedule> implements ScheduleServiceI {

	@Autowired
	private BaseDaoI<TOrganization> organizationDao;

	@Override
	public void grantOrganization(String id, String organizationIds) {
		WsTSchedule schedule = getById(id);
		if (schedule != null) {
			schedule.setOrganizations(new HashSet<TOrganization>());
			for (String organizationId : organizationIds.split(",")) {
				if (!StringUtils.isBlank(organizationId)) {
					TOrganization organization = organizationDao.getById(TOrganization.class, organizationId);
					if (organization != null) {
						schedule.getOrganizations().add(organization);
					}
				}
			}
		}
	}

}
