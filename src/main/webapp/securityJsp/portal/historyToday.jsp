<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" charset="utf-8">
	$(function() {
		$.post(myhope.contextPath + '/init!doNotNeedSessionAndSecurity_historyToday.myhope', function(result) {
			var divString = '';
			if(result.reason == 'Succes'){
				divString += '<table class="table" style="width: 100%; height: 100%">';
				divString += '<thead><tr><th style="width: 25px">序号</th><th style="width: 100px">时间</th><th>事件</th></tr></thead>';
				var data = result.result;
				divString += '<tbody>';
				$.each(data, function(index, item) {
					divString += '<tr><td style="text-align:center">'+(index+1)+'</td><td style="text-align:center">'+this.year+'年'+this.month+'月'+this.day+'日'+'</td><td>'+this.title+'</td></tr>';
				});
				divString += '</tbody>';
				divString += '</table>';
			}
			$('#div_history_today').html(divString);
		}, 'json');
	});
</script>
<div id="div_history_today">正在载入数据...</div>