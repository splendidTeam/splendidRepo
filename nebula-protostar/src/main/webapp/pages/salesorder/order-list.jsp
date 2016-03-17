<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="order.list.manage"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/salesorder/order-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/users.png"><spring:message code="order.list"/>
	        <input type="button" value="手工下单" title="手工下单"  class="button orange manualCreate" /> 
			<input type="button" value="游客下单" title="游客下单"  class="button orange guestCreate" />
			<input type="button" value="会员下单" title="会员下单"  class="button orange memberCreate" />
    </div>
    <form id="searchForm">
	    <div class="ui-block">
		    <div class="ui-block-content ui-block-content-lb">
		        <table>
		            <tr>
		                <td><label><spring:message code="order.list.code"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_code" loxiaType="input" mandatory="false" placeholder="<spring:message code="order.list.codeholder"/>"></input>
		                    </span>
		                </td>
		                <td><label>OMS<spring:message code="order.list.code"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_omsCode" loxiaType="input" mandatory="false" placeholder="OMS<spring:message code="order.list.code"/>"></input>
		                    </span>
		                </td>
		                <td><label><spring:message code="order.list.consigneename"/></label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="text" name="q_sl_name" loxiaType="input" mandatory="false" placeholder="<spring:message code="order.list.consigneeholder"/>"></input>
		                    </span>
		                </td>
		        	</tr>
		        	<tr>
		                <td><label><spring:message code="order.list.logisticsstatus"/></label></td>
			            <td>
			               <span id="searchkeytext">
								<opt:select name="q_long_logisticsStatus" loxiaType="select" expression="chooseOption.ORDER_STATUS_LOGISTICS" nullOption="role.list.label.unlimit"/>
							</span>
			            </td>
		                <td><label><spring:message code="order.list.financialstatus"/></label></td>
			            <td>
			               <span id="searchkeytext">
								<opt:select name="q_long_financialStatus" loxiaType="select" expression="chooseOption.ORDER_STATUS_FINANCIAL" nullOption="role.list.label.unlimit"/>
							</span>
			            </td>
		                <td><label><spring:message code="member.group.source"/></label></td>
		                <td>
		                <span>
							<span id="searchkeytext">
		                        <opt:select name="q_long_source" loxiaType="select" expression="chooseOption.ORDER_SOURCE" nullOption="role.list.label.unlimit"/>
		                    </span>
						</span>
		                </td>
		        	</tr>
					<tr>
			            <td><label><spring:message code="user.list.filter.createtime"/></label></td>
			            <td>
			               <span><input type="text" id="startDate" name="q_date_startDate"  value="" loxiaType="date" mandatory="false" /></span>
			            </td>
			            <td><label>——</label></td>
			            <td>
			                <span><input type="text" id="endDate" name="q_date_endDate"  value="" loxiaType="date" mandatory="false"/></span>
			            </td>
			            <td><label><spring:message code="member.group.membername"/></label></td>
		                <td>
			                <span>
								<span id="searchkeytext">
			                    	<input type="text" name="q_sl_memberName" loxiaType="input"  mandatory="false" placeholder="<spring:message code="member.group.membername"/>"></input>
			                    </span>
							</span>
		                </td>
			        </tr>
		        </table>
		        <div class="button-line1">
                	<a href="javascript:void(0);" class="func-button search"><span><spring:message code ='btn.search'/></span></a>
            	</div>
		    </div>
	    </div>
    </form>
    <div class="ui-block">
   	 	<div class="border-grey" id="table1" caption="<spring:message code="order.list"/>"></div>
    </div>
</div>


</body>
</html>
