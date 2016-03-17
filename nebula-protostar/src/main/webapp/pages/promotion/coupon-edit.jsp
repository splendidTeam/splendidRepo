<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/coupon-edit.js"></script>
<script type="text/javascript" src="${base }/scripts/jquery/jqueryplugin/jquery.json.js"></script> 
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<style type="text/css">
	.ui-loxia-simple-table table .highlight td{
		background-color: pink;
	}

	.ui-loxia-simple-table table tr.highlight:hover td{
		background-color: pink;
	}
</style>
</head>

<body>
<div class="content-box width-percent100">

	<div class="ui-block">	
	 <div class="ui-block-title1">    优惠劵类型-<c:if test="${optype eq 'new'}">新增</c:if><c:if test="${optype eq 'view'}">查看</c:if><c:if test="${optype eq 'edit'}">编辑</c:if></div>
   <form id="couponForm" name="couponForm" action="/coupon/createOrupdate.json" method="post">
		<div class="ui-block-content border-grey">
			<div class="ui-block-line mt5">
				<label>优惠劵名称</label>
				<input id="couponName" name ="couponName" placeHolder="优惠劵名称" loxiaType="input" mandatory="true" value="${coupon.couponName}" />
			</div>
			<div class="ui-block-line mt5">
				<label>优惠劵类型</label>
				<!-- nullOption="member.group.label.unlimit" -->
				<opt:select id="type" name="type" loxiaType="select"  expression="chooseOption.CP_TYPE" defaultValue="${coupon.type}"  />
			</div>
			<div class="ui-block-line mt5">
				<label>金额/折扣</label>
				<input id="discount"  name = "discount" placeHolder="金额/折扣" loxiaType="input"  mandatory="true" value="${coupon.discount }" />
			</div>
			<div class="ui-block-line mt5">
				<label>状态</label>
				<opt:select loxiaType="select"  id="activeMark" name="activeMark"  defaultValue="${coupon.activeMark}" expression="chooseOption.IS_AVAILABLE"  />
			</div>
			<div class="ui-block-line mt5">
				<label>使用次数</label>
				<input id="count"  name = "limitTimes" placeHolder="使用次数" loxiaType="number"  mandatory="true" <c:if test="${coupon != null}">value="${coupon.limitTimes}"</c:if><c:if test="${coupon == null}">value="1"</c:if> />
			</div>
			 <input type="hidden" name="id" value="${coupon.id}">
			 <input type="hidden" id= "optype" name="optype" value="${optype}">
		</div>
	</form>
</div>
</div>


<div class="button-line">
	<c:if test="${optype ne 'view'}">
		<input type="button" value="<spring:message code="btn.save" />" class="button orange saveForm" title="<spring:message code="btn.save" />"/>
	</c:if>
	<input type="button" value="<spring:message code="btn.return" />" class="button return" title="<spring:message code="btn.return" />"/>
</div>


</body>
</html>