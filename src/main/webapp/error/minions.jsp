<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<%
		double d = Math.random() * 22 + 1;
		int i = (int) d;
		String n = String.valueOf(i);
	%>
	<img alt="" src="${pageContext.request.contextPath}/style/images/minions/<%=n%>.gif">
</body>
</html>
