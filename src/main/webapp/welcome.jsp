<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<title>欢迎页面</title>
<jsp:include page="inc.jsp"></jsp:include>
<script type="text/javascript" charset="utf-8">
	var portal;
	var panels;
	$(function() {
		
		panels = [{
			id : 'p1',
			title : '天气',
			height : 130,
			collapsible : true,
			content : '<iframe allowtransparency="true" frameborder="0" width="575" height="96" scrolling="no" src="http://tianqi.2345.com/plugin/widget/index.htm?s=1&z=1&t=0&v=0&d=5&bd=0&k=&f=&q=1&e=1&a=1&c=54511&w=575&h=96&align=center"></iframe>'
		}, {
			id : 'p2',
			title : 'MyHope机器人',
			height : 410,
			collapsible : true,
			href:'${pageContext.request.contextPath}/securityJsp/portal/robot.jsp'
		}, {
			id : 'p3',
			title : '历史上的今天',
			height : 240,
			collapsible : true,
			href:'${pageContext.request.contextPath}/securityJsp/portal/historyToday.jsp'
		}, {
			id : 'p4',
			title : '翻译',
			height : 300,
			collapsible : true,
			href:'${pageContext.request.contextPath}/securityJsp/portal/translate.jsp'
		}];

		portal = $('#portal').portal({
			border : false,
			fit : true,
			onStateChange : function() {
				$.cookie('portal-state', getPortalState(), {
					expires : 7
				});
			}
		});
		var state = $.cookie('portal-state');
		if (!state) {
			state = 'p1,p2:p3,p4';/*冒号代表列，逗号代表行*/
		}
		addPanels(state);
		portal.portal('resize');

	});

	function getPanelOptions(id) {
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i].id == id) {
				return panels[i];
			}
		}
		return undefined;
	}
	function getPortalState() {
		var aa=[];
		for(var columnIndex=0;columnIndex<2;columnIndex++) {
			var cc=[];
			var panels=portal.portal('getPanels',columnIndex);
			for(var i=0;i<panels.length;i++) {
				cc.push(panels[i].attr('id'));
			}
			aa.push(cc.join(','));
		}
		return aa.join(':');
	}
	function addPanels(portalState) {
		var columns = portalState.split(':');
		for (var columnIndex = 0; columnIndex < columns.length; columnIndex++) {
			var cc = columns[columnIndex].split(',');
			for (var j = 0; j < cc.length; j++) {
				var options = getPanelOptions(cc[j]);
				if (options) {
					var p = $('<div/>').attr('id', options.id).appendTo('body');
					p.panel(options);
					portal.portal('add', {
						panel : p,
						columnIndex : columnIndex
					});
				}
			}
		}
	}
</script>
</head>
<body class="easyui-layout">
	<div id="portal" style="position:relative">
		<div></div>
		<div></div>
	</div>
</body>
</html>