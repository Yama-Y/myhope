package com.myhope.service.base;

import java.util.List;

import com.myhope.model.base.TRole;
import com.myhope.service.BaseServiceI;
import com.myhope.util.base.HqlFilter;


/**
 * 角色业务
 * 
 * @author YangMing
 * 
 */
public interface RoleServiceI extends BaseServiceI<TRole> {

	/**
	 * 查找角色
	 * 
	 * @param hqlFilter
	 * @param page
	 * @param rows
	 * @return
	 */
	public List<TRole> findRoleByFilter(HqlFilter hqlFilter, int page, int rows);

	/**
	 * 查找角色
	 */
	public List<TRole> findRoleByFilter(HqlFilter hqlFilter);

	/**
	 * 统计角色
	 * 
	 * @param hqlFilter
	 * @return
	 */
	public Long countRoleByFilter(HqlFilter hqlFilter);

	/**
	 * 添加一个角色
	 * 
	 * @param data
	 * @param userId
	 */
	public void saveRole(TRole role, String userId);

	/**
	 * 为角色授权
	 * 
	 * @param id
	 *            角色ID
	 * @param resourceIds
	 *            资源IDS
	 */
	public void grant(String id, String resourceIds);

}
