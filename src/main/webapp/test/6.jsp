<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>测试</title>
<jsp:include page="../inc.jsp"></jsp:include>
<script type="text/javascript">
	i=0;
	function asd(asd) {
        $('#aaa').html(asd);
		i++;
    }
</script>
<style>
.content{font-size:20px}
#dvCompute{position:absolute;visibility:hidden;}
</style>
</head>
<body>
<input type='button' value='asd' onclick='asd(1)'/>
<div id='aaa'></div>
</body>
</html>