package com.myhope.model.base;

/**
 * sessionInfo模型，只要登录成功，就需要设置到session里面，便于系统使用
 * 
 * @author YangMing
 * 
 */
public class SessionInfo implements java.io.Serializable {

	private TUser user;

	public TUser getUser() {
		return user;
	}

	public void setUser(TUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return user.getLoginname();
	}

}
