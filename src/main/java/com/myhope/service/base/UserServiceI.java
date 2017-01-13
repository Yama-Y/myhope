package com.myhope.service.base;

import java.util.List;

import com.myhope.model.base.TUser;
import com.myhope.service.BaseServiceI;
import com.myhope.util.base.HqlFilter;


/**
 * 用户业务
 * 
 * @author YangMing
 * 
 */
public interface UserServiceI extends BaseServiceI<TUser> {

	/**
	 * 修改用户角色
	 * 
	 * @param id
	 *            用户ID
	 * @param roleIds
	 *            角色IDS
	 */
	public void grantRole(String id, String roleIds);

	/**
	 * 修改用户机构
	 * 
	 * @param id
	 *            用户ID
	 * @param organizationIds
	 *            机构IDS
	 */
	public void grantOrganization(String id, String organizationIds);

	/**
	 * 统计用户注册时间图表
	 * 
	 * @return
	 */
	public List<Long> userCreateDatetimeChart();

	/**
	 * 统计?角色的用户
	 * 
	 * @param roleId
	 * @return
	 */
	public Long countUserByRoleId(String roleId);

	/**
	 * 统计没有角色的用户数量
	 * 
	 * @return
	 */
	public Long countUserByNotRoleId();
	
	/**
	 * 查找符合条件的用户(项目)
	 */
	public List<TUser> findProjectUserByFilter(HqlFilter hqlFilter);
	
}
