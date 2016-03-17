<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="order.detail"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/order/list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


<script type="text/javascript" src="${base}/scripts/salesorder/cancel-order-list.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="../images/wmi/blacks/32x32/users.png"><spring:message code="cancel.order.list.applicationOrderList"/>
    </div>
   <form id="searchForm">
    <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
        <table>
            <tr>
                <td><label><spring:message code="order.detail.orderCode"/></label></td>
                <td>
                    <span><input type="text" name="q_string_orderCode" loxiaType="input" mandatory="false" placeholder="<spring:message code='order.detail.orderCode'/>"></input></span>
                </td>
                <td><label><spring:message code="cancel.order.list.appplicationStatus"/></label></td>
                <td>
                      <span>
						<opt:select name="q_long_state" loxiaType="select" expression="chooseOption.CANCEL_ORDER_STATUS" nullOption="role.list.label.unlimit"/>
					</span>
                </td>
                <td><label><spring:message code="order.detail.memberName"/></label></td>
                <td>
                    <span><input type="text" name="q_sl_memberName" loxiaType="input" mandatory="false" placeholder="<spring:message code='order.detail.memberName'/>"></input></span>
                </td>
        	</tr>
			<tr>
				<td><label><spring:message code="cancel.order.list.telephone"/></label></td>
	            <td>
	                  <span><input type="text" name="q_sl_mobile" loxiaType="input" mandatory="false" placeholder="13800000000"></input></span>
	            </td>
	            <td><label><spring:message code="user.list.filter.createtime"/></label></td>
	            <td>
	               <span><input type="text" id="startDate" name="q_date_startDate"  value="" loxiaType="date" mandatory="false" /></span>
	            </td>
	            <td><label>——</label></td>
	            <td>
	                <span><input type="text" id="endDate" name="q_date_endDate"  value="" loxiaType="date" mandatory="false"/></span>
	            </td>
	            
	        </tr>
        </table>
        <div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='btn.search'/>"><span><spring:message code ='btn.search'/></span></a>
        </div>
    </div>
    </div>
    </form>
    <div class="ui-block">
   	 	<div class="border-grey" id="table1" caption="<spring:message code='cancel.order.list.applicationOrderList'/>"></div>
    </div>
</div>


</body>
</html>
