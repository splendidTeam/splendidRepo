<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<style type="text/css">
.wl-right-auto2
{


border:1px solid #ccc;
overflow-y:scroll;

}
.wl-right-auto2  input[type="checkbox"]
{
clear:both;
}

.wl-right-auto2  input[type="radio"]
{
clear:both;
}
.wl-right-auto2  p
{
width:80%;
}
</style>


<script type="text/javascript" src="${base }/scripts/auth/user/reset-passwd.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
    
   <form name="userForm" action="/user/reset-passwd.htm" method="post">
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/user.png">
		<spring:message code="user.list.label.title" />
	</div>
	
	
	<div class="ui-block">
    <div class="ui-block-title1"><spring:message code="user.modify.label.resetpasswd" /></div>

    <div class="ui-block-content border-grey">
    
	    <div class="ui-block-line">
	         <label><spring:message code="user.modify.label.password" /></label>
	         <div>
	              <input name="password" type="password" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code="user.modify.label.password" />"/>
	         </div>
	    </div>
	    
	    <div class="ui-block-line">
	         <label><spring:message code="user.modify.label.passwordagain" /></label>
	         <div>
	              <input name="passwordAgain" type="password" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code="user.modify.label.passwordagain" />"/>
	         </div>
	    </div>
	</div>
    

	</div>
	<input type="hidden" name="userId" value="${user.id }" >
   </form>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit" />
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />
    </div>
</div>


</body>
</html>
