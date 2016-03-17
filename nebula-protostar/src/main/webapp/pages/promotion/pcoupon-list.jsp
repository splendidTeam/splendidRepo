<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/pcoupon-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">优惠劵类型管理
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addcoupon" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label>优惠券名称</label></td>
            <td><input type="text" name="q_sl_couponName" placeHolder="优惠券名称" name="q_sl_promotionName" loxiaType="input" mandatory="false"></input></td>
            <td><label>优惠劵类型</label></td>
			<td>
			<opt:select id="isused" name="q_int_type" loxiaType="select" expression="chooseOption.CP_TYPE" nullOption="member.group.label.unlimit" />
			</td>
            <td><label>状态</label></td>
			<td>
			<opt:select id="isused" name="q_int_activeMark" loxiaType="select" expression="chooseOption.IS_AVAILABLE" nullOption="member.group.label.unlimit" />
			</td>
            <td></td> 
            <td></td>
        </tr> 
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="table1" caption="优惠劵类型管理列表"></div>   
    </div>
     
</div>

</body>
</html>