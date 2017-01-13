package com.myhope.action.base;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.action.BaseAction;
import com.myhope.model.base.TResourcetype;
import com.myhope.service.base.ResourcetypeServiceI;


/**
 * 资源类型
 * 
 * @author YangMing
 * 
 */
@Namespace("/base")
@Action
public class ResourcetypeAction extends BaseAction<TResourcetype> {

	/**
	 * 注入业务逻辑，使当前action调用service.xxx的时候，直接是调用基础业务逻辑
	 * 
	 * 如果想调用自己特有的服务方法时，请使用((TServiceI) service).methodName()这种形式强转类型调用
	 * 
	 * @param service
	 */
	@Autowired
	public void setService(ResourcetypeServiceI service) {
		this.service = service;
	}

	/**
	 * 获得资源类型combobox
	 */
	public void doNotNeedSecurity_combobox() {
		writeJson(((ResourcetypeServiceI) service).findResourcetype());
	}

}
