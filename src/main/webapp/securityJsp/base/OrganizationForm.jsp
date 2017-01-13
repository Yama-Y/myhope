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
				url = myhope.contextPath + '/base/organization!update.myhope';
			} else {
				url = myhope.contextPath + '/base/organization!save.myhope';
			}
			$.post(url, myhope.serializeObject($('form')), function(result) {
				if (result.success) {
					$grid.treegrid('reload');
					$dialog.dialog('destroy');
				} else {
					$pjq.messager.alert('提示', result.msg, 'error');
				}
			}, 'json');
		}
	};
	var showIcons = function() {
		var dialog = parent.myhope.modalDialog({
			title : '浏览小图标',
			url : myhope.contextPath + '/style/icons.jsp',
			buttons : [ {
				text : '确定',
				handler : function() {
					dialog.find('iframe').get(0).contentWindow.selectIcon(dialog, $('#iconCls'));
				}
			} ]
		});
	};
	$(function() {
		if ($(':input[name="data.id"]').val().length > 0) {
			parent.$.messager.progress({
				text : '数据加载中....'
			});
			$.post(myhope.contextPath + '/base/organization!getById.myhope', {
				id : $(':input[name="data.id"]').val()
			}, function(result) {
				if (result.id != undefined) {
					$('form').form('load', {
						'data.id' : result.id,
						'data.name' : result.name,
						'data.address' : result.address,
						'data.organization.id' : result.organization ? result.organization.id : '',
						'data.iconCls' : result.iconCls,
						'data.seq' : result.seq,
						'data.code' : result.code,
						'data.defaultSchedule' : result.defaultSchedule
					});
					$('#iconCls').attr('class', result.iconCls);//设置背景图标
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
			<legend>机构基本信息</legend>
			<table class="table" style="width: 100%;">
				<tr>
					<th>编号</th>
					<td><input name="data.id" value="<%=id%>" readonly="readonly" /></td>
					<th>机构名称</th>
					<td><input name="data.name" class="easyui-validatebox" data-options="required:true" /></td>
				</tr>
				<tr>
					<th>机构编码</th>
					<td><input name="data.code" /></td>
					<th>顺序</th>
					<td><input name="data.seq" class="easyui-numberspinner" data-options="required:true,min:0,max:100000,editable:false" style="width: 155px;" value="100" /></td>
				</tr>
				<tr>
					<th>上级机构</th>
					<td><select id="organization_id" name="data.organization.id" class="easyui-combotree" data-options="editable:false,idField:'id',textField:'name',parentField:'pid',url:'<%=contextPath%>/base/organization!doNotNeedSecurity_comboTree.myhope'" style="width: 155px;"></select><img class="iconImg ext-icon-cross" onclick="$('#organization_id').combotree('clear');" title="清空" /></td>
					<th>机构图标</th>
					<td><input id="iconCls" name="data.iconCls" readonly="readonly" style="padding-left: 18px; width: 134px;" /><img class="iconImg ext-icon-zoom" onclick="showIcons();" title="浏览图标" />&nbsp;<img class="iconImg ext-icon-cross" onclick="$('#iconCls').val('');$('#iconCls').attr('class','');" title="清空" /></td>
				</tr>
				<tr>
					<th>机构地址</th>
					<td><input name="data.address" /></td>
					<th>默认班次</th>
					<td><input id="defaultSchedule" class="easyui-combobox" name="data.defaultSchedule" data-options="valueField:'id',textField:'name',url:'<%=contextPath%>/schedule!doNotNeedSecurity_findSchedule().myhope?id=<%=id%>'" /><img class="iconImg ext-icon-cross" onclick="$('#defaultSchedule').combobox('clear');" title="清空" /></td>
				</tr>
			</table>
		</fieldset>
	</form>
</body>
</html>