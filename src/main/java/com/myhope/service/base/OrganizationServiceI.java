package com.myhope.service.base;

import java.util.List;

import com.myhope.model.base.TOrganization;
import com.myhope.service.BaseServiceI;
import com.myhope.util.base.HqlFilter;

/**
 * 机构业务
 * 
 * @author YangMing
 * 
 */
public interface OrganizationServiceI extends BaseServiceI<TOrganization> {

	/**
	 * 更新机构
	 */
	public void updateOrganization(TOrganization organization);

	/**
	 * 机构授权
	 * 
	 * @param id
	 *            机构ID
	 * @param resourceIds
	 *            资源IDS
	 */
	public void grant(String id, String resourceIds);

	/**
	 * 根据用户查找机构
	 */
	public List<TOrganization> findOrganizationByFilter(HqlFilter hqlFilter);

	/**
	 * 保存机构
	 * 
	 * @param data
	 *            机构信息
	 * @param id
	 *            用户ID
	 */
	public void saveOrganization(TOrganization organization, String userId);

}
