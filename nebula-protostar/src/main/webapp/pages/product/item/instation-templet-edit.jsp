<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/instation-templet-edit.js"></script>
</head>

<body>

<div class="content-box">
    
    <form name="roleForm" action="/station/saveOrUpdateStationTemplet.json" method="post">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/user.png">添加站内信模板管理
	</div>
	<div class="ui-block">
		<div class="ui-block-title1">站内信模板编辑</div>
		<div class="">
		<input type="hidden" name="id" value="${info.id }" />
		<input type="hidden" name="type" value="${info.type}" />
		<%-- <input type="hidden" name="createTime" value="${info.createTime}" /> --%>
		    <div class="ui-block-line">
		         <label>title</label>
		         <div class="wl-right">
		              <input name="title" style="width:370px;" type="text" loxiaType="input" value="${info.title }" mandatory="true" placeholder="标题"/>
		         </div>
		    </div>
		    <div class="ui-block-line">
		         <label>message</label>
		         <div class="wl-right">
		              <textarea name="message" loxiaType="input" mandatory="true" placeholder="内容,请输入255字以内的描述" style="width: 999px; height: 349px;">${info.message }</textarea>
		         </div>
		    </div>
		</div>
	</div>
    </form>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" class="button orange saveForm" title="<spring:message code="btn.save" />"/>
         <input type="button" value="<spring:message code="btn.return" />" class="button return" title="<spring:message code="btn.return" />"/>
    </div>
</div>


</body>
</html>
