<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>
<script type="text/javascript" src="${base}/scripts/login.js"></script>
</head>
<input id="type" type="hidden" value="${param.type}"/>
<body class="login-bg content">
<form:form id="login-form" method="post" action="${pageContext.request.contextPath}/j_spring_security_check">
     <div class="login">
          <h5><spring:message code="user.login.label.title" /></h5>
          <div class="login-line">
               <label><spring:message code="user.login.label.username" /></label><input name="j_username" loxiaType="input" mandatory="true" class="fLeft" value=""/>
          </div>
          <div class="login-line">
               <label><spring:message code="user.login.label.password" /></label><input name="j_password" type="password" loxiaType="input" mandatory="true" class="fLeft" value=""/>
          </div>
          <div class="login-line pt15">
               <input type="submit" class="orange submit" value="<spring:message code="user.login.btn.login" />"/>
               <input type="button"  class="reset" value="<spring:message code="btn.reset" />"/>
          </div>
     </div>
</form:form>
</body>
</html>
