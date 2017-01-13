package com.myhope.service.project.impl;

import java.util.HashSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myhope.dao.base.BaseDaoI;
import com.myhope.model.base.TUser;
import com.myhope.model.project.PrTProject;
import com.myhope.service.impl.BaseServiceImpl;
import com.myhope.service.project.ProjectServiceI;

/**
 * 项目业务逻辑
 * 
 * @author YangMing
 * 
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<PrTProject> implements
		ProjectServiceI {

	@Autowired
	private BaseDaoI<TUser> userDao;

	@Override
	public void grantUser(String id, String userIds) {
		PrTProject project = getById(id);
		if (project != null) {
			project.setUsers(new HashSet<TUser>());
			for (String userId : userIds.split(",")) {
				if (!StringUtils.isBlank(userId)) {
					TUser user = userDao.getById(TUser.class, userId);
					if (user != null) {
						project.getUsers().add(user);
					}
				}
			}
		}
	}

}
