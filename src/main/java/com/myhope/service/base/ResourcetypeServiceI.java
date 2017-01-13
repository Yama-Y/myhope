package com.myhope.service.base;

import java.util.List;

import com.myhope.model.base.TResourcetype;
import com.myhope.service.BaseServiceI;


/**
 * 资源类型业务
 * 
 * @author YangMing
 * 
 */
public interface ResourcetypeServiceI extends BaseServiceI<TResourcetype> {

	/**
	 * 获取所有资源类型
	 */
	public List<TResourcetype> findResourcetype();

}
