package com.myhope.service.project;

import com.myhope.model.project.PrTProject;
import com.myhope.service.BaseServiceI;

/**
 * 项目业务
 * 
 * @author YangMing
 * 
 */
public interface ProjectServiceI extends BaseServiceI<PrTProject> {
	/**
	 * 为项目授权用户
	 * 
	 * @param id
	 *            项目ID
	 * @param userIds
	 *            用户IDS
	 */
	public void grantUser(String id, String userIds);

}
