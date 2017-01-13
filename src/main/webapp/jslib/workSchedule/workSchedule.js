var AllDataArray = new Array();  // 后台传过来的数据
var staffDataArray = new Array();   //员工
var workScheduleDataArray = new Array();  // 排班
var scheduleDataArray = new Array();  // 班次

var weekArray = new Array();  // 周数据集
var startDate = new Date();
var thisTdId = ""; // 当前鼠标移入的td
var isEdit = false;

$(function() {
	/*AllDataArray = [
		// 员工数据
		[ 
			{
				"id": "user1",
				"name": "username1"
			}, 
			{
				"id": "user2",
				"name": "username2"
			}
		],
		// 排班数据
		[
			{
				"id": "1", 
				"date": "2016-01-13 00:00:00", 
				"user": {
					"id": "user1"
				}, 
				"schedule": "schedule1"
			},
            {
				"id": "2", 
				"date": "2016-01-13 00:00:00", 
				"user": {
					"id": "user1"
				}, 
				"schedule": "schedule2"
			},			
			{
				"id": "3", 
				"date": "2016-01-14 00:00:00", 
				"user": {
					"id": "user2"
				}, 
				"schedule": "schedule2"
			}
		],
		// 班次数据
		[
			{
				"id": "schedule1", 
				"color": "#00DDDD", 
				"name": "schedulename1", 
				"workTime" : "8",
				"type": "A"
			}, 
			{
				"id": "schedule2", 
				"color": "#FF0000", 
				"name": "schedulename2", 
				"workTime" : "6",
				"type": "A"
			}
		]
	];*/
	//init(JSON.stringify(AllDataArray),getWeekStartDate(new Date(), 0));
	startDate = getWeekStartDate(new Date(), 0);   // 本周周一
	var endDate = getWeekStartDate(new Date(), 6);   // 本周周日
	ajaxGetSchedule(startDate,endDate);   // ajax请求排班数据
});

/**
 * 根据后台封装的数据生成排班table
 */
function init(dataJson,sDate){
	// 接收json数据,赋当前周起始日期
	AllDataArray = JSON.parse(dataJson);
	startDate = sDate;
	
	// 拆分获得员工、排班、班次数据		
	staffDataArray = AllDataArray[0];
	workScheduleDataArray = AllDataArray[1];
	scheduleDataArray = AllDataArray[2];
	
	// 生成班次弹窗
	createScheduleDiv();
		
    // 生成横轴周数据
	createWeekTr();
	
	// 生成纵轴员工数据及初始排班
	createScheduleTable();
	
	// 生成排班
	workScheduleData();
	
	// 计算累计时间
	initSumShedule();
	editSchedule(isEdit);
}

/**
 *  生成班次弹窗
 */
function createScheduleDiv(){
	if($(".editDiv").length>0){
		return;  // 若页面上存在则不生成
	}
	// 创建编辑框div
	var editDivHtml = '<div class="editDiv" id="editDiv">';
	for(var i=0; i<scheduleDataArray.length; i++){
		var sche = scheduleDataArray[i];
		editDivHtml = editDivHtml + '<div class="selectScheduleDiv" id="sche_' + sche.id + '" style="background-color:' + sche.color
								  + '" onmouseover="$(this).css(\'background-color\',\'#969696\')" onmouseout="$(this).css(\'background-color\',\''+sche.color+'\')"'
							      + ' onclick="selectSchedule(\''+sche.id+'\');">'
								    + sche.name
								    + '<div class="sche_name">'+sche.name+'</div><div class="sche_color">'+sche.color+'</div><div class="sche_type">'+sche.type+'</div><div class="sche_workTime">'+sche.workTime+'</div>'
								  + '</div>';
	}
	editDivHtml = editDivHtml + '</div>';
	$(document.body).append(editDivHtml);    
}

/**
 *  根据本周周一日期生成本周日期数据
 */
function createWeekTr(){
	// 修改排班时间段信息
	var calendar_head = getWeekStartDate(new Date(startDate.getTime()),0).Format("yyyy 年 MM 月 dd 日") + " - " + getWeekStartDate(new Date(startDate.getTime()),6).Format("yyyy 年 MM 月 dd 日");
	document.getElementById("calendar_head").innerHTML = '本周 ('+ calendar_head +')排班表';
	var weeks = ["星期一","星期二","星期三","星期四","星期五","星期六","星期日"];
	// 横轴日期
	for(var i=0;i<7;i++){
		var theDate = getWeekStartDate(new Date(startDate.getTime()),i).Format("yyyy-MM-dd");
		var textDate = weeks[i] + "(" + theDate + ")";
		document.getElementById("weekTh_"+i).innerHTML = textDate;
		
		weekArray[i] = theDate;  //一周日期数组
	}
}

/**
 * 生成纵轴员工数据及初始排班
 */
function createScheduleTable(){
	// 删除旧数据
	$('#schedule_table tr[class=schedule_tr]').remove(); 
	// 遍历员工数据集合
	for(var i=0; i<staffDataArray.length; i++){
		var staff = staffDataArray[i];
		var newTr = '<tr class="schedule_tr" id="tr_'+staff.id+'">'
				  + '<td class="wtClass">' + staff.name + '</td>';
		// 遍历日期集合，生成初始排班
		for(var j=0; j<weekArray.length; j++){
			  var div_id = staff.id + "_" + weekArray[j];
			  newTr = newTr + '<td id="td_'+div_id+'" class="scheduleTd"><div id='+ div_id +' class="scheduleDiv"></div></td>';
		}
		newTr = newTr + '<td id="total_'+ staff.id +'"></td></tr>';
		// 在最下边添加一行
		$("#schedule_table tr:last").after(newTr);
	}
}

/**
 * 生成排班
 */
function workScheduleData(){
	for(var i=0; i<workScheduleDataArray.length; i++){
		var id = workScheduleDataArray[i].id;   // 排班id
		var date = workScheduleDataArray[i].date;
		var Schedule_identify = workScheduleDataArray[i].user.id + "_" + date.substr(0,10); 
		var schedule_id = workScheduleDataArray[i].schedule;  // 班次id
		$(".editDiv").attr("id","edit"+Schedule_identify);
		// 生成排班
		addSchedule(Schedule_identify,id,schedule_id);
	}
	$(".deleteSchedule").css("display","none");   // 隐藏删除a标签
}

/**
 * 初始化生成初始的累计时间
 */
function initSumShedule(){
	for(var i=0; i<staffDataArray.length; i++){
		// 计算每一行累计时间
		sumShedule("tr_"+staffDataArray[i].id);
	}
}

/**
 *  新增排班数据到页面中
 *  param: 排班id、班次id
 */
function addSchedule(workscheduleDivId,workSche_id,schedule_id){  
	var _scheduleId = workSche_id + "_" + schedule_id;
	// 获取班次属性
	var scheduleColor;
	var scheduleName;
	var scheduleType;
	var scheduleWorkTime;
	var sche = $("#sche_"+schedule_id);
	scheduleColor = sche.children("div[class='sche_color']").html();
	scheduleName = sche.children("div[class='sche_name']").html();
	scheduleType = sche.children("div[class='sche_type']").html();
	scheduleWorkTime = sche.children("div[class='sche_workTime']").html();
	// 排班
	var _scheduleDiv = '<div class="workScheDiv" style="background-color: '+ scheduleColor +'" id="'+ _scheduleId +'">' 
						+ scheduleName
	                    + '<div class="sche_type">'+scheduleType+'</div><div class="sche_workTime">'+scheduleWorkTime+'</div>'
			            + '<a class="deleteSchedule" href="#" onclick="deleteSchedule(\''+ workSche_id +'\');" >删除</a>'
					 + '</div>';
	// 将排班加到对应位置的div中的最下面
	$('#' + workscheduleDivId).append(_scheduleDiv);
	// 计算这一行累计时间
	var userId = workscheduleDivId.split("_")[0];
	sumShedule("tr_"+userId);
}

/**
 * 点击上一周
 */
function preWeekSchedule(){
   startDate = getWeekStartDate(new Date(startDate.getTime()), -7);   // 上周周一
   var endDate = getWeekStartDate(new Date(startDate.getTime()),6);   // 上周周日
   ajaxGetSchedule(startDate,endDate);   // ajax请求排班数据
   $('#littleC').calendar('moveTo', startDate);
}

/**
 * 点击本周
 */
function nowWeekSchedule(){
   startDate = getWeekStartDate(new Date(), 0);   // 本周周一
   var endDate = getWeekStartDate(new Date(), 6);   // 本周周日
   ajaxGetSchedule(startDate,endDate);   // ajax请求排班数据
   $('#littleC').calendar('moveTo', startDate);
}

/**
 * 点击下一周
 */
function nextWeekSchedule(){
   startDate = getWeekStartDate(new Date(startDate.getTime()), 7);   // 下周周一
   var endDate = getWeekStartDate(new Date(startDate.getTime()),13);   // 下周周日
   ajaxGetSchedule(startDate,endDate);   // ajax请求排班数据
   $('#littleC').calendar('moveTo', startDate);
}

/**
 *  控制编辑排班（开始排班/结束排班）
 */
function editSchedule(checked){
   isEdit = checked;
   // 若当前可编辑状态为false，则启动编辑，并修改当前可编辑状态为true
   if(isEdit){
		$(".deleteSchedule").css("display","block");   // 删除排班可用
		// 排班框绑定鼠标事件
		$(".scheduleTd").bind("mouseover", workSchedule);
		
		// class='wtClass'的组件绑定点击事件：隐藏编辑框div
		$(".wtClass").click(function(){ $(".editDiv").css("display","none");});
   }else{  // 若当前可编辑状态为true，则结束编辑，并修改当前可编辑状态为false
		$(".deleteSchedule").css("display","none");   // 删除排班不可用
		// 排班框解绑鼠标事件
		$(".scheduleTd").unbind("mouseover"); 
		
		// 将所有排班框恢复默认，隐藏编辑框
		$(".scheduleTd").css("background-color","#FFFFFF");
		$(".editDiv").css("display","none");		
   }
}

/**
 *  编辑排班（鼠标移入排班Td事件）
 */
function workSchedule(){
	// 将上一个排班框恢复默认,记录当前移入td
	$("#"+thisTdId).css("background-color","#FFFFFF");
	thisTdId = $(this).attr("id");
	// 鼠标移入的td改变背景颜色
	$(this).css("background-color","#FFFF00"); 
	// 获取当前td的子div
	var _div = $(this).children("div");
	var _divId = $(this).children("div").attr("id");   // 外层排班div
	var X = _div.offset().top;
	var Y = _div.offset().left;
	var H = _div.height();
	
	$(".editDiv").attr("id", "edit"+_divId);
	$(".editDiv").css("display","block");
	$(".editDiv").offset({ top: X+H, left: Y });
}

/**
 *  点击增加排班事件
 */
function selectSchedule(scheduleId){
	// 获取排班框id。（员工id、日期）
	var editDivId = $(".editDiv").attr("id");
	var workscheduleDivId = editDivId.substr(4,editDivId.length);
	//var arrays = workscheduleDivId.split("_");
	//var userId = arrays[0];
	//var date = arrays[1];
	// 校验后增加排班数据
	if(checkWorkSchedule(scheduleId)){
	   // 增加排班（ajax添加到数据库中、排班信息加到页面中）
	   ajaxScheduleAdd(workscheduleDivId,scheduleId);
	}
}

/**
 *  校验新增排班（当前排班框中不能含有重复的班次；班次不能超过三个；）
 */
function  checkWorkSchedule(scheduleId){
	// 获取当前排班Div的id。（员工id、日期）
	var editDivId = $(".editDiv").attr("id");
	var workscheduleDivId = editDivId.substr(4,editDivId.length);
	var chileDiv = $(document.getElementById(workscheduleDivId)).children("div");   //获取已存在班次div
	var flag = true;
	if(chileDiv.length >= 3){
		$.messager.alert("警告","一天内排班不能超过三个！");
		flag = false;
	}		
	chileDiv.each(function(i,n){
		var sches = $(this).attr("id");
		var scheId = sches.split("_")[1];  //取班次id
		if(scheId == scheduleId){
		   var scheduleName = $("#sche_"+scheduleId).children("div[class='sche_name']").html();
		   $.messager.alert("警告","您选择的"+scheduleName+"已经存在！");
		   flag = false;
		}
	});
	return flag;
}

/**
 *  点击删除排班事件
 */
function deleteSchedule(workSche_id){
	// 删除排班（ajax删除排班数据、排班信息从页面删除）
	ajaxScheduleDelete(workSche_id);
	
}

/**
 *  根据排班id从页面中删除排班数据
 */
function delSchedule(workSche_id){  
	var workScheDiv = $(".workScheDiv");
	var $tr_workSche;
	workScheDiv.each(function(i,n){
		var sches = $(this).attr("id");
		var scheId = sches.split("_")[0];
		if(scheId == workSche_id){
			$tr_workSche = $(this).parents().parents().parents();
		   // 找到要删除的div后删掉
		   $(this).remove(); 
		}
	});
	sumShedule($tr_workSche.attr("id"));
}

/**
 * 计算每一行累计时间(小时)
 * param: 行id：tr_id（'tr_' + userid）
 */
function sumShedule(tr_id) {
	var _worksche = $("#"+tr_id).find("div[class='workScheDiv']");
	var t = 0;
	_worksche.each(function(i,n){
		var _type = $(this).children("div[class='sche_type']").html();
		var _workTime = $(this).children("div[class='sche_workTime']").html();
		if(_type == 'A'){  // 若班次类型是A，则计算该班次的时间
			t = t + parseInt(_workTime);
		}
	});
	// 填写
	var userid = tr_id.substr(3,tr_id.length);
	$("#total_"+userid).html(t);
}

/**
 * 获得当前日期所在周的开始日期
 */
function getWeekStartDate(date,day) {
	var _date = date;
	var firstDateOfWeek;  
	_date.setDate(_date.getDate() + 1 + day - _date.getDay());  
	firstDateOfWeek = _date;  
	return firstDateOfWeek;  
}