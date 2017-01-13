package com.myhope.util.base;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.myhope.model.base.SessionInfo;
import com.myhope.model.base.TOrganization;
import com.myhope.model.base.TResource;
import com.myhope.model.base.TRole;


/**
 * 用于前台页面判断是否有权限的工具类
 * 
 * @author YangMing
 * 
 */
public class SecurityUtil {
	private HttpSession session;

	public SecurityUtil(HttpSession session) {
		this.session = session;
	}

	/**
	 * 判断当前用户是否可以访问某资源
	 * 
	 * @param url
	 *            资源地址
	 * @return
	 */
	public boolean havePermission(String url) {
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		List<TResource> resources = new ArrayList<TResource>();
		for (TRole role : sessionInfo.getUser().getRoles()) {
			resources.addAll(role.getResources());
		}
		for (TOrganization organization : sessionInfo.getUser().getOrganizations()) {
			resources.addAll(organization.getResources());
		}
		resources = new ArrayList<TResource>(new HashSet<TResource>(resources));// 去重(这里包含了当前用户可访问的所有资源)
		for (TResource resource : resources) {
			if (StringUtils.equals(resource.getUrl(), url)) {// 如果有相同的，则代表当前用户可以访问这个资源
				return true;
			}
		}
		return false;
	}
}
