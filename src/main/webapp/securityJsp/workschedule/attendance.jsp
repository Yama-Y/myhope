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
	$(function() {
		grid = $('#grid').datagrid({
			title : '',
			url : myhope.contextPath + '/attendance!grid.myhope',
			striped : true,
			rownumbers : true,
			pagination : true,
			singleSelect : true,
			idField : 'id',
			sortName : 'time',
			sortOrder : 'desc',
			pageSize : 50,
			pageList : [ 10, 20, 30, 40, 50, 100, 200, 300, 400, 500 ],
			columns : [ [ {
				width : '100',
				title : '考勤机工号',
				field : 'wcode',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				width : '100',
				title : '姓名',
				field : 'name',
				halign : 'center',
				align : 'center',
				sortable : true
			}, {
				width : '180',
				title : '时间',
				field : 'time',
				halign : 'center',
				align : 'center',
				sortable : true
			} ] ],
			toolbar : '#toolbar',
			onBeforeLoad : function(param) {
				myhope.progressBar({
					title : '加载中...'
				});
			},
			onLoadSuccess : function(data) {
				$('.iconImg').attr('src', myhope.pixel_0);
				myhope.progressBar('close');
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
								<td>考勤机工号</td>
								<td><input name="QUERY_t#wcode_S_EQ"/></td>
								<td>姓名</td>
								<td><input name="QUERY_t#name_S_LK"/></td>
								<td>开始时间</td>
								<td><input class="easyui-datetimebox" name="QUERY_t#time_D_GE" data-options="editable:false,width:150"/></td>
								<td>结束时间</td>
								<td><input class="easyui-datetimebox" name="QUERY_t#time_D_LE" data-options="editable:false,width:150"/></td>
								<td>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" onclick="grid.datagrid('load',myhope.serializeObject($('#searchForm')));">过滤</a>
									<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom_out',plain:true" onclick="$('#searchForm input').val('');grid.datagrid('load',{});">重置过滤</a>
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
	</div>
	<div data-options="region:'center',fit:true,border:false">
		<table id="grid" data-options="fit:true,border:false"></table>
	</div>
</body>
</html>