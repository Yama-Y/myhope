package com.myhope.cxf.demo;

import javax.jws.WebParam;
import javax.jws.WebService;

import com.myhope.model.base.TUser;


/**
 * WebService接口定义
 * 
 * @author YangMing
 * 
 */
@WebService
public interface WebServiceDemoI {

	public String helloWs(@WebParam(name = "userName") String name);

	public TUser getUser(@WebParam(name = "userId") String id);

}
