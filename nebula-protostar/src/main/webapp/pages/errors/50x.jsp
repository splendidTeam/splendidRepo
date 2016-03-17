<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>
</head>

<div class="error-page" cellpadding="0" cellspacing="0">
	 <h1>ERROR</h1>
     <p><spring:message code="business_exception_30001" /></p>
</div>
<script type="text/javascript">
$j(window).load(function(){
	$j(".error-page").css({"padding-top":((parseInt($j(window).height())-parseInt($j(".error-page").height()))/2)-50,"display":"block"});
	$j(window).resize(function(){
		$j(".error-page").css({"padding-top":((parseInt($j(window).height())-parseInt($j(".error-page").height()))/2)-50,"display":"block"});
	});
});
</script>
