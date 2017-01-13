<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript" src="../../jslib/workSchedule/workSchedule.js"></script>
<link rel="stylesheet" href="../../jslib/workSchedule/workSchedule.css" type="text/css"></link>
<script type="text/javascript">
	$(function() {
		myhope.progressBar({title : '初始化中...'});
		$('#littleC').calendar({
			current : new Date(),
			border : false,
			onSelect : function(date) {
				$(this).calendar('moveTo', date);
				ajaxGetSchedule(getWeekStartDate(new Date(date),0),getWeekStartDate(new Date(date),6));
			}
		});
        $('#button_bianji').switchbutton({
            checked: false,
            onChange: function(checked){
            	editSchedule(checked);
            }
        });
	});
	
	/**
	 *  ajax请求排班数据，并刷新数据展示
	 */
	function ajaxGetSchedule(sDate,eDate) {
		var url = myhope.contextPath + '/workschedule!getDates.myhope?id=' + sDate.Format("yyyy-MM-dd") + ',' + eDate.Format("yyyy-MM-dd");
		$.post(url, function(result) {
			if("" != result){
				init(result,sDate);
				myhope.progressBar('close');
		    }
		}, 'text');
	}
	
	/**
	 * ajax新增排班保存到数据库
	 */
	function ajaxScheduleAdd(workscheduleDivId,scheduleId){
		myhope.progressBar({title : '添加中...'});
		var datas = workscheduleDivId.split('_');
		$('form').form('load', {
			'data.date' : datas[1],
			'data.user.id' : datas[0],
			'data.schedule' : scheduleId
		});
		var url = myhope.contextPath + '/workschedule!doNotNeedSecurity_save.myhope';
		$.post(url, myhope.serializeObject($('form')), function(result) {
			myhope.progressBar('close');
			if(result){
				// 新增排班数据到页面中
				addSchedule(workscheduleDivId, result, scheduleId); //param: 排班id：data；班次id：scheduleId。
		    }else{
		    	$.messager.alert('警告','添加失败！'); // 添加失败
			}
		}, 'json');
	}
	
	/**
	 * ajax删除数据库中排班信息
	 */
	 function ajaxScheduleDelete(workSche_id) {
		myhope.progressBar({title : '删除中...'});
		$('form').form('load', {
			'id' : workSche_id
		});
		var url = myhope.contextPath + '/workschedule!doNotNeedSecurity_delete.myhope';
		$.post(url, myhope.serializeObject($('form')), function(result) {
			myhope.progressBar('close');
			if (result) {
				// 将排班数据从页面中删除
				delSchedule(workSche_id);
			} else {
				$.messager.alert('警告','删除失败！');
			}
		}, 'text');
	}
</script>
<style>
</style>
</head>
<body class="easyui-layout wtClass">
	<div data-options="region:'west',title:'快捷操作'" style="width: 200px">
		<div class="easyui-panel" data-options="border:false">
			<div id="littleC" style="width: 196px"></div>
		</div>
		<div class="easyui-panel" data-options="title:'当前排班状态',border:false">
			编辑<input id="button_bianji"/>
			<!-- 
			<a href="javascript:void(0);"
				class="easyui-linkbutton"
				data-options="iconCls:'ext-icon-table_go',plain:true"
				onclick="autoSchedule();">自动排班</a>
			 -->
		</div>
	</div>
	<div data-options="region:'center',split:true" >
		<div>
			<div class="date_div">
				<a href="javascript:void(0);" class="easyui-linkbutton" onclick="preWeekSchedule();">
				     上一周
			    </a>
			    <a href="javascript:void(0);" class="easyui-linkbutton" onclick="nowWeekSchedule();">
				     本周
			    </a>
				<a href="javascript:void(0);" class="easyui-linkbutton" onclick="nextWeekSchedule();">
				     下一周
				</a>
			</div>
		</div>
		<div>
			<div id='calendar_head'>本周 (2016 年 1 月 4 日 - 2016 年 1 月 10 日)排班表</div>
			<table id="schedule_table" class="schedule_table table" style="width:100%;height:auto;">
				<thead>
					<tr>
						<th style="min-width: 80px" width="10%">姓名</th>
						<th style="min-width: 100px" width="12%" id="weekTh_0"></th>
						<th style="min-width: 100px" width="12%" id="weekTh_1"></th>
						<th style="min-width: 100px" width="12%" id="weekTh_2"></th>
						<th style="min-width: 100px" width="12%" id="weekTh_3"></th>
						<th style="min-width: 100px" width="12%" id="weekTh_4"></th>
						<th style="min-width: 100px" width="12%" id="weekTh_5"></th>
						<th style="min-width: 100px" width="12%" id="weekTh_6"></th>
						<th style="min-width: 50px" width="6%">累计时间（小时）</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<form method="post" class="form">
			<input name="id" type="hidden"/>
			<input name="data.date" type="hidden"/>
			<input name="data.user.id" type="hidden"/>
			<input name="data.schedule" type="hidden"/>
		</form>
	</div>
</body>
</html>