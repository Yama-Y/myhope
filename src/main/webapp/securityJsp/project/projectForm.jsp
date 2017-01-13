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
				url = myhope.contextPath + '/project!update.myhope';
			} else {
				url = myhope.contextPath + '/project!save.myhope';
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
		$('form').form('load', {
			'data.state' : 'A',
		});
		if ($(':input[name="data.id"]').val().length > 0) {
			parent.$.messager.progress({
				text : '数据加载中....'
			});
			$.post(myhope.contextPath + '/project!getById.myhope', {
				id : $(':input[name="data.id"]').val()
			}, function(result) {
				if (result.id != undefined) {
					$('form').form('load', {
						'data.id' : result.id,
						'data.name' : result.name,
						'data.state' : result.state,
						'data.content' : result.content
					});
				}
				parent.$.messager.progress('close');
			}, 'json');
		}
	});
</script>
</head>
<body>
	<form method="post" class="form">
		<fieldset>
			<legend>项目基本信息</legend>
			<table class="table" style="width: 100%;">
				<tr>
					<th>编号</th>
					<td><input name="data.id" value="<%=id%>" readonly="readonly" /></td>
					<th>项目状态</th>
					<td>
						<select name="data.state" class="easyui-combobox" data-options="required:true,panelHeight:'auto',editable:false">
							<option value="A">开始</option>
							<option value="B">暂停</option>
							<option value="C">结束</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>项目名称</th>
					<td><input name="data.name" class="easyui-validatebox" data-options="required:true" /></td>
					<th>项目描述</th>
					<td><textarea name="data.content"></textarea></td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>