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
	var allData;//所有数据
	$(function() {
		myhope.progressBar({title : '数据加载中....'});
		getAllData();
		<%if (securityUtil.havePermission("/workcalendar!workChart_project")) {%>
		$('#container_project').highcharts({
			exporting : {
				filename : '项目时间分布'
			},
			chart : {
				plotBackgroundColor : null,
				plotBorderWidth : null,
				plotShadow : false
			},
			title : {
				text : '项目时间分布'
			},
			tooltip : {
				pointFormat : '{series.name}: <b>{point.y}</b>'
			},
			plotOptions : {
				pie : {
					allowPointSelect : true,
					cursor : 'pointer',
					events: {
						click: function(e) {
							$('#detail_project').dialog({
								title: '项目[' + e.point.name + '] 详细',
								width: 700,
								height: 500,
								modal: true,
								left: 200,
								top: 50
							});
							drow_project_detail(e.point.projectId);
							$('#detail_project').dialog('open');
						}
					},
					dataLabels : {
						enabled : true,
						color : '#000000',
						connectorColor : '#000000',
						formatter : function() {
							return '<b>' + this.point.name + '</b>：' + Math.round(this.point.percentage*100)/100 + '%';
						}
					}
				}
			},
			series : [ {
				type : 'pie',
				name : '项目时间：',
				data : []
			} ]
		});
		drowChart_project();
		<%}%>
		
		<%if (securityUtil.havePermission("/workcalendar!workChart_type")) {%>
		$('#container_type').highcharts({
			exporting : {
				filename : '工作类别时间分布'
			},
			chart : {
				plotBackgroundColor : null,
				plotBorderWidth : null,
				plotShadow : false
			},
			title : {
				text : '工作类别时间分布'
			},
			tooltip : {
				pointFormat : '{series.name}: <b>{point.y}</b>'
			},
			plotOptions : {
				pie : {
					allowPointSelect : true,
					cursor : 'pointer',
					events: {
						click: function(e) {
							$('#detail_type').dialog({
								title: '类别[' + e.point.name + '] 详细',
								width: 700,
								height: 500,
								modal: true,
								left: 200,
								top: 50
							});
							drow_type_detail(e.point.name);
							$('#detail_type').dialog('open');
						}
					},
					dataLabels : {
						enabled : true,
						color : '#000000',
						connectorColor : '#000000',
						formatter : function() {
							return '<b>' + this.point.name + '</b>：' + Math.round(this.point.percentage*100)/100 + '%';
						}
					}
				}
			},
			series : [ {
				type : 'pie',
				name : '工作类别时间：',
				data : []
			} ]
		});
		drowChart_type();
		<%}%>
	});
	
	var getAllData = function(){
		$.post(myhope.contextPath + '/workcalendar!doNotNeedSecurity_workDetail.myhope',myhope.serializeObject($('#searchForm')), function(result) {
			allData = result;
			myhope.progressBar('close');
		}, 'json');
	};
	
	var drowChart_project = function(){
		$.post(myhope.contextPath + '/workcalendar!workChart_project.myhope',myhope.serializeObject($('#searchForm')), function(result) {
			var trs = '';
			$.each(result, function(index, item) {
				trs += myhope.formatString('<tr><td>{0}</td><td>{1}</td></tr>', item.name, item.y);
			});
			$('#table_project').html('<tr><th>项目名称</th><th>项目用时（小时）</th></tr>');
			$('#table_project').append(trs);

			var chart = $('#container_project').highcharts();
			chart.series[0].setData(result);

		}, 'json');
	};
	
	var drow_project_detail = function(projectId){
		var titleArray = new Array();
		$.each(allData, function(i, item){
			if(titleArray.indexOf(this.title) == -1){
				titleArray.push(this.title);
			}
		});
		var str = '<table style="width: 100%; height: 100%" class="table">';
		
		$.each(titleArray, function(i, item){
			var str_tr = '<tr>';
			str_tr = str_tr + '<td style="width: 20px">' + titleArray[i] + '</td>';
			str_tr = str_tr + '<td>';
			var isShow = false;
			$.each(allData, function(j, item){
				if(item.content != undefined && item.type == 'A' && projectId == item.project.id && item.title == titleArray[i]){
					//去除重复
					if(str_tr.indexOf(item.content) == -1){
						str_tr = str_tr + '<div>' + item.content + '</div>';
						isShow = true;
					}
				}
			});
			str_tr = str_tr + '</td>';
			str_tr = str_tr + '</tr>';
			//如果没内容不显示
			if(isShow){
				str = str + str_tr;
			}
		});
		
		str = str + '</table>';
		$('#detail_project').html(str);
	};
	
	var drowChart_type = function(){
		$.post(myhope.contextPath + '/workcalendar!workChart_type.myhope',myhope.serializeObject($('#searchForm')), function(result) {
			var trs = '';
			$.each(result, function(index, item) {
				trs += myhope.formatString('<tr><td>{0}</td><td>{1}</td></tr>', item.name, item.y);
			});
			$('#table_type').html('<tr><th>工作类别</th><th>类别用时（小时）</th></tr>');
			$('#table_type').append(trs);

			var chart = $('#container_type').highcharts();
			chart.series[0].setData(result);

		}, 'json');
	};
	
	var drow_type_detail = function(title){
		var titleArray = new Array();
		$.each(allData, function(i, item){
			if(titleArray.indexOf(this.project.name) == -1){
				titleArray.push(this.project.name);
			}
		});
		var str = '<table style="width: 100%; height: 100%" class="table">';
		
		$.each(titleArray, function(i, item){
			var str_tr = '<tr>';
			str_tr = str_tr + '<td style="width: 20px">' + titleArray[i] + '</td>';
			str_tr = str_tr + '<td>';
			var isShow = false;
			$.each(allData, function(j, item){
				if(item.content != undefined && item.type == 'A' && title == item.title && item.project.name == titleArray[i]){
					//去除重复
					if(str_tr.indexOf(item.content) == -1){
						str_tr = str_tr + '<div>' + item.content + '</div>';
						isShow = true;
					}
				}
			});
			str_tr = str_tr + '</td>';
			str_tr = str_tr + '</tr>';
			//如果没内容不显示
			if(isShow){
				str = str + str_tr;
			}
		});
		
		str = str + '</table>';
		$('#detail_type').html(str);
	};
	
	var fastFun = function(flag){
		myhope.progressBar({title : '数据加载中....'});
		var start_val,end_val;
		//本周
		if(flag == 1){
			start_val = (getWeekStartDate(0)).Format("yyyy-MM-dd 00:00:00");
			end_val = (getWeekStartDate(6)).Format("yyyy-MM-dd 23:59:59");
		}
		//本月
		if(flag == 2){
			start_val = (getCurrentMonthFirst()).Format("yyyy-MM-dd 00:00:00");
			end_val = (getCurrentMonthLast()).Format("yyyy-MM-dd 23:59:59");
		}
		//上月
		if(flag == 3){
			start_val = (getLastMonthFirst()).Format("yyyy-MM-dd 00:00:00");
			end_val = (getLastMonthLast()).Format("yyyy-MM-dd 23:59:59");
		}
		//本年
		if(flag == 4){
			start_val = (new Date()).Format("yyyy-01-01 00:00:00");
			end_val = (new Date()).Format("yyyy-12-31 23:59:59");
		}
		//全部
		if(flag == 5){
			start_val = '';
			end_val = '';
		}
		$('#start').datetimebox('setValue', start_val);
		$('#end').datetimebox('setValue', end_val);
		getAllData();
		<%if (securityUtil.havePermission("/workcalendar!workChart_project")) {%>
		drowChart_project();
		<%}%>
		<%if (securityUtil.havePermission("/workcalendar!workChart_type")) {%>
		drowChart_type();
		<%}%>
	};
	
	/**
	 * 获得当前日期所在周的开始日期
	 */
	function getWeekStartDate(day) {
		var theDate = new Date();
		var firstDateOfWeek;  
		theDate.setDate(theDate.getDate() + 1 + day - theDate.getDay());  
		firstDateOfWeek = theDate;  
		return firstDateOfWeek;  
	}
	
	/**
	 * 获取当前月的第一天
	 */
	function getCurrentMonthFirst() {
		var date = new Date();
		date.setDate(1);
		return date;
	}

	/**
	 * 获取当前月的最后一天
	 */
	function getCurrentMonthLast() {
		var date = new Date();
		var currentMonth = date.getMonth();
		var nextMonth = ++currentMonth;
		var nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
		var oneDay = 1000 * 60 * 60 * 24;
		return new Date(nextMonthFirstDay - oneDay);
	}
	
	/**
	 * 获取上月的第一天
	 */
	function getLastMonthFirst(){
	    return new Date(getCurrentMonthFirst().setMonth(getCurrentMonthFirst().getMonth()-1));
	}
	
	/**
	 * 获取上月的最后一天
	 */
	function getLastMonthLast(){
	    return new Date(getCurrentMonthFirst().setDate(getCurrentMonthFirst().getDate()-1));  
	}
</script>
</head>
<body class="easyui-layout">
	<div data-options="region:'west',title:'快捷操作'" style="width: 200px">
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="fastFun(1);">本周</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="fastFun(2);">本月</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="fastFun(3);">上月</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="fastFun(4);">本年</a>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="fastFun(5);">全部</a>
		<div class="easyui-panel" data-options="title:'数据范围',border:false">
			<form id="searchForm">
				<table>
					<%if (securityUtil.havePermission("/workcalendar!workChartByUser")) {%>
					<tr>
						<td>员工</td>
					</tr>
					<tr>
						<td>
							<select id="user_id" name="data.user.id" class="easyui-combotree" data-options="editable:false,idField:'id',textField:'text',parentField:'pid',lines:'true',url:'<%=contextPath%>/base/user!doNotNeedSecurity_getUsersTree.myhope'" style="width: 170px;"></select><img class="iconImg ext-icon-cross" onclick="$('#user_id').combotree('clear');" title="清空" /></td>
						</td>
					</tr>
					<%}%>
					<tr>
						<td>开始</td>
					</tr>
					<tr>
						<td>
							<input id="start" class="easyui-datetimebox" name="QUERY_t#start_D_GE" data-options="editable:false"/>
						</td>
					</tr>
					<tr>
						<td>结束</td>
					</tr>
					<tr>
						<td>
							<input id="end" class="easyui-datetimebox" name="QUERY_t#end_D_LE" data-options="editable:false"/>
						</td>
					</tr>
					<tr>
						<td>
							<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-zoom',plain:true" 
							onclick="myhope.progressBar({title : '数据加载中....'});
									getAllData();
									<%if (securityUtil.havePermission("/workcalendar!workChart_project")) {%>
									drowChart_project();
									<%}%>
									<%if (securityUtil.havePermission("/workcalendar!workChart_type")) {%>
									drowChart_type();
									<%}%>">过滤</a>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<div data-options="region:'center',split:true">
		<%if (securityUtil.havePermission("/workcalendar!workChart_project")) {%>
		<div class="easyui-panel" data-options="title:'项目分布'">
			<table style="width: 100%; height: 100%">
				<tr>
					<td style="width: 60%"><div id="container_project"></div></td>
					<td>
						<table id="table_project" class="table" style="margin-left: 20px;">
						</table>
					</td>
				</tr>
			</table>
			<div id="detail_project" class="easyui-dialog" data-options="closed:true">
		
			</div>
		</div>
		<%}%>
		<%if (securityUtil.havePermission("/workcalendar!workChart_type")) {%>
		<div class="easyui-panel" data-options="title:'工作类别分布'">
			<table style="width: 100%; height: 100%">
				<tr>
					<td style="width: 60%"><div id="container_type"></div></td>
					<td>
						<table id="table_type" class="table" style="margin-left: 20px;">
						</table>
					</td>
				</tr>
			</table>
			<div id="detail_type" class="easyui-dialog" data-options="closed:true">
		
			</div>
		</div>
		<%}%>
	</div>
</body>
</html>