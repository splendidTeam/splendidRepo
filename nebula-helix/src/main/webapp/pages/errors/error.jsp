<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>
<script type="text/javascript" src="${base}/scripts/ZeroClipboard/ZeroClipboard.min.js"></script>
<script type="text/javascript">
$j(document).ready(function(){
	loxia.init({debug:true,locale:"zh-cn"});
	nps.init();
    var clip = new ZeroClipboard($j("#d_clip_button"), {
        moviePath: "${base}/scripts/ZeroClipboard/ZeroClipboard.swf"
    });
    clip.on('complete', function (client, args) {
        nps.info('<spring:message code="error.msg.copy.complete" />', '<spring:message code="error.msg.copy.complete" />');
    });

    $j(".ui-button.mail-button").click(function() {
		var msg = $j("#d_clip_button").attr("data-clipboard-text");
		var errorCode = $j("#error").attr("errorCode");
		var error = $j("#error").attr("error");
		var json = {"errorCode": errorCode, "msg" : msg, "error" : error};
		loxia.lockPage();
		nps.asyncXhrPost('/error/mail.htm', json, {successHandler: function(){
			  nps.info('<spring:message code="success" />','<spring:message code="success" />');
		}});
    });
    $j(".ui-button.add-button").click(function() {
    	if($j(this).hasClass("mini-button")){
    		$j(this).removeClass("mini-button");
    		$j(".error-detail").css("display","none");
    		setTimeout(function(){$j(".error-page").css({"padding-top":((parseInt($j(window).height())-parseInt($j(".error-page").height()))/2)-50,"display":"block"});},1);
    	}
    	else{
    		$j(this).addClass("mini-button");
    		$j(".error-detail").css("display","block");
    		setTimeout(function(){$j(".error-page").css({"padding-top":((parseInt($j(window).height())-parseInt($j(".error-page").height()))/2)-50,"display":"block"});},1);
    	}
    });
});
</script>
</head>

<body>
<div class="error-page" cellpadding="0" cellspacing="0">
	 <h1>ERROR</h1>
     	<c:if test="${exception.statusCode != null}">
     	  	<p>
	     	[${exception.statusCode}] [${exception.message}]
	     	<a id="error" errorCode="${exception.statusCode}" error="${exception.message}"></a>
	     	<c:if test="${exception.statusCode eq '1'}">
		        <input type="button" class="ui-button add-button" value='<spring:message code="error.btn.viewmore" />'/>
		        <input type="button" class="ui-button mail-button" value='<spring:message code="error.btn.sendmail" />'/>
	        </c:if>
	       	</p>
	       
		    <div class="error-detail">
	     		<input type="button" value='<spring:message code="error.btn.copy" />' class="ui-button copy-button my_clip_button" id="d_clip_button" title="Click me to copy to clipboard." data-clipboard-target="fe_text" data-clipboard-text="${exception.stackTrace}"/>
	        	<div class="clear-line height1"></div>
		    	${exception.stackTrace}
	    	</div>
	    </c:if>
	    <c:if test="${exception.statusCode == null}">
	    	<p>
	     		[system error]
	     		<a id="error" errorCode="1" error="system error"></a>
		        <input type="button" class="ui-button add-button" value='<spring:message code="error.btn.viewmore" />'/>
		        <input type="button" class="ui-button mail-button" value='<spring:message code="error.btn.sendmail" />'/>
		    </p>
		    <div class="error-detail">
	     		<input type="button" value='<spring:message code="error.btn.copy" />' class="ui-button copy-button my_clip_button" id="d_clip_button" title="Click me to copy to clipboard." data-clipboard-target="fe_text" data-clipboard-text="${pageContext.exception}"/>
	        	<div class="clear-line height1"></div>
		    	${pageContext.exception}
	    	</div>
	    </c:if>
</div>
<script type="text/javascript">
$j(window).load(function(){
	$j(".error-page").css({"padding-top":((parseInt($j(window).height())-parseInt($j(".error-page").height()))/2)-50,"display":"block"});
	$j(window).resize(function(){
		$j(".error-page").css({"padding-top":((parseInt($j(window).height())-parseInt($j(".error-page").height()))/2)-50,"display":"block"});
	});
});
</script>
</body>
</html>

