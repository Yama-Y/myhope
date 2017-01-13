package com.myhope.action.workschedule;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.JdbcUtils;
import com.myhope.action.BaseAction;
import com.myhope.model.workschedule.WsTAttendance;
import com.myhope.service.workschedule.AttendanceServiceI;
import com.mysql.jdbc.Connection;

/**
 * 考勤
 * 
 * action访问地址是/attendance.myhope
 * 
 * @author YangMing
 * 
 */
@Namespace("/")
@Action
public class AttendanceAction extends BaseAction<WsTAttendance> {

	@Autowired
	public void setService(AttendanceServiceI service) {
		this.service = service;
	}

	
	/**
	 * select * from kqz_employee where EmployeeName='杨明'
	 * 
	 * INSERT INTO kqz_card (EmployeeID, CardTime, CardTypeID, DevID, DevClass, ImgStatus) VALUES ('39', '2016-04-21 8:15:22', '0', '1', '1', '1');
	 * 
	 */
	public void doNotNeedSessionAndSecurity_insert() {
		String sql = "INSERT INTO kqz_card (EmployeeID, CardTime, CardTypeID, DevID, DevClass, ImgStatus) VALUES ('?', '?', '0', '1', '1', '1');";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(data.getId());
		parameters.add(data.getTime());
		try {
			JdbcUtils.execute(getConn(), sql, parameters);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void doNotNeedSessionAndSecurity_select() {
		String sql = "select * from kqz_employee where EmployeeName='?';";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(data.getId());
		parameters.add(data.getTime());
		try {
			JdbcUtils.execute(getConn(), sql, parameters);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://192.168.10.10:3306/hwatt";
		String username = "ccone";
		String password = "ccone";
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = (Connection) DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
