<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.myhope.model.base.SessionInfo"%>
<%
	String contextPath = request.getContextPath();
	SessionInfo sessionInfo = (SessionInfo) session.getAttribute("sessionInfo");
%>
<!DOCTYPE html>
<html>
<head>
<title>MyHope</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	var mainMenu;
	var mainTabs;
	var mainTabsMenu;
	
	$(function() {
		var loginFun = function() {
			if ($('#loginDialog form').form('validate')) {
				$('#loginBtn').linkbutton('disable');
				$.post(myhope.contextPath + '/base/user!doNotNeedSessionAndSecurity_login.myhope', $('#loginDialog form').serialize(), function(result) {
					if (result.success) {
						$('#loginDialog').dialog('close');
					} else {
						$.messager.alert('提示', result.msg, 'error', function() {
							$('#loginDialog form :input:eq(1)').focus();
						});
					}
					$('#loginBtn').linkbutton('enable');
				}, 'json');
			}
		};
		$('#loginDialog').show().dialog({
			modal : true,
			closable : false,
			iconCls : 'ext-icon-lock_open',
			buttons : [ {
				id : 'loginBtn',
				text : '登录',
				handler : function() {
					loginFun();
				}
			} ],
			onOpen : function() {
				$('#loginDialog form :input[name="data.pwd"]').val('');
				$('form :input').keyup(function(event) {
					if (event.keyCode == 13) {
						loginFun();
					}
				});
			}
		}).dialog('close');

		$('#passwordDialog').show().dialog({
			modal : true,
			closable : true,
			iconCls : 'ext-icon-lock_edit',
			buttons : [ {
				text : '修改',
				handler : function() {
					if ($('#passwordDialog form').form('validate')) {
						$.post(myhope.contextPath + '/base/user!doNotNeedSecurity_updateCurrentPwd.myhope', {
							'data.pwd' : $('#pwd').val()
						}, function(result) {
							if (result.success) {
								$.messager.alert('提示', '密码修改成功！', 'info');
								$('#passwordDialog').dialog('close');
							}
						}, 'json');
					}
				}
			} ],
			onOpen : function() {
				$('#passwordDialog form :input').val('');
			}
		}).dialog('close');

		mainMenu = $('#mainMenu').tree({
			url : myhope.contextPath + '/base/resource!doNotNeedSecurity_getMainMenu.myhope',
			parentField : 'pid',
			lines : true,
			animate : true,
			onClick : function(node) {
				if (node.attributes.url) {
					var src = myhope.contextPath + node.attributes.url;
					if (!myhope.startWith(node.attributes.url, '/')) {
						src = node.attributes.url;
					}
					if (node.attributes.target && node.attributes.target.length > 0) {
						window.open(src, node.attributes.target);
					} else {
						var tabs = $('#mainTabs');
						var opts = {
							title : node.text,
							closable : true,
							iconCls : node.iconCls,
							content : myhope.formatString('<iframe src="{0}" allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"></iframe>', src),
							border : false,
							fit : true
						};
						if (tabs.tabs('exists', opts.title)) {
							tabs.tabs('select', opts.title);
						} else {
							tabs.tabs('add', opts);
						}
					}
				}
			}
		});

		$('#mainLayout').layout('panel', 'center').panel({
			onResize : function(width, height) {
				myhope.setIframeHeight('centerIframe', $('#mainLayout').layout('panel', 'center').panel('options').height - 5);
			}
		});

		mainTabs = $('#mainTabs').tabs({
			fit : true,
			border : false,
			tools : [ 
			/*
			{
				iconCls : 'ext-icon-arrow_up',
				handler : function() {
					mainTabs.tabs({
						tabPosition : 'top'
					});
				}
			}, {
				iconCls : 'ext-icon-arrow_left',
				handler : function() {
					mainTabs.tabs({
						tabPosition : 'left'
					});
				}
			}, {
				iconCls : 'ext-icon-arrow_down',
				handler : function() {
					mainTabs.tabs({
						tabPosition : 'bottom'
					});
				}
			}, {
				iconCls : 'ext-icon-arrow_right',
				handler : function() {
					mainTabs.tabs({
						tabPosition : 'right'
					});
				}
			},
			*/
			{
				text : '刷新',
				iconCls : 'ext-icon-arrow_refresh',
				handler : function() {
					var panel = mainTabs.tabs('getSelected').panel('panel');
					var frame = panel.find('iframe');
					try {
						if (frame.length > 0) {
							for (var i = 0; i < frame.length; i++) {
								frame[i].contentWindow.document.write('');
								frame[i].contentWindow.close();
								frame[i].src = frame[i].src;
							}
							if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
								try {
									CollectGarbage();
								} catch (e) {
								}
							}
						}
					} catch (e) {
					}
				}
			}, {
				text : '关闭',
				iconCls : 'ext-icon-cross',
				handler : function() {
					var index = mainTabs.tabs('getTabIndex', mainTabs.tabs('getSelected'));
					var tab = mainTabs.tabs('getTab', index);
					if (tab.panel('options').closable) {
						mainTabs.tabs('close', index);
					} else {
						$.messager.alert('提示', '[' + tab.panel('options').title + ']不可以被关闭！', 'error');
					}
				}
			} ]
		});
		
		mainTabsMenu = $('#mainTabsMenu').menu({
			onClick : function(item) {
				var curTabTitle = $(this).data('tabTitle');
				var type = $(item.target).attr('type');
				var tab = mainTabs.tabs('getTab', curTabTitle);
				
				if (type === 'refresh') {
					var frame = tab.find('iframe');
					try {
						if (frame.length > 0) {
							for (var i = 0; i < frame.length; i++) {
								frame[i].contentWindow.document.write('');
								frame[i].contentWindow.close();
								frame[i].src = frame[i].src;
							}
							if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
								try {
									CollectGarbage();
								} catch (e) {
								}
							}
						}
					} catch (e) {
					}

					return;
				}

				if (type === 'close') {
					var index = mainTabs.tabs('getTabIndex', tab);
					if (tab.panel('options').closable) {
						mainTabs.tabs('close', index);
					} else {
						$.messager.alert('提示', '[' + tab.panel('options').title + ']不可以被关闭！', 'error');
					}
					return;
				}

				var allTabs = mainTabs.tabs('tabs');
				var closeTabsTitle = [];

				$.each(allTabs, function() {
					var opt = $(this).panel('options');
					if (opt.closable && opt.title != curTabTitle && type === 'closeOther') {
						closeTabsTitle.push(opt.title);
					} else if (opt.closable && type === 'closeAll') {
						closeTabsTitle.push(opt.title);
					}
				});

				for ( var i = 0; i < closeTabsTitle.length; i++) {
					mainTabs.tabs('close', closeTabsTitle[i]);
				}
			}
		});
		mainTabs = $('#mainTabs').tabs({
			fit : true,
			border : false,
			onContextMenu : function(e, title) {
				e.preventDefault();
				mainTabsMenu.menu('show', {
					left : e.pageX,
					top : e.pageY
				}).data('tabTitle', title);
			}
		});

	});
</script>
</head>
<body id="mainLayout" class="easyui-layout">
	<div data-options="region:'north',href:'<%=contextPath%>/securityJsp/north.jsp'" style="height: 70px; overflow: hidden;"></div>
	<div data-options="region:'west',href:'',split:true" title="导航" style="width: 200px; padding: 10px;">
		<ul id="mainMenu"></ul>
	</div>
	<div data-options="region:'center'" style="overflow: hidden;">
		<div id="mainTabs">
			<div title="主页" data-options="iconCls:'ext-icon-heart'">
				<iframe src="<%=contextPath%>/welcome.jsp" allowTransparency="true" style="border: 0; width: 100%; height: 99%;" frameBorder="0"></iframe>
			</div>
		</div>
	</div>
	<div data-options="region:'south',href:'<%=contextPath%>/securityJsp/south.jsp',border:false" style="height: 30px; overflow: hidden;"></div>

	<div id="loginDialog" title="解锁登录" style="display: none;">
		<form method="post" class="form" onsubmit="return false;">
			<table class="table">
				<tr>
					<th width="50">登录名</th>
					<td><%=sessionInfo.getUser().getLoginname()%><input name="data.loginname" readonly="readonly" type="hidden" value="<%=sessionInfo.getUser().getLoginname()%>" /></td>
				</tr>
				<tr>
					<th>密码</th>
					<td><input name="data.pwd" type="password" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="passwordDialog" title="修改密码" style="display: none;">
		<form method="post" class="form" onsubmit="return false;">
			<table class="table">
				<tr>
					<th>新密码</th>
					<td><input id="pwd" name="data.pwd" type="password" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<th>重复密码</th>
					<td><input type="password" class="easyui-validatebox" data-options="required:true,validType:'eqPwd[\'#pwd\']'" /></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="mainTabsMenu" style="width: 120px;display:none;">
		<div type="refresh">刷新</div>
		<div class="menu-sep"></div>
		<div type="close">关闭</div>
		<div type="closeOther">关闭其他</div>
		<div type="closeAll">关闭所有</div>
	</div>
</body>
</html>