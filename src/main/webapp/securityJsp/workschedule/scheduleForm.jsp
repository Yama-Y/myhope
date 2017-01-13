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
<script type="text/javascript" src="../../jslib/minicolors/jquery.minicolors.js"></script>
<link rel="stylesheet" href="../../jslib/minicolors/jquery.minicolors.css" type="text/css"></link>
<script type="text/javascript">
	var submitForm = function($dialog, $grid, $pjq) {
		if ($('form').form('validate')) {
			var url;
			if ($(':input[name="data.id"]').val().length > 0) {
				url = myhope.contextPath + '/schedule!update.myhope';
			} else {
				url = myhope.contextPath + '/schedule!save.myhope';
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
			$.post(myhope.contextPath + '/schedule!getById.myhope', {
				id : $(':input[name="data.id"]').val()
			}, function(result) {
				if (result.id != undefined) {
					$('form').form('load', {
						'data.id' : result.id,
						'data.color' : result.color,
						'data.name' : result.name,
						'data.type' : result.type,
						'data.remark' : result.remark
					});
					colorDom($('#dataColor'));
				}
				parent.$.messager.progress('close');
			}, 'json');
		}else{
			colorDom($('#dataColor'));
		}
	});
	
	var colorDom = function($dom) {
		$dom.minicolors({
			control: 'wheel',
			defaultValue: '',
			letterCase: 'lowercase',
			position: 'bottom left',
			change: function(hex, opacity) {
				if( !hex ) return;
				if( opacity ) hex += ', ' + opacity;
				try {
					console.log(hex);
				} catch(e) {}
			}
		});
	};
</script>
</head>
<body>
	<form method="post" class="form">
		<fieldset>
			<legend>班次基本信息</legend>
			<table class="table" style="width: 100%;">
				<tr>
					<th>编号</th>
					<td><input name="data.id" value="<%=id%>" readonly="readonly" /></td>
					<th>班次类型</th>
					<td>
						<select name="data.type" class="easyui-combobox" data-options="required:true,panelHeight:'auto',editable:false">
							<option value="A">工作班次</option>
							<option value="B">休假班次</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>班次颜色</th>
					<td><input id="dataColor" name="data.color" class="easyui-validatebox" data-options="required:true" /></td>
					<th>班次名称</th>
					<td><input name="data.name" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<th>班次备注</th>
					<td colspan="3"><textarea name="data.remark" name="data.content" style="width:300px;height:200px"></textarea></td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>