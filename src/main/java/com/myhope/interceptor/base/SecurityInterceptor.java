package com.myhope.interceptor.base;

import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;


import com.myhope.model.base.SessionInfo;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TResource;
import com.myhope.model.base.TRole;
import com.myhope.util.base.ConfigUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 权限拦截器
 * 
 * @author YangMing
 * 
 */
public class SecurityInterceptor extends MethodFilterInterceptor {

	private static final Logger logger = Logger.getLogger(SecurityInterceptor.class);

	protected String doIntercept(ActionInvocation actionInvocation) throws Exception {
		//ActionContext actionContext = actionInvocation.getInvocationContext();
		SessionInfo sessionInfo = (SessionInfo) ServletActionContext.getRequest().getSession().getAttribute(ConfigUtil.getSessionInfoName());
		String servletPath = ServletActionContext.getRequest().getServletPath();

		servletPath = StringUtils.substringBeforeLast(servletPath, ".");// 去掉后面的后缀 *.myhope *.action之类的

		logger.info("进入权限拦截器->访问的资源为：[" + servletPath + "]");

		Set<TRole> roles = sessionInfo.getUser().getRoles();
		for (TRole role : roles) {
			for (TResource resource : role.getResources()) {
				if (resource != null && StringUtils.equals(resource.getUrl(), servletPath)) {
					return actionInvocation.invoke();
				}
			}
		}
		Set<TOrganization> organizations = sessionInfo.getUser().getOrganizations();
		for (TOrganization organization : organizations) {
			for (TResource resource : organization.getResources()) {
				if (resource != null && StringUtils.equals(resource.getUrl(), servletPath)) {
					return actionInvocation.invoke();
				}
			}
		}

		String errMsg = "您没有访问此功能的权限！功能路径为[" + servletPath + "]请联系管理员给你赋予相应权限。";
		logger.info(errMsg);
		ServletActionContext.getRequest().setAttribute("msg", errMsg);
		return "noSecurity";
	}

}
