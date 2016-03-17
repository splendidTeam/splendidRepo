<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base }/scripts/updateUser.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
</head>

<body>

<div class="content-box width-percent100">
    
   <form name="userForm" action="/saveUser.json" method="post">
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/user.png">
		<spring:message code="user.updateuser.title" />
	</div>
	
	<div class="ui-block">
        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tbody><tr>
                    <td><label><spring:message code="user.modify.label.username" /></label></td>
                    <td><span>${user.userName }</span></td>
                    
                </tr>
            </tbody></table>
        </div>
	</div>
	
	
	<div class="ui-block">
    <div class="ui-block-title1"><spring:message code="user.updateuser.label.title" /></div>

    <div class="ui-block-content border-grey">
    
	    <div class="ui-block-line">
	         <label><spring:message code="user.modify.label.password" /></label>
	         <div>
	              <input name="password" type="password" loxiaType="input" value=""  placeholder="<spring:message code="user.updateuser.placeholder.pass" />"/>
	         </div>
	    </div>
	    
	    <div class="ui-block-line">
	         <label><spring:message code="user.modify.label.passwordagain" /></label>
	         <div>
	              <input name="passwordAgain" type="password" loxiaType="input" value=""  placeholder="<spring:message code="user.updateuser.placeholder.pass" />"/>
	         </div>
	    </div>
	    
		<div class="ui-block-line">
	         <label><spring:message code="user.modify.label.email" /></label>
	         <div>
	              <input name="email" type="text" loxiaType="input" value="${user.email }" mandatory="true"/>
	         </div>
	    </div>
	    
	    <div class="ui-block-line">
	         <label><spring:message code="user.modify.label.realname" /></label>
	         <div>
	              <input name="realName" type="text" loxiaType="input" value="${user.realName }" mandatory="true"/>
	         </div>
	    </div>
	    
	    <div class="ui-block-line">
	         <label><spring:message code="user.modify.label.mobile" /></label>
	         <div>
	              <input type="text" name="mobile" loxiaType="input" value="${user.mobile }" mandatory="true"/>
	         </div>
	    </div>
	    
	</div>
    

	</div>
	<input type="hidden" name="userId" value="${user.id }" >
   </form>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit" />
         
    </div>
</div>


</body>
</html>
