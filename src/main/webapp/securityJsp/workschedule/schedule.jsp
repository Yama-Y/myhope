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
	var addFun = function() {
		var dialog = parent.myhope.modalDialog({
			title : '添加班次信息',
			url : myhope.contextPath + '/securityJsp/workschedule/scheduleForm.jsp',
			buttons : [ {
				text : '添加',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var editFun = function(id) {
		var dialog = parent.myhope.modalDialog({
			title : '编辑班次信息',
			url : myhope.contextPath + '/securityJsp/workschedule/scheduleForm.jsp?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var grantOrganizationFun = function(id) {
		var dialog = parent.myhope.modalDialog({
			title : '授权机构',
			url : myhope.contextPath + '/securityJsp/workschedule/scheduleOrganizationGrant.jsp?id=' + id,
			buttons : [ {
				text : '授权',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var editDetailFun = function(id) {
		var dialog = parent.myhope.modalDialog({
			title : '编辑班次规则',
			url : myhope.contextPath + '/securityJsp/workschedule/scheduleDetail.jsp?id=' + id
		});
	};
	var removeFun = function(id) {
		parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(myhope.contextPath + '/schedule!delete.myhope', {
					id : id
				}, function() {
					grid.datagrid('reload');
				}, 'json');
			}
		});
	};

	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : myhope.contextPath + '/schedule!grid.myhope',
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
				width : '80',
				title : '班次颜色',
				field : 'color',
				halign : 'center',
				align : 'center',
				sortable : false,
				formatter : function(value, row, index) {
					return '<div style="background-color:'+value+';width:70px;height:13px"></div>';
				}
			}, {
				width : '100',
				title : '班次名',
				field : 'name',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				width : '80',
				title : '班次类型',
				field : 'type',
				halign : 'center',
				align : 'center',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 'A':
						return '工作班次';
					case 'B':
						return '休假班次';
					}
				}
			}, {
				width : '100',
				title : '班次时间（小时）',
				field : 'workTime',
				halign : 'center',
				align : 'center',
				formatter : function(value, row, index) {
					return value.toFixed(2);
				}
			} ] ],
			columns : [ [ {
				width : '150',
				title : '班次备注',
				field : 'remark',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				width : '80',
				title : '打卡规则数',
				field : 'scheduleDetailsSize',
				halign : 'center',
				align : 'center'
			}, {
				title : '操作',
				field : 'action',
				halign : 'center',
				align : 'center',
				width : '90',
				formatter : function(value, row) {
					var str = '';
					<%if (securityUtil.havePermission("/schedule!update")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note_edit" title="编辑" onclick="editFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/schedule!grantOrganization")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-group" title="授权机构" onclick="grantOrganizationFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/scheduledetail!grid")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-key" title="规则" onclick="editDetailFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/schedule!delete")) {%>
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
								<td>班次名</td>
								<td><input name="QUERY_t#name_S_LK" style="width: 80px;" /></td>
								<td>班次类型</td>
								<td>
									<select name="QUERY_t#type_S_EQ" class="easyui-combobox" data-options="panelHeight:'auto',editable:false">
										<option value="">请选择</option>
										<option value="A">工作班次</option>
										<option value="B">休假班次</option>
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
							<%if (securityUtil.havePermission("/schedule!save")) {%>
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
</body>
</html>