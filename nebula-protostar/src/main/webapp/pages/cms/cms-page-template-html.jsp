<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<style type="text/css">
 
.comments { 
width:100%;/*自动适应父布局宽度*/  
overflow:auto;  
word-break:break-all;  
} 

</style>
</head>
<body style="overflow:hidden;" >

<div>
<textarea class="comments" style="height:900px;">
			${pagetemplate.data}</textarea>
</div>
</body>
</html>