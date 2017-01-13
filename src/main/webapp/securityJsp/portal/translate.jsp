<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
	var requestFun_translate = function(){
		var translate_value = $('#translate_info').val().trim();
		if(translate_value == ''){
			return;
		}
		$('#translate_text').html('<h2>MyHope正在拼命为您翻译，请稍等......</h2>');
		$.post(myhope.contextPath + '/init!doNotNeedSessionAndSecurity_translate.myhope',{id:translate_value}, function(result) {
			if(result.errNum != null && result.errNum == '0'){
				var resultText = '<h2>'+translate_value + '<hr>';
				$.each(result.retData.trans_result, function(index, item) {
					resultText += this.dst + ' ';
				});
				resultText += '</h2>';
				$('#translate_text').html(resultText);
			}else{
				$('#translate_text').html('抱歉~我出错啦 >_<#');
			}
		}, 'json');
	};
	
	$(function(){
		$("#translate_info").keydown(function(event){
			event=document.all?window.event:event;
			if((event.keyCode || event.which)==13){
				requestFun_translate();
			}
		});
	});
</script>
<div>
<textarea id="translate_info" name="id" style="width:99%;height:70px;font-size: 20px"></textarea>
<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'ext-icon-table_go',plain:true" onclick="requestFun_translate();">翻译</a>
<div id="translate_text" style="width: 100%; height: 100px;word-wrap:break-word;">
</div>
</div>