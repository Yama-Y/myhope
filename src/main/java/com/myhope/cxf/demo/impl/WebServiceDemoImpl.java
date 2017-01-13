package com.myhope.cxf.demo.impl;

import javax.jws.WebService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.myhope.cxf.demo.WebServiceDemoI;
import com.myhope.model.base.TUser;
import com.myhope.service.base.UserServiceI;


/**
 * WebService接口实现类
 * 
 * @author YangMing
 * 
 */
@WebService(endpointInterface = "com.myhope.cxf.demo.WebServiceDemoI", serviceName = "webServiceDemo")
public class WebServiceDemoImpl implements WebServiceDemoI {

	@Autowired
	private UserServiceI userService;

	@Override
	public String helloWs(String name) {
		if (StringUtils.isBlank(name)) {
			name = "SunYu";
		}
		String str = "hello[" + name.trim() + "]WebService test ok!";
		System.out.println(str);
		return str;
	}

	@Override
	public TUser getUser(String id) {
		if (StringUtils.isBlank(id)) {
			id = "0";
		}
		TUser user = userService.getById(id.trim());
		TUser u = new TUser();
		u.setName(user.getName());
		u.setAge(user.getAge());
		u.setCreatedatetime(user.getCreatedatetime());
		u.setUpdatedatetime(user.getUpdatedatetime());
		u.setId(user.getId());
		u.setLoginname(user.getLoginname());
		u.setSex(user.getSex());
		u.setPhoto(user.getPhoto());
		return u;
	}

}
