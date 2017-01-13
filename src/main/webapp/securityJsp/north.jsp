<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.myhope.model.base.SessionInfo"%>
<%
	String contextPath = request.getContextPath();
	SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
%>
<script type="text/javascript" charset="utf-8">
	var lockWindowFun = function() {
		$.post(myhope.contextPath + '/base/user!doNotNeedSessionAndSecurity_logout.myhope', function(result) {
			$('#loginDialog').dialog('open');
		}, 'json');
	};
	var logoutFun = function() {
		$.post(myhope.contextPath + '/base/user!doNotNeedSessionAndSecurity_logout.myhope', function(result) {
			location.replace(myhope.contextPath + '/index.jsp');
		}, 'json');
	};
	var showMyInfoFun = function() {
		var dialog = parent.myhope.modalDialog({
			title : '我的信息',
			url : myhope.contextPath + '/securityJsp/userInfo.jsp'
		});
	};
	$(function(){
		if('${sessionScope.sessionInfo.user.photo}' != ''){
			$('#north_userPhoto').attr('src','${sessionScope.sessionInfo.user.photo}');
		}
	});
</script>
<div class="logo"><div style="height: 70px;width:120px;float: left;"/><img id="north_userPhoto" style="height: 70px;"/></div>
<div id="sessionInfoDiv" style="position: absolute; right: 10px; top: 5px;">
	<%
		if (sessionInfo != null) {
			out.print(com.myhope.util.base.StringUtil.formateString("欢迎您，{0}", sessionInfo.getUser().getName()));
		}
	%>
</div>
<div style="position: absolute; right: 0px; bottom: 0px;">
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_pfMenu',iconCls:'ext-icon-rainbow'">更换皮肤</a> 
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_kzmbMenu',iconCls:'ext-icon-cog'">控制面板</a> 
	<a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_zxMenu',iconCls:'ext-icon-disconnect'">注销</a>
</div>
<div id="layout_north_pfMenu" style="width: 120px; display: none;">
	<div onclick="myhope.changeTheme('default');" title="default">default</div>
	<div onclick="myhope.changeTheme('gray');" title="gray">gray</div>
	<div onclick="myhope.changeTheme('metro');" title="metro">metro</div>
	<div onclick="myhope.changeTheme('black');" title="black">black</div>
	<div onclick="myhope.changeTheme('bootstrap');" title="bootstrap">bootstrap</div>
	<div class="menu-sep"></div>
	<div onclick="myhope.changeTheme('metro-blue');">metro-blue</div>
	<div onclick="myhope.changeTheme('metro-gray');">metro-gray</div>
	<div onclick="myhope.changeTheme('metro-green');">metro-green</div>
	<div onclick="myhope.changeTheme('metro-orange');">metro-orange</div>
	<div onclick="myhope.changeTheme('metro-red');">metro-red</div>
	<div class="menu-sep"></div>
	<div onclick="myhope.changeTheme('ui-cupertino');">cupertino</div>
	<div onclick="myhope.changeTheme('ui-dark-hive');">dark-hive</div>
	<div onclick="myhope.changeTheme('ui-pepper-grinder');">pepper-grinder</div>
	<div onclick="myhope.changeTheme('ui-sunny');">sunny</div>
</div>
<div id="layout_north_kzmbMenu" style="width: 100px; display: none;">
	<div data-options="iconCls:'ext-icon-user_edit'" onclick="$('#passwordDialog').dialog('open');">修改密码</div>
	<div class="menu-sep"></div>
	<div data-options="iconCls:'ext-icon-user'" onclick="showMyInfoFun();">我的信息</div>
</div>
<div id="layout_north_zxMenu" style="width: 100px; display: none;">
	<div data-options="iconCls:'ext-icon-lock'" onclick="lockWindowFun();">锁定窗口</div>
	<div class="menu-sep"></div>
	<div data-options="iconCls:'ext-icon-door_out'" onclick="logoutFun();">退出系统</div>
</div>