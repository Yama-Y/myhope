package com.myhope.action.project;

import java.util.Set;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.action.BaseAction;
import com.myhope.model.base.SessionInfo;
import com.myhope.model.easyui.Json;
import com.myhope.model.project.PrTProject;
import com.myhope.service.base.UserServiceI;
import com.myhope.service.project.ProjectServiceI;
import com.myhope.util.base.ConfigUtil;
import com.myhope.util.base.HqlFilter;

/**
 * 项目
 * 
 * action访问地址是/project.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/")
@Action
public class ProjectAction extends BaseAction<PrTProject> {

	@Autowired
	private UserServiceI userService;

	/**
	 * 注入业务逻辑，使当前action调用service.xxx的时候，直接是调用基础业务逻辑
	 * 
	 * 如果想调用自己特有的服务方法时，请使用((TServiceI) service).methodName()这种形式强转类型调用
	 * 
	 * @param service
	 */
	@Autowired
	public void setService(ProjectServiceI service) {
		this.service = service;
	}

	/**
	 * 项目授权用户
	 */
	public void grantUsers() {
		Json json = new Json();
		((ProjectServiceI) service).grantUser(id, ids);
		json.setSuccess(true);
		writeJson(json);
	}

	/**
	 * 获得项目的用户列表
	 */
	public void doNotNeedSecurity_getProjectUser() {
		HqlFilter hqlFilter = new HqlFilter(getRequest());
		hqlFilter.addFilter("QUERY_project#id_S_EQ", id);
		writeJson(userService.findProjectUserByFilter(hqlFilter));
	}

	/**
	 * 从Session获得当前用户的项目
	 */
	public void doNotNeedSecurity_getProjectByUserId() {
		SessionInfo sessionInfo = (SessionInfo) getSession().getAttribute(ConfigUtil.getSessionInfoName());
		Set<PrTProject> projects = sessionInfo.getUser().getProjects();
		for (PrTProject project : projects) {
			if ("C".equals(project.getState())) {
				projects.remove(project);
			}
		}
		writeJson(projects);
	}

}
