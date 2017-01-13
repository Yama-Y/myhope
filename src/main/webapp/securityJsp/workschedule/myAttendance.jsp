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
		$('#beginDate').datebox('setValue', getCurrentMonthFirst().Format("yyyy-MM-dd"));
		$('#endDate').datebox('setValue', new Date().Format("yyyy-MM-dd"));
		grid = $('#grid').datagrid({
			title : '',
			url : myhope.contextPath + '/workschedule!doNotNeedSecurity_attendance_my.myhope',
			striped : true,
			rownumbers : true,
			pagination : false,
			singleSelect : false,
			sortName : 'normalDate',
			sortOrder : 'desc',
			columns : [ [ {
				width : '180',
				title : '【标准】打卡时间',
				field : 'normalDate',
				halign : 'center',
				align : 'center',
				sortable : false
			}, {
				width : '150',
				title : '打卡类型',
				field : 'type',
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
				width : '180',
				title : '【实际】打卡时间',
				field : 'doDate',
				halign : 'center',
				align : 'center',
				sortable : false
			}, {
				width : '300',
				title : '状态',
				field : 'wrongTime',
				halign : 'center',
				align : 'center',
				formatter : function(value, row, index) {
					if(value == -1){
						return '<div style="background-color:#ff7c7a;width:290px;">无有效打卡</div>';
					} else if(value == 0){
						return '<div style="background-color:#b2ff4e;width:290px;">正常</div>';
					} else{
						var wrongTime_str;
						if (row.type == 'A') {
							wrongTime_str = '上班迟到：';
						} else if (row.type == 'B'){
							wrongTime_str = '下班早退：';
						} else if (row.type == 'C'){
							wrongTime_str = '中间休息下班早退：';
						} else if (row.type == 'D'){
							wrongTime_str = '中间休息上班迟到：';
						}
						return '<div style="background-color:#ff7c7a;width:290px;">' + wrongTime_str + Math.round((value/(1000*60))*100)/100 + '分钟</div>';
					}
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
	/**
	 * 获取当前月的第一天
	 */
	function getCurrentMonthFirst() {
		var date = new Date();
		date.setDate(1);
		return date;
	}
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
								<td>员工</td>
								<td>
									<select id="user_id" name="data.user.id" class="easyui-combotree" data-options="editable:false,idField:'id',textField:'text',parentField:'pid',lines:'true',url:'<%=contextPath%>/base/user!doNotNeedSecurity_getUsersTree.myhope'" style="width: 250px;"></select><img class="iconImg ext-icon-cross" onclick="$('#user_id').combotree('clear');" title="清空" /></td>
								</td>
								<td><div class="datagrid-btn-separator"></div></td>
								<td>开始时间</td>
								<td><input id="beginDate" class="easyui-datebox" name="id" data-options="editable:false,width:120"/></td>
								<td>结束时间</td>
								<td>
									<input id="endDate" class="easyui-datebox" name="ids" data-options="editable:false,width:120"/>
								</td>
								<td>
									<input type="checkbox" name="q" />
								</td>
								<td>
									仅显示异常
								</td>
								<td><div class="datagrid-btn-separator"></div></td>
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