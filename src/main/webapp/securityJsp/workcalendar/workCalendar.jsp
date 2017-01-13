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
<link href='../../jslib/fullcalendar-2.4.0/fullcalendar.css' rel='stylesheet' />
<link href='../../jslib/fullcalendar-2.4.0/fullcalendar.print.css' rel='stylesheet' media='print' />
<script src='../../jslib/fullcalendar-2.4.0/lib/moment.min.js'></script>
<script src="../../jslib/fullcalendar-2.4.0/lib/jquery-ui.custom.min.js"></script>
<script src='../../jslib/fullcalendar-2.4.0/fullcalendar.js'></script>
<script src='../../jslib/fullcalendar-2.4.0/lang/zh-cn.js'></script>
<script type="text/javascript">
var eventResizeStart_Start;
var eventResizeStart_end;
$(document).ready(
	function() {
		$('#calendar').fullCalendar(
				{
					header : {
						left : 'prev,next today',
						center : 'title',
						right : 'agendaWeek,agendaDay'
					},
					defaultView:'agendaDay',
					defaultDate : new Date(),
					allDaySlot : false,
					unselectAuto : false,
					droppable : true,
					selectable : true,
					selectHelper : true,
					weekNumbers : true,
					minTime : '06:00:00',
					maxTime : '20:00:00',
					dropAccept: '.external-event',
					//日历外拖拽释放
					drop: function(date, jsEvent, ui) {
						var originalEventObject = $(this).data('eventObject');
						$('#eventForm').dialog({
							iconCls:'ext-icon-note_add',
							title: '添加记录',
							modal : true,
							buttons:[{
								text:'添加',
								iconCls:'ext-icon-note_add',
								handler:function(){
									if ($('form').form('validate')) {
										myhope.progressBar({
											title : '添加中'
										});
										var url = myhope.contextPath + '/workcalendar!addEvent.myhope';
										$.post(url, myhope.serializeObject($('form')), function(result) {
											myhope.progressBar('close');
											if (result.success) {
												$('#calendar').fullCalendar('renderEvent',result.obj, true);
												$('#eventForm').dialog('close');
											} else {
												$('#eventForm').dialog('close');
												$.messager.alert('警告','添加失败！');
											}
										}, 'json').error(function() {
															myhope.progressBar('close');
															$('#eventForm').dialog('close');
															$.messager.alert('警告','未知异常，请重试');
														});
									}
								}
							}]
						});
						var drop_endDate_S = $.fullCalendar.formatDate(date, "YYYY-MM-DD HH:mm:ss");
						$('form').form('load', {
							'id' : '',
							'data.id' : '',
							'data.start' : $.fullCalendar.formatDate(date, "YYYY-MM-DD HH:mm:ss"),
							'data.end' : drop_endDate_S.substr(0,11)+((drop_endDate_S.substr(11,2)-0)+2)+drop_endDate_S.substr(13,6),
							'data.project.id' : originalEventObject.project.id,
							'data.type' : 'A',
							'data.title' : originalEventObject.title,
							'data.content' : originalEventObject.content
						});
						$('#eventForm').dialog('open');
						
					}, 
					//拖拽完成并且时间改变时
					eventDrop: function( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ) {
						myhope.progressBar({
							title : '修改中'
						});
						$('form').form('load', {
							'id' : event.id,
							'data.id' : event.id,
							'data.start' : $.fullCalendar.formatDate(event.start, "YYYY-MM-DD HH:mm:ss"),
							'data.end' : $.fullCalendar.formatDate(event.end, "YYYY-MM-DD HH:mm:ss"),
							'data.project.id' : event.project.id,
							'data.type' : event.type,
							'data.title' : event.title,
							'data.content' : event.content
						});
						var url = myhope.contextPath + '/workcalendar!update.myhope';
						$.post(url, myhope.serializeObject($('form')), function(result) {
							myhope.progressBar('close');
							if (result.success) {
								myhope.progressBar('close');
							} else {
								$('#eventForm').dialog('close');
								$.messager.alert('警告','修改失败！');
							}
						}, 'json').error(function() {
											myhope.progressBar('close');
											$('#eventForm').dialog('close');
											$.messager.alert('警告','未知异常，请重试');
										});
					},
					eventResizeStart : function( event, jsEvent, ui, view ) {
						eventResizeStart_Start = event.start;
						eventResizeStart_end = event.end;
					},
					//日程事件改变大小并成功后
					eventResize: function( event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ) {
						if((($.fullCalendar.formatDate(event.end, "DD")-0)-($.fullCalendar.formatDate(event.start, "DD")-0)) > 0){
							$('#calendar').fullCalendar('unselect');
							event.start = eventResizeStart_Start;
							event.end = eventResizeStart_end;
							$('#calendar').fullCalendar('updateEvent',event);
							$.messager.alert('警告','请勿跨日拖拽！');
							return;
						}
						myhope.progressBar({
							title : '修改中'
						});
						$('form').form('load', {
							'id' : event.id,
							'data.id' : event.id,
							'data.start' : $.fullCalendar.formatDate(event.start, "YYYY-MM-DD HH:mm:ss"),
							'data.end' : $.fullCalendar.formatDate(event.end, "YYYY-MM-DD HH:mm:ss"),
							'data.project.id' : event.project.id,
							'data.type' : event.type,
							'data.title' : event.title,
							'data.content' : event.content
						});
						var url = myhope.contextPath + '/workcalendar!update.myhope';
						$.post(url, myhope.serializeObject($('form')), function(result) {
							myhope.progressBar('close');
							if (result.success) {
								myhope.progressBar('close');
							} else {
								$('#eventForm').dialog('close');
								$.messager.alert('警告','修改失败！');
							}
						}, 'json').error(function() {
											myhope.progressBar('close');
											$('#eventForm').dialog('close');
											$.messager.alert('警告','未知异常，请重试');
										});
					},
					//事件点击
					eventClick: function(event, jsEvent, view) {
						$('#eventForm').dialog({
							iconCls:'ext-icon-note_add',
							title: '事件内容',
							modal : true,
							buttons:[{
								text:'修改',
								iconCls:'ext-icon-note_edit',
								handler:function(){
									if ($('form').form('validate')) {
										myhope.progressBar({
											title : '修改中'
										});
										var url = myhope.contextPath + '/workcalendar!update.myhope';
										$.post(url, myhope.serializeObject($('form')), function(result) {
											myhope.progressBar('close');
											if (result.success) {
												event.title = result.obj.title;
												event.content = result.obj.content;
												event.project.id = result.obj.project.id;
												$('#calendar').fullCalendar('updateEvent',event);
												$('#eventForm').dialog('close');
											} else {
												$('#eventForm').dialog('close');
												$.messager.alert('警告','修改失败！');
											}
										}, 'json').error(function() {
															myhope.progressBar('close');
															$('#eventForm').dialog('close');
															$.messager.alert('警告','未知异常，请重试');
														});
									}
								}
							},{
								text:'删除',
								iconCls:'ext-icon-note_delete',
								handler:function(){
									myhope.progressBar({
										title : '删除中'
									});
									var url = myhope.contextPath + '/workcalendar!delete.myhope';
									$.post(url, myhope.serializeObject($('form')), function(result) {
										myhope.progressBar('close');
										if (result.success) {
											$('#calendar').fullCalendar('removeEvents',event.id);
											$('#eventForm').dialog('close');
										} else {
											$('#eventForm').dialog('close');
											$.messager.alert('警告','删除失败！');
										}
									}, 'json').error(function() {
														myhope.progressBar('close');
														$('#eventForm').dialog('close');
														$.messager.alert('警告','未知异常，请重试');
													});
								}
							}]
						});
						
						$('form').form('load', {
							'id' : event.id,
							'data.id' : event.id,
							'data.start' : $.fullCalendar.formatDate(event.start, "YYYY-MM-DD HH:mm:ss"),
							'data.end' : $.fullCalendar.formatDate(event.end, "YYYY-MM-DD HH:mm:ss"),
							'data.project.id' : event.project.id,
							'data.type' : event.type,
							'data.title' : event.title,
							'data.content' : event.content
						});
						$('#eventForm').dialog('open');
					},
					//时间选取
					select : function(start, end) {
						if((($.fullCalendar.formatDate(end, "DD")-0)-($.fullCalendar.formatDate(start, "DD")-0)) > 0){
							$('#calendar').fullCalendar('unselect');
							$.messager.alert('警告','请勿跨日选取！');
							return;
						}
						$('#eventForm').dialog({
							iconCls:'ext-icon-note_add',
							title: '添加记录',
							onClose: function(){
								$('#calendar').fullCalendar('unselect');
							},
							modal : true,
							buttons:[{
								text:'添加',
								iconCls:'ext-icon-note_add',
								handler:function(){
									if ($('form').form('validate')) {
										myhope.progressBar({
											title : '添加中'
										});
										var url = myhope.contextPath + '/workcalendar!addEvent.myhope';
										$.post(url, myhope.serializeObject($('form')), function(result) {
											myhope.progressBar('close');
											if (result.success) {
												$('#calendar').fullCalendar('renderEvent',result.obj, true);
												$('#calendar').fullCalendar('unselect');
												$('#eventForm').dialog('close');
											} else {
												$('#calendar').fullCalendar('unselect');
												$('#eventForm').dialog('close');
												$.messager.alert('警告','添加失败！');
											}
										}, 'json').error(function() {
															myhope.progressBar('close');
															$('#eventForm').dialog('close');
															$.messager.alert('警告','未知异常，请重试');
														});
									}
								}
							}]
						});
						var projectId0 = $("select[comboname='data.project.id']").combobox('getData')[0];
						if(projectId0 != null){
							projectId0 = projectId0.id;
						}else{
							projectId0 = '';
						}
						$('form').form('load', {
							'id' : '',
							'data.id' : '',
							'data.start' : $.fullCalendar.formatDate(start, "YYYY-MM-DD HH:mm:ss"),
							'data.end' : $.fullCalendar.formatDate(end, "YYYY-MM-DD HH:mm:ss"),
							'data.project.id' : projectId0,
							'data.type' : 'A',
							'data.title' : '研发',
							'data.content' : ''
						});
						$('#eventForm').dialog('open');
					},
					editable : true,
					eventLimit : true, // allow "more" link when too many events
					events : []
				});
		//双日历协调
		$('.fc-prev-button,.fc-next-button,.fc-today-button').click(function() {
			var moment = $('#calendar').fullCalendar('getDate');
			$('#littleC').calendar('moveTo', new Date(moment));
		});

		//日历数据初始化
		var url = myhope.contextPath + '/workcalendar!getWorkcalendars.myhope';
		$.post(url, '', function(result) {
			var startArray = new Array();
			result.forEach(function(e){
				//添加记录
				if(e.type == 'A'){
					$('#calendar').fullCalendar('renderEvent',e, true);
					startArray.push((e.start.substr(0,4)) + (e.start.substr(5,2)) + (e.start.substr(8,2)));
				}
				//初始化计划
				else if(e.type == 'B'){
					//初始化本周计划
					if((getWeekStartDate(0)).Format("yyyy-MM-dd") == e.start.substr(0,10)){
						var external_event_str = "<div id='plan_"+e.id+"' class=\"external-event\">"+e.title+"<br/>"+e.content+"<br/><a href='#' onclick=\"deletePlanFun('"+e.id+"');\">删除</a></div>";
						$('#external-events1').append(external_event_str);
						var $addPlanPom = $('#plan_'+e.id);
						$addPlanPom.panel();
						var eventObject = {
							title: e.title,
							content: e.content,
							project:{id: e.project.id}
						};
						$addPlanPom.data('eventObject', eventObject);
						$addPlanPom.draggable({
							zIndex: 999,
							revert: true,
							revertDuration: 0
							});
					}
					//初始化下周计划
					else if((getWeekStartDate(7)).Format("yyyy-MM-dd") == e.start.substr(0,10)){
						var external_event_str = "<div id='plan_"+e.id+"' class=\"external-event\">"+e.title+"<br/>"+e.content+"<br/><a href='#' onclick=\"deletePlanFun('"+e.id+"');\">删除</a></div>";
						$('#external-events2').append(external_event_str);
						var $addPlanPom = $('#plan_'+e.id);
						$addPlanPom.panel();
						var eventObject = {
							title: e.title,
							content: e.content,
							project:{id: e.project.id}
						};
						$addPlanPom.data('eventObject', eventObject);
						$addPlanPom.draggable({
							zIndex: 999,
							revert: true,
							revertDuration: 0
							});
					}
				}
			})
			
			$('#littleC').calendar({
				current : new Date(),
				border : false,
				onSelect : function(date) {
					$(this).calendar('moveTo', date);
					$('#calendar').fullCalendar( 'gotoDate', date );
					$('#external-events1').html('');
					$('#external-events2').html('');
					result.forEach(function(e){
						if(e.type == 'B'){
							//初始化本周计划
							if((getWeekStartDate(0)).Format("yyyy-MM-dd") == e.start.substr(0,10)){
								var external_event_str = "<div id='plan_"+e.id+"' class=\"external-event\">"+e.title+"<br/>"+e.content+"<br/><a href='#' onclick=\"deletePlanFun('"+e.id+"');\">删除</a></div>";
								$('#external-events1').append(external_event_str);
								var $addPlanPom = $('#plan_'+e.id);
								$addPlanPom.panel();
								var eventObject = {
									title: e.title,
									content: e.content,
									project:{id: e.project.id}
								};
								$addPlanPom.data('eventObject', eventObject);
								$addPlanPom.draggable({
									zIndex: 999,
									revert: true,
									revertDuration: 0
									});
							}
							//初始化下周计划
							else if((getWeekStartDate(7)).Format("yyyy-MM-dd") == e.start.substr(0,10)){
								var external_event_str = "<div id='plan_"+e.id+"' class=\"external-event\">"+e.title+"<br/>"+e.content+"<br/><a href='#' onclick=\"deletePlanFun('"+e.id+"');\">删除</a></div>";
								$('#external-events2').append(external_event_str);
								var $addPlanPom = $('#plan_'+e.id);
								$addPlanPom.panel();
								var eventObject = {
									title: e.title,
									content: e.content,
									project:{id: e.project.id}
								};
								$addPlanPom.data('eventObject', eventObject);
								$addPlanPom.draggable({
									zIndex: 999,
									revert: true,
									revertDuration: 0
									});
							}
						}
					})
				},
				styler: function(date){
					var dateString = ("" + date.getFullYear()) + ("" + ((date.getMonth() + 1) < 10 ? '0' + 
							(date.getMonth() + 1): (date.getMonth() + 1))) + (date.getDate() < 10 ? '0' + date.getDate(): '' + date.getDate());
					if (startArray.indexOf(dateString) != -1){
						return 'background-color:#ccc';
					} else {
						return '';
					}
				}
			});
		}, 'json').error(function() {
							myhope.progressBar('close');
							$('#eventForm').dialog('close');
							$.messager.alert('警告','未知异常，请重试');
						});
});

//添加计划
function addPlanFun(flag){
	$('#eventForm').dialog({
		iconCls:'ext-icon-note_add',
		title: '添加计划',
		modal : true,
		buttons:[{
			text:'添加',
			iconCls:'ext-icon-note_add',
			handler:function(){
				if ($('form').form('validate')) {
					myhope.progressBar({
						title : '添加中'
					});
					var url = myhope.contextPath + '/workcalendar!addEvent.myhope';
					$.post(url, myhope.serializeObject($('form')), function(result) {
						myhope.progressBar('close');
						if (result.success) {
							$('#eventForm').dialog('close');
							//if(flag == 1){
								var external_event_str = "<div id='plan_"+result.obj.id+"' class=\"external-event\">"+result.obj.title+"<br/>"+result.obj.content+"<br/><a href='#' onclick=\"deletePlanFun('"+result.obj.id+"');\">删除</a></div>";
								$('#external-events' + flag).append(external_event_str);
								var $addPlanPom = $('#plan_'+result.obj.id);
								$addPlanPom.panel();
								var eventObject = {
									title: result.obj.title,
									content: result.obj.content,
									project:{id: result.obj.project.id}
								};
								$addPlanPom.data('eventObject', eventObject);
								$addPlanPom.draggable({
									zIndex: 999,
									revert: true,
									revertDuration: 0
									});
							//}
						} else {
							$('#eventForm').dialog('close');
							$.messager.alert('警告','添加失败！');
						}
					}, 'json').error(function() {
										myhope.progressBar('close');
										$('#eventForm').dialog('close');
										$.messager.alert('警告','未知异常，请重试');
									});
				}
			}
		}]
	});
	
	var planStartDate,planEndDate;
	//本周
	if(flag == 1){
		planStartDate = (getWeekStartDate(0)).Format("yyyy-MM-dd 00:00:00");
		planEndDate = (getWeekStartDate(4)).Format("yyyy-MM-dd 23:59:59");
	}
	//下周
	else if(flag == 2){
		planStartDate = (getWeekStartDate(7)).Format("yyyy-MM-dd 00:00:00");
		planEndDate = (getWeekStartDate(11)).Format("yyyy-MM-dd 23:59:59");
	}
	var projectId0 = $("select[comboname='data.project.id']").combobox('getData')[0];
	if(projectId0 != null){
		projectId0 = projectId0.id;
	}else{
		projectId0 = '';
	}
	$('form').form('load', {
		'id' : '',
		'data.id' : '',
		'data.start' : planStartDate,
		'data.end' : planEndDate,
		'data.project.id' : projectId0,
		'data.type' : 'B',
		'data.title' : '研发',
		'data.content' : ''
	});
	$('#eventForm').dialog('open');
}

//删除计划
function deletePlanFun(planId){
	myhope.progressBar({
		title : '删除中'
	});
	$('form').form('clear');
	$('form').form('load', {
		'id' : planId
	});
	var url = myhope.contextPath + '/workcalendar!delete.myhope';
	$.post(url, myhope.serializeObject($('form')), function(result) {
		myhope.progressBar('close');
		if (result.success) {
			$('#plan_' + planId).remove();
		} else {
			$.messager.alert('警告','删除失败！');
		}
	}, 'json').error(function() {
						myhope.progressBar('close');
						$.messager.alert('警告','未知异常，请重试');
					});
}

//导出周报
function exportExcelFun(){
	var beginDate = getWeekStartDate(0).Format("yyyy-MM-dd");
	var endDate = getWeekStartDate(4).Format("yyyy-MM-dd");
	var endDateNext = getWeekStartDate(11).Format("yyyy-MM-dd");
	$.messager.confirm('确认', '当前选择为【'+beginDate+'~'+endDate+'】的周报。确认导出？', function(r){
		if (r){
			window.location.href = myhope.contextPath + '/workcalendar!doNotNeedSecurity_exportExcel.myhope?id='+beginDate+'&ids='+beginDate+','+endDateNext;
		}
	});
}

//获得当前日期所在周的开始日期
function getWeekStartDate(day) {
	var moment = $('#calendar').fullCalendar('getDate');
	var theDate = new Date(moment);
	var firstDateOfWeek;  
	theDate.setDate(theDate.getDate() + 1 + day - theDate.getDay());  
	firstDateOfWeek = theDate;  
	return firstDateOfWeek;  
}
</script>
<style>
#calendar {
	max-width: 900px;
	margin: 0 auto;
}
</style>
</head>
<body class="easyui-layout">
	<div data-options="region:'west',title:'快捷操作'" style="width: 200px">
		<div class="easyui-panel" data-options="border:false">
			<div id="littleC" style="width: 196px"></div>
		</div>
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="exportExcelFun();">导出周报</a>
		<div class="easyui-panel" data-options="title:'本周计划',border:false">
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-note_add',plain:true" onclick="addPlanFun(1);">添加本周</a>
			<div id="external-events1" style="min-height: 50px">
			</div>
		</div>
		<div class="easyui-panel" data-options="title:'下周计划',border:false">
		<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-note_add',plain:true" onclick="addPlanFun(2);">添加下周</a>
			<div id="external-events2" style="min-height: 50px">
			</div>
		</div>
	</div>
	<div data-options="region:'center',split:true">
		<div id='calendar'></div>
	</div>
	<div id="eventForm" class="easyui-dialog" data-options="closed:true">
		<form method="post" class="form">
			<table class="table" style="width: 100%;">
				<input name="id" type="hidden"/>
				<input name="data.id" type="hidden"/>
				<input name="data.type" type="hidden"/>
				<tr>
					<th>时间</th>
					<td>
						<input name="data.start" class="easyui-textbox" data-options="readonly:true"/>
						<input name="data.end" class="easyui-textbox" data-options="readonly:true"/>
					</td>
				</tr>
				<tr>
					<th>项目</th>
					<td>
						<select name="data.project.id" class="easyui-combobox" data-options="required:true,editable:false,valueField:'id',textField:'name',url:'<%=contextPath%>/project!doNotNeedSecurity_getProjectByUserId.myhope',panelHeight:'auto'" style="width: 300px;"></select>
					</td>
				</tr>
				<tr>
					<th>标题</th>
					<td>
						<select class="easyui-combobox" name="data.title" style="width:300px;" data-options="required:true,editable:false">
							<option value="设计">设计</option>   
							<option value="研发">研发</option>
							<option value="测试">测试</option>
							<option value="修改Bug">修改Bug</option>
							<option value="其他">其他</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>内容</th>
					<td><textarea name="data.content" style="width:300px;height:200px"></textarea></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>