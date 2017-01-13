<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<%
	String id = request.getParameter("id");
	if (id == null) {
		id = "";
	}
%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<jsp:include page="../../inc.jsp"></jsp:include>
<script type="text/javascript">
	var submitForm = function($dialog, $grid, $pjq) {
		if ($('form').form('validate')) {
			var url;
			if ($(':input[name="data.id"]').val().length > 0) {
				url = myhope.contextPath + '/scheduledetail!update.myhope';
			} else {
				url = myhope.contextPath + '/scheduledetail!save.myhope';
			}
			$.post(url, myhope.serializeObject($('form')), function(result) {
				if (result.success) {
					$grid.datagrid('load');
					$dialog.dialog('destroy');
				} else {
					$pjq.messager.alert('提示', result.msg, 'error');
				}
			}, 'json');
		}
	};
	$(function() {
		if ($(':input[name="data.id"]').val().length > 0) {
			parent.$.messager.progress({
				text : '数据加载中....'
			});
			$.post(myhope.contextPath + '/scheduledetail!getById.myhope', {
				id : $(':input[name="data.id"]').val()
			}, function(result) {
				if (result.id != undefined) {
					$('form').form('load', {
						'data.id' : result.id,
						'data.cardType' : result.cardType,
						'data.timeType' : result.timeType,
						'data.time' : myhope.dateStr2TimeStr(result.time),
						'data.begintime' : myhope.dateStr2TimeStr(result.begintime),
						'data.endtime' : myhope.dateStr2TimeStr(result.endtime),
						'data.isCard' : result.isCard,
						'data.schedule.id' : result.schedule.id
					});
				}
				parent.$.messager.progress('close');
			}, 'json');
		}else{
			$('form').form('load', {
				'data.time' : "00:00:00",
				'data.begintime' : "00:00:00",
				'data.endtime' : "00:00:00",
				'data.timeType' : "A",
				'data.isCard' : "A",
				'data.schedule.id' : myhope.getUrlData('sid')
			});
		}
	});
</script>
</head>
<body>
	<form method="post" class="form">
		<fieldset>
			<legend>班次规则信息</legend>
			<table class="table" style="width: 100%;">
				<input name="data.schedule.id" style="visibility: hidden;"/>
				<input name="data.id" value="<%=id%>" style="visibility: hidden;" />
				<tr>
					<th>班次规则类型</th>
					<td>
						<select name="data.cardType" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,required:true">
							<option value="">请选择</option>
							<option value="A">上班</option>
							<option value="B">下班</option>
							<option value="C">中间休息开始</option>
							<option value="D">中间休息结束</option>
						</select>
					</td>
					<th>规则标准时间</th>
					<td><input name="data.time" class="easyui-timespinner" data-options="required:true,showSeconds:true" /></td>
				</tr>
				<tr>
					<th>是否打卡</th>
					<td>
						<select name="data.isCard" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,required:true">
							<option value="">请选择</option>
							<option value="A">打卡</option>
							<option value="B">不打卡</option>
						</select>
					</td>
					<th>规则开始时间</th>
					<td><input name="data.begintime" class="easyui-timespinner" data-options="required:true,showSeconds:true" /></td>
				</tr>
				<tr>
					<th>结束时间类型</th>
					<td>
						<select name="data.timeType" class="easyui-combobox" data-options="panelHeight:'auto',editable:false,required:true">
							<option value="">请选择</option>
							<option value="A">本日</option>
							<option value="B">次日</option>
						</select>
					</td>
					<th>规则结束时间</th>
					<td><input name="data.endtime" class="easyui-timespinner" data-options="required:true,showSeconds:true" /></td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>