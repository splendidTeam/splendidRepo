<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript">
var $j = jQuery.noConflict();

$j(document).ready(function(){

});
</script>
<script type="text/javascript" src="../scripts/main.js"></script>
</head>

<body>


<div class="dashboard">
	 <div class="dashboard-new" style="margin-top:80px;">
     	  <h1>Hi ${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.realName }，欢迎您登录本系统</h1>
          
          <div class="dash-table">
          	   <div class="dash-title"><h2>用户信息</h2></div>
               
               <table cellpadding="0" cellspacing="0">
               		  <tr>
                      		<td class="bg-grey">用户名</td>
                            <td>${sessionScope.SPRING_SECURITY_CONTEXT.authentication.principal.username }</td>
                      </tr>
                      

                      
                      <tr>
                      		<td class="bg-grey">上次登录时间</td>
                            <td><fmt:formatDate value="${loginlog.loginTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                      </tr>
                      
                      <tr>
                      		<td class="bg-grey">最近修改时间</td>
                            <td><fmt:formatDate value="${user.latestUpdateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                      </tr>
               </table>
          </div>
     </div>
</div>

</body>
</html>
