<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>

<script type="text/javascript" src="${base }/scripts/jquery/jqueryplugin/jquery.json.js"></script> 
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/system/email/edit.js"></script>
<style type="text/css">
	.ui-loxia-simple-table table .highlight td{
		background-color: pink;
	}
	.ui-loxia-simple-table table tr.highlight:hover td{
		background-color: pink;
	}
	.autoCLine{
	 word-break: break-all;
	}
</style>
</head>

<body>
<div class="content-box width-percent100">
<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png"><spring:message code="email.list.email.mng"/>
</div>
<div class="ui-block">	
	 <div class="ui-block-title1"><spring:message code="email.edit.tmp"/>-<c:if test="${optype eq 'new'}"><spring:message code="btn.add"/></c:if><c:if test="${optype eq 'edit'}"><spring:message code="promotion.edit"/></c:if></div>
   <form id="couponForm" name="emailForm" action="/coupon/createOrupdate.json" method="post">
		<div class="ui-block-content border-grey email-content">
		  <div class="ui-block-line p5">
		   <label><spring:message code="product.property.lable.code"/></label><input class="input_add code" type="text" loxiaType="input" <c:if test="${optype eq 'edit'}">readonly="readonly"</c:if> mandatory="true" value="${email.code}"  >
		  </div>
		  <div class="ui-block-line p5">
		    <label><spring:message code="member.group.name"/></label><input class="input_add name"  type="text" loxiaType="input"  mandatory="true" value="${email.name}"  >
		  </div>
		  <div class="ui-block-line p5">
		   <label ><spring:message code="cms.page.add.descriptioninfo"/></label><textarea   style="width: 400px;height: 100px;resize: none;" class="description autoCLine" rows="10" cols="100" loxiaType="input"  >${email.description}</textarea>
		   </div>
		  <div class="ui-block-line p5">
	       <label ><spring:message code="navigation.list.type"/></label><opt:select id="type"  loxiaType="select" expression="chooseOption.EMAIL_TEMPLATE_TYPE" defaultValue="${email.type}" />
	       </div>
	        <div class="ui-block-line p5">
           <label>发件人</label><input class="input_add" style="width: 300px"  type="text" loxiaType="input" id ="senderEmail" value="${email.senderEmail}"  >
          </div>
           <div class="ui-block-line p5">
           <label>发件人别名</label><input class="input_add" style="width: 300px"  type="text" loxiaType="input"  id="senderAlias" value="${email.senderAlias}"  >
          </div>
		  <div class="ui-block-line p5">
           <label><spring:message code="email.edit.subject"/></label><input class="input_add subject" style="width: 300px"  type="text" loxiaType="input"  mandatory="true" value="${email.subject}"  >
          </div>
		  <div class="ui-block-line p5">
             <label ><spring:message code="email.edit.body"/></label><textarea  style="width: 800px;height: 200px;resize: none;"  class="input_add body autoCLine"  rows="10" cols="100" loxiaType="input" mandatory="true" >${email.body}</textarea>
          </div>
			 <input type="hidden" id="emailId" name="id" value="${email.id}">
			 <input type="hidden" id= "optype" value="${optype}">
		</div>
	</form>
</div>
</div>

<div class="button-line">
	<c:if test="${optype ne 'view'}">
		<input  type="button" value="<spring:message code="btn.save" />" class="button orange saveForm" title="<spring:message code="btn.save" />"/>
	</c:if>
	<input type="button" value="<spring:message code="btn.return" />" class="button return" title="<spring:message code="btn.return" />"/>
</div>

</body>
</html>
