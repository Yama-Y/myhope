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
		var nodes = $('#tree').tree('getChecked', [ 'checked', 'indeterminate' ]);
		var ids = [];
		for (var i = 0; i < nodes.length; i++) {
			if (nodes[i].attributes.isuser == '1') {
				ids.push(nodes[i].id);
			}
		}
		$.post(myhope.contextPath + '/project!grantUsers.myhope', {
			id : $(':input[name="data.id"]').val(),
			ids : ids.join(',')
		}, function(result) {
			if (result.success) {
				$dialog.dialog('destroy');
			} else {
				$pjq.messager.alert('提示', result.msg, 'error');
			}
			$pjq.messager.alert('提示', '授权成功！', 'info');
		}, 'json');
	};
	$(function() {
		parent.$.messager.progress({
			text : '数据加载中....'
		});
		$('#tree').tree({
			url : myhope.contextPath + '/base/user!doNotNeedSecurity_getUsersTree.myhope',
			parentField : 'pid',
			checkbox : true,
			formatter : function(node) {
				return node.text;
			},
			onLoadSuccess : function(node, data) {
				$.post(myhope.contextPath + '/project!doNotNeedSecurity_getProjectUser.myhope', {
					id : $(':input[name="data.id"]').val()
				}, function(result) {
					if (result) {
						for (var i = 0; i < result.length; i++) {
							var node = $('#tree').tree('find', result[i].id);
							if (node.attributes.isuser == '1') {
								var isLeaf = $('#tree').tree('isLeaf', node.target);
								if (isLeaf) {
									$('#tree').tree('check', node.target);
								}
							}
						}
					}
					parent.$.messager.progress('close');
				}, 'json');
			}
		});
	});
</script>
</head>
<body>
	<input name="data.id" value="<%=id%>" readonly="readonly" type="hidden" />
	<fieldset>
		<legend>项目授权</legend>
		<ul id="tree"></ul>
	</fieldset>
</body>
</html>