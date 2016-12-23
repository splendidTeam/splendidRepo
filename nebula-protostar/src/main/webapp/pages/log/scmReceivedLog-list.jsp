<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/log/scmReceivedLog.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">SCM接收日志列表
	</div>
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
        		<td><label>接口标识</label></td>
				<td><select loxiaType="select" mandatory="false" id="ifIdentify" name="q_sl_ifIdentify">
                    <option value="">不限</option>
					<option value="L1-1">物流跟踪</option>
					<option value="O2-2">付款推送</option> 
					<option value="O2-1">订单推送</option> 
					<option value="L2-1">SF上门取件</option>
					<option value="I1-2">库存同步(增)</option> 
					<option value="I1-1">库存同步(全)</option> 
					<option value="P1-2">商品价格同步</option> 
					<option value="P2-1">在售商品同步</option> 
					<option value="P1-1">商品信息同步</option> 
					<option value="O1-1">订单状态同步(scm2shop)</option> 
					<option value="O2-3">订单状态同步(shop2scm)</option> 
                    </select> 
                 </td>
				
				<td><label>消息Id</label></td>
				<td>
					<input name="q_sl_msgId" type="text" loxiaType="input" ></input>
				</td>
	  	</tr>
		<tr>		
       			<td><label>Msg发送时间</label></td>
				<td>
                    <input type="text" id="startDate" name="q_date_sendTimeStart" loxiaType="date" mandatory="false"></input>
	            </td>
                <td align="center"><label>——</label></td>
                <td>
	            	<input type="text" id="endDate" name="q_date_sendTimeEnd"  loxiaType="date" mandatory="false"></input>
	            </td>
        </tr> 
    </table>
    	<div class="button-line1">
    		<a href="javascript:void(0);" class="func-button reset" title="<spring:message code ='user.list.filter.btn'/>">重置</a>
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="tableList" caption="SCM接收日志"></div>   
    </div>
</div>
</body>
</html>