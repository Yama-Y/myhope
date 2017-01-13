<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.myhope.util.base.SecurityUtil"%>
<%
	String contextPath = request.getContextPath();
	SecurityUtil securityUtil = new SecurityUtil(session);
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var grid;
	var menu_id;
	var addFun = function() {
		var dialog = parent.myhope.modalDialog({
			title : '添加用户信息',
			url : myhope.contextPath + '/securityJsp/base/UserForm.jsp',
			buttons : [ {
				text : '添加',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var showFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		var dialog = parent.myhope.modalDialog({
			title : '查看用户信息',
			url : myhope.contextPath + '/securityJsp/base/UserForm.jsp?id=' + id
		});
	};
	var editFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		var dialog = parent.myhope.modalDialog({
			title : '编辑用户信息',
			url : myhope.contextPath + '/securityJsp/base/UserForm.jsp?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var removeFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(myhope.contextPath + '/base/user!delete.myhope', {
					id : id
				}, function() {
					grid.datagrid('reload');
				}, 'json');
			}
		});
	};
	var grantRoleFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		var dialog = parent.myhope.modalDialog({
			title : '修改角色',
			url : myhope.contextPath + '/securityJsp/base/UserRoleGrant.jsp?id=' + id,
			buttons : [ {
				text : '修改',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var grantOrganizationFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		var dialog = parent.myhope.modalDialog({
			title : '修改机构',
			url : myhope.contextPath + '/securityJsp/base/UserOrganizationGrant.jsp?id=' + id,
			buttons : [ {
				text : '修改',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : myhope.contextPath + '/base/user!grid.myhope',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'createdatetime',
			sortOrder : 'desc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [ [ {
				width : '100',
				title : '登录名',
				field : 'loginname',
				sortable : true
			}, {
				width : '80',
				title : '姓名',
				field : 'name',
				sortable : true
			} ] ],
			columns : [ [ {
				width : '150',
				title : '创建时间',
				field : 'createdatetime',
				sortable : true
			}, {
				width : '150',
				title : '修改时间',
				field : 'updatedatetime',
				sortable : true
			}, {
				width : '50',
				title : '性别',
				field : 'sex',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case '0':
						return '女';
					case '1':
						return '男';
					}
				}
			}, {
				width : '50',
				title : '年龄',
				field : 'age',
				hidden : true
			}, {
				width : '250',
				title : '照片',
				field : 'photo',
				formatter : function(value, row) {
					if(value){
						return myhope.formatString('<span title="{0}">{1}</span>', value, value);
					}
				}
			}, {
				title : '操作',
				field : 'action',
				width : '90',
				formatter : function(value, row) {
					var str = '';
					<%if (securityUtil.havePermission("/base/user!getById")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note" title="查看" onclick="showFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/base/user!update")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note_edit" title="编辑" onclick="editFun(\'{0}\');"/>', row.id);
					<%}%>
					if(row.id == '${sessionScope.sessionInfo.user.id}' && str.indexOf("编辑") == -1){
						str += myhope.formatString('<img class="iconImg ext-icon-note_edit" title="编辑" onclick="editFun(\'{0}\');"/>', row.id);
					}
					<%if (securityUtil.havePermission("/base/user!grantRole")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-user" title="用户角色" onclick="grantRoleFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/base/user!grantOrganization")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-group" title="用户机构" onclick="grantOrganizationFun(\'{0}\');"/>', row.id);
					<%}%>						
					<%if (securityUtil.havePermission("/base/user!delete")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note_delete" title="删除" onclick="removeFun(\'{0}\');"/>', row.id);
					<%}%>
					return str;
				}
			} ] ],
			toolbar : '#toolbar',
			onBeforeLoad : function(param) {
				parent.$.messager.progress({
					text : '数据加载中....'
				});
			},
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', myhope.pixel_0);
				parent.$.messager.progress('close');
			},
			onRowContextMenu : function(e, rowIndex, rowData) {
				e.preventDefault();
				menu_id = rowData.id;
				$(this).datagrid('unselectAll');
				$(this).datagrid('selectRow', rowIndex);
				$('#menu').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			}
		});
	});
</script>
</head>
<body class="easyui-layout" data-options="fit:true,border:false">
	<div id="toolbar" style="display: none;">
		<table>
			<tr>
				<td>
					<form id="searchForm">
						<table>
							<tr>
								<td>登录名</td>
								<td><input name="QUERY_t#loginname_S_LK" style="width: 80px;" /></td>
								<td>姓名</td>
								<td><input name="QUERY_t#name_S_LK" style="width: 80px;" /></td>
								<td>性别</td>
								<td><select name="QUERY_t#sex_S_EQ" class="easyui-combobox" data-options="panelHeight:'auto',editable:false"><option value="">请选择</option>
										<option value="1">男</option>
										<option value="0">女</option></select></td>
								<td>创建时间</td>
								<td><input name="QUERY_t#createdatetime_D_GE" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" style="width: 120px;" />-<input name="QUERY_t#createdatetime_D_LE" class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly" style="width: 120px;" /></td>
								<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" onclick="grid.datagrid('load',myhope.serializeObject($('#searchForm')));">过滤</a><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom_out',plain:true" onclick="$('#searchForm input').val('');grid.datagrid('load',{});">重置过滤</a></td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<table>
						<tr>
							<%if (securityUtil.havePermission("/base/user!save")) {%>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-note_add',plain:true" onclick="addFun();">添加</a></td>
							<%}%>
							<!-- 
							<td><div class="datagrid-btn-separator"></div></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_add',plain:true" onclick="">导入</a></td>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="">导出</a></td>
							 -->
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
	
	<div id="menu" class="easyui-menu" style="width:120px;display: none;">
		<%if (securityUtil.havePermission("/base/user!getById")) {%>
		<div onclick="showFun();" data-options="iconCls:'iconImg ext-icon-note'">查看</div>
		<%}%>
		<%if (securityUtil.havePermission("/base/user!update")) {%>
		<div onclick="editFun();" data-options="iconCls:'iconImg ext-icon-note_edit'">编辑</div>
		<%}%>
		<%if (securityUtil.havePermission("/base/user!grantRole")) {%>
		<div onclick="grantRoleFun();" data-options="iconCls:'iconImg ext-icon-user'">用户角色</div>
		<%}%>
		<%if (securityUtil.havePermission("/base/user!grantOrganization")) {%>
		<div onclick="grantOrganizationFun();" data-options="iconCls:'iconImg ext-icon-group'">用户机构</div>
		<%}%>
		<%if (securityUtil.havePermission("/base/user!delete")) {%>
		<div onclick="removeFun();" data-options="iconCls:'iconImg ext-icon-note_delete'">删除</div>
		<%}%>
	</div>
</body>
</html>