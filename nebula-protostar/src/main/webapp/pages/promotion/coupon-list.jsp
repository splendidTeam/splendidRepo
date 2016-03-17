<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp"%>

<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/coupon-list.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
</head>

<body>

<div class="content-box width-percent100">
<div class="ui-title1">
	<img src="${base }/images/wmi/blacks/32x32/calc.png">优惠券列表
	<input class="button orange import" type="button" title="导入" value="导入">
</div>
<form id="searchForm">
<div class="ui-block">
<div class="ui-block-content ui-block-content-lb">
<table>

	<tr>
		<td><label>券码</label></td>
		<td> <input type="text" loxiaType="input" mandatory="false" id="couponName" name="q_sl_couponCode" /></td>
		<td><label>优惠券名称</label></td>
		<td><select loxiaType="select" mandatory="false" id="couponType"name="q_long_couponType">
			<option value=""><spring:message code="member.group.label.unlimit" /></option>
			<c:forEach var="item" items="${couponTypeList}">
				<option value="${item.id}">${item.couponName}</option>
			</c:forEach>
			</select>
		</td>
		<td><label>状态</label></td>
		<td>
			<opt:select id="isused" name="q_int_isused" loxiaType="select" expression="chooseOption.COUPON_STATE" nullOption="member.group.label.unlimit" />
		</td>
	</tr>

	<tr>
		<td><label>有效期</label></td>
		<td><input type="text" id="startTime" name="q_date_startTime" loxiaType="date" mandatory="false" /></td>
		<td> ——</td>
		<td> <input type="text" name="q_date_endTime" id="endTime" loxiaType="date"	mandatory="false" /></td> 
		<td>&nbsp;</td>
		<td>&nbsp;</td> 
		<td>&nbsp;</td>
	</tr>


</table>
<div class="button-line1">
	<a href="javascript:void(0);"	class="func-button search"	title="<spring:message code ='user.list.filter.btn'/>">
			<spring:message	code='user.list.filter.btn' /></a>
 </div>
</div>
</div>
</form>

<div class="ui-block">

<div class="table-border0 border-grey" id="table1" caption="优惠券列表"></div>
</div>

<div class="button-line">
	<input class="button orange import" type="button" title="导入" value="导入">
</div>

</div>
</body>
</html>