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
			title : '添加项目信息',
			url : myhope.contextPath + '/securityJsp/project/projectForm.jsp',
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
			title : '查看项目信息',
			url : myhope.contextPath + '/securityJsp/project/projectForm.jsp?id=' + id
		});
	};
	var editFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		var dialog = parent.myhope.modalDialog({
			title : '编辑项目信息',
			url : myhope.contextPath + '/securityJsp/project/projectForm.jsp?id=' + id,
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
				$.post(myhope.contextPath + '/project!delete.myhope', {
					id : id
				}, function() {
					grid.datagrid('reload');
				}, 'json');
			}
		});
	};
	var grantUserFun = function(id) {
		if(id == null){
			id = menu_id;
		}
		var dialog = parent.myhope.modalDialog({
			title : '修改项目用户',
			url : myhope.contextPath + '/securityJsp/project/projectUserGrant.jsp?id=' + id,
			buttons : [ 
			<%if (securityUtil.havePermission("/project!grantUsers")) {%>
			{
				text : '修改',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			}
			<%}%>
			]
		});
	};

	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : myhope.contextPath + '/project!grid.myhope',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'id',
			sortOrder : 'desc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [ [ {
				width : '180',
				title : '项目名',
				field : 'name',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				width : '80',
				title : '项目状态',
				field : 'state',
				halign : 'center',
				align : 'center',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 'A':
						return '开始';
					case 'B':
						return '暂停';
					case 'C':
						return '结束';
					}
				}
			} ] ],
			columns : [ [ {
				width : '150',
				title : '创建时间',
				field : 'createdatetime',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				width : '150',
				title : '修改时间',
				field : 'updatedatetime',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				title : '操作',
				field : 'action',
				halign : 'center',
				align : 'center',
				width : '90',
				formatter : function(value, row) {
					var str = '';
					<%if (securityUtil.havePermission("/project!getById")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note" title="查看" onclick="showFun(\'{0}\');"/>', row.id);
						str += myhope.formatString('<img class="iconImg ext-icon-user" title="项目用户" onclick="grantUserFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/project!update")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note_edit" title="编辑" onclick="editFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/project!delete")) {%>
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
								<td>项目名</td>
								<td><input name="QUERY_t#name_S_LK" style="width: 80px;" /></td>
								<td>项目状态</td>
								<td>
									<select name="QUERY_t#state_S_EQ" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
										<option value="">请选择</option>
										<option value="A">开始</option>
										<option value="B">暂停</option>
										<option value="C">结束</option>
									</select>
								</td>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" onclick="grid.datagrid('load',myhope.serializeObject($('#searchForm')));">过滤</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom_out',plain:true" onclick="$('#searchForm input').val('');grid.datagrid('load',{});">重置过滤</a>
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
			<tr>
				<td>
					<table>
						<tr>
							<%if (securityUtil.havePermission("/project!save")) {%>
							<td><a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-note_add',plain:true" onclick="addFun();">添加</a></td>
							<%}%>
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
		<%if (securityUtil.havePermission("/project!getById")) {%>
		<div onclick="showFun();" data-options="iconCls:'iconImg ext-icon-note'">查看</div>
		<%}%>
		<%if (securityUtil.havePermission("/project!update")) {%>
		<div onclick="editFun();" data-options="iconCls:'iconImg ext-icon-note_edit'">编辑</div>
		<%}%>
		<%if (securityUtil.havePermission("/project!grantUsers")) {%>
		<div onclick="grantUserFun();" data-options="iconCls:'iconImg ext-icon-user'">项目用户</div>
		<%}%>
		<%if (securityUtil.havePermission("/project!delete")) {%>
		<div onclick="removeFun();" data-options="iconCls:'iconImg ext-icon-note_delete'">删除</div>
		<%}%>
	</div>
</body>
</html>