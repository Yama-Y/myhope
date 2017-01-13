package com.myhope.service.base;

import java.util.List;

import com.myhope.model.base.TResource;
import com.myhope.service.BaseServiceI;
import com.myhope.util.base.HqlFilter;


/**
 * 资源业务逻辑
 * 
 * @author YangMing
 * 
 */
public interface ResourceServiceI extends BaseServiceI<TResource> {

	/**
	 * 获得资源树，或者combotree(只查找菜单类型的节点)
	 * 
	 * @return
	 */
	public List<TResource> getMainMenu(HqlFilter hqlFilter);

	/**
	 * 获得资源treeGrid
	 * 
	 * @return
	 */
	public List<TResource> resourceTreeGrid(HqlFilter hqlFilter);

	/**
	 * 更新资源
	 */
	public void updateResource(TResource resource);

	/**
	 * 保存一个资源
	 * 
	 * @param resource
	 * @param userId
	 * @return
	 */
	public void saveResource(TResource resource, String userId);

	/**
	 * 查找符合条件的资源
	 */
	public List<TResource> findResourceByFilter(HqlFilter hqlFilter);

}
