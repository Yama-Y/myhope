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
	var sid = myhope.getUrlData('id');
	var grid;
	var addFun = function() {
		var dialog = parent.myhope.modalDialog({
			title : '添加班次规则',
			url : myhope.contextPath + '/securityJsp/workschedule/scheduleDetailForm.jsp?sid=' + sid,
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
			title : '编辑班次规则',
			url : myhope.contextPath + '/securityJsp/workschedule/scheduleDetailForm.jsp?id=' + id,
			buttons : [ {
				text : '编辑',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.submitForm(dialog, grid, parent.$);
				}
			} ]
		});
	};
	var removeFun = function(id) {
		parent.$.messager.confirm('询问', '您确定要删除此记录？', function(r) {
			if (r) {
				$.post(myhope.contextPath + '/scheduledetail!delete.myhope', {
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
			url : myhope.contextPath + '/scheduledetail!grid.myhope?id=' + sid,
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'time',
			sortOrder : 'asc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			frozenColumns : [ [ {
				width : '80',
				title : '班次规则类型',
				field : 'cardType',
				halign : 'center',
				align : 'center',
				formatter : function(value, row, index) {
					switch (value) {
					case 'A':
						return '上班';
					case 'B':
						return '下班';
					case 'C':
						return '中间休息开始';
					case 'D':
						return '中间休息结束';
					}
				}
			}, {
				width : '90',
				title : '规则标准时间',
				field : 'time',
				halign : 'center',
				align : 'center',
				sortable : true,
				formatter : function(value, row, index) {
					return myhope.dateStr2TimeStr(value);
				}
			}, {
				width : '80',
				title : '规则时间类型',
				field : 'timeType',
				halign : 'center',
				align : 'center',
				formatter : function(value, row, index) {
					switch (value) {
					case 'A':
						return '本日';
					case 'B':
						return '次日';
					}
				}
			} ] ],
			columns : [ [ {
				width : '90',
				title : '规则开始时间',
				field : 'begintime',
				halign : 'center',
				align : 'center',
				sortable : true,
				formatter : function(value, row, index) {
					return myhope.dateStr2TimeStr(value);
				}
			}, {
				width : '90',
				title : '规则结束时间',
				field : 'endtime',
				halign : 'center',
				align : 'center',
				sortable : true,
				formatter : function(value, row, index) {
					return myhope.dateStr2TimeStr(value);
				}
			}, {
				width : '70',
				title : '是否打卡',
				field : 'isCard',
				halign : 'center',
				align : 'center',
				sortable : true,
				formatter : function(value, row, index) {
					switch (value) {
					case 'A':
						return '打卡';
					case 'B':
						return '不打卡';
					}
				}
			}, {
				title : '操作',
				field : 'action',
				halign : 'center',
				align : 'center',
				width : '90',
				formatter : function(value, row) {
					var str = '';
					<%if (securityUtil.havePermission("/scheduledetail!update")) {%>
						str += myhope.formatString('<img class="iconImg ext-icon-note_edit" title="编辑" onclick="editFun(\'{0}\');"/>', row.id);
					<%}%>
					<%if (securityUtil.havePermission("/scheduledetail!delete")) {%>
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
								<td>班次规则类型</td>
								<td>
									<select name="QUERY_t#cardType_S_EQ" class="easyui-combobox" data-options="panelHeight:'auto',editable:false" style="width:100px;">
										<option value="">请选择</option>
										<option value="A">上班</option>
										<option value="B">下班</option>
										<option value="C">中间休息开始</option>
										<option value="D">中间休息结束</option>
									</select>
								</td>
								<td>是否打卡</td>
								<td>
									<select name="QUERY_t#isCard_S_EQ" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,required:true" style="width:100px;">
										<option value="">请选择</option>
										<option value="A">打卡</option>
										<option value="B">不打卡</option>
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