<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
   .newDiv4a:first-child{border-top:none;}
   .newDiv4a:last-child{border-bottom:none;}
   .newDiv4a{
      width:100%;
	  color: #afafaf;
	  margin-top: 5px;
	  margin-right: 10px;
	  margin-bottom: 0px;
   }
   .newDiv4a .user{
		float:left;
		display:inline;
		height: 45px;
		width: 45px;
		margin-top: 0px;
		margin-right: 5px;
		margin-bottom: 0px;
		margin-left: 10px;
		font-size: 12px;
		line-height: 20px;
		text-align: center;
   }
   .newDiv4a .talk_recordtextbg{
		float:left;
		width:10px;
		height:30px;
		display:block;
		background-image: url(securityJsp/portal/robot-img/talk_recordtext.png);
		background-repeat: no-repeat;
		background-position: left top;
	}
	.newDiv4a .talk_recordtext{
		-moz-border-radius:5px;
		-webkit-border-radius:5px;
		border-radius:5px;
		background-color:#b8d45c;
		width:300px;
		height:auto;
		display:block;
		padding: 5px;
		float:left;
		color:#333333;
    }
	.newDiv4a h3{
		font-size:14px;
		padding:2px 0 5px 0;
		font-weight: 100;
		word-wrap:break-word;
    }
	.newDiv4b{
	    display:block;
		min-height:80px;
		color: #afafaf;	
		padding-top: 5px;
		padding-right: 10px;
		padding-left: 10px;
		padding-bottom: 0px;
    }
	.newDiv4b .talk_recordtextbg{
		float:right;
		width:10px;
		height:30px;
		display:block;
		background-image: url(securityJsp/portal/robot-img/talk_recordtextme.png);
		background-repeat: no-repeat;
		background-position: left top;
	}
    .newDiv4b .talk_recordtext{
		-moz-border-radius:5px;
		-webkit-border-radius:5px;
		border-radius:5px;
		background-color:#fcfcfc;
		width:300px;
		height:auto;
		padding: 5px;
		color:#666;
		font-size:12px;
		float:right;	
    }
    .newDiv4b h3{
		font-size:14px;
		padding:2px 0 5px 0;
		font-weight: 100;
		color:#333333;
		word-wrap:break-word;	
    }
    .newDiv4b .user{
		float:right;
		height: 45px;
		width: 45px;
		margin-top: 0px;
		margin-right: 0px;
		margin-bottom: 0px;
		margin-left: 5px;
		font-size: 12px;
		line-height: 20px;
		text-align: center;
		display:inline;
    }
	.talk_time{
	    font-size:12px;
		color: #666;
		text-align: right;
		width: 240px;
		display: block;
    }
</style>
<script type="text/javascript" charset="utf-8">
	var myText = '';

	var requestFun_robot = function(){
		if($('#robot_info').val().trim() == ''){
			return;
		}
		write_info_Fun_robot($('#robot_info').val());
		$('#p2').panel('setTitle','对方正在输入.....');
		$.post(myhope.contextPath + '/init!doNotNeedSessionAndSecurity_robot.myhope',{id:myText}, function(result) {
			if(result.code != null){
				var resultText = '';
				if(result.code == '100000'){
					resultText = result.text;
				}else if(result.code == '200000'){
					resultText = result.text+'<br><a href="'+result.url+'" target="_blank">我是链接，点我。</a>';
				}else if(result.code == '302000'){
					resultText = result.text + '<br><table class="table" style="width: 100%; height: 100%">';
					$.each(result.list, function(index, item) {
						resultText += '<tr><td style="text-align:center"><a href="'+this.detailurl+'" target="_blank">'+this.article+'</a></td></tr>';
					});
					resultText += '</table>'
				}else if(result.code == '308000'){
					resultText = result.text + '<br><table class="table" style="width: 100%; height: 100%">';
					$.each(result.list, function(index, item) {
						resultText += '<tr><td style="text-align:center"><a href="'+this.detailurl+'" target="_blank">'+this.name+'</a></td><td style="text-align:center">'+this.info+'</td></tr>';
					});
					resultText += '</table>'
				}else{
					resultText = result.text;
				}
				write_text_Fun_robot(resultText);
			}else{
				write_text_Fun_robot('抱歉~我出错啦 >_<#');
			}
			$('#p2').panel('setTitle','MyHope机器人');
		}, 'json');
	};
	
	var myImgPath = 'securityJsp/portal/robot-img/1.jpg';
	
	var write_info_Fun_robot = function(text){
		myText = text;
		var newDiv4b = '<div class="newDiv4b">'
            +'<div class="user"><img src="'+myImgPath+'" style="height: 45px;width: 45px;"/>我</div><div class="talk_recordtextbg">&nbsp;</div>'
			+'<div class="talk_recordtext"><h3>'+text+'</h3><span class="talk_time">'+new Date().Format("yyyy-MM-dd hh:mm:ss")+'</span></div>'
			+'<div style="clear:both"></div>'
			+'</div>';
		$('#robot_text').append(newDiv4b);
		$('#robot_text_panel').scrollTop($('#robot_text_panel')[0].scrollHeight);
		setTimeout(function(){$('#robot_info').val('');},10);
	};
	var write_text_Fun_robot = function(text){
		var newDiv4a = '<div class="newDiv4a">'
            +'<div class="user"><img src="securityJsp/portal/robot-img/2.jpg"/>MyHope</div><div class="talk_recordtextbg">&nbsp;</div>'
			+'<div class="talk_recordtext"><h3>'+text+'</h3><span class="talk_time">'+new Date().Format("yyyy-MM-dd hh:mm:ss")+'</span></div>'
			+'<div style="clear:both"></div>'
			+'</div>';
		$('#robot_text').append(newDiv4a);
		$('#robot_text_panel').scrollTop($('#robot_text_panel')[0].scrollHeight);
	};
	
	$(function(){
		if('${sessionScope.sessionInfo.user.photo}' != ''){
			myImgPath = '${sessionScope.sessionInfo.user.photo}';
		}
		$("#robot_info").keydown(function(event){
			event=document.all?window.event:event;
			if((event.keyCode || event.which)==13){
				requestFun_robot();
			}
		});
		write_text_Fun_robot('您好，我是MyHope机器人，很高兴为您服务。');
	});
</script>
<div id="robot_text_panel" class="easyui-panel" data-options="border:true,height : 310">
	<div id="robot_text" style="width:100%;height:90%;">
	</div>
</div>
<div>
<textarea id="robot_info" name="id" style="width:99%;height:100%"></textarea>
<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="requestFun_robot();">发送</a>
<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-cross',plain:true" onclick="$('#robot_text').html('');">清屏</a>
</div>