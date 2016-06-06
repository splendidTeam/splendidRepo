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
<script type="text/javascript" src="${base}/scripts/log/skuInventoryChangeLog.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">库存变更日志列表
	</div>
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
        		<td><label>商品编码</label></td>
				<td>
					<input name="q_sl_itemCode" type="text" loxiaType="input" ></input>
				</td>
				<td><label>商品名称</label></td>
				<td>
					<input name="q_sl_itemTitle" type="text" loxiaType="input" ></input>
				</td>
       			<td><label>商品UPC</label></td>
				<td>
					<input name="q_sl_extentionCode" type="text" loxiaType="input" ></input>
				</td>
		</tr>
		<tr>	
				<td><label>操作者账号</label></td>
				<td>
					<input name="q_sl_operatorName" type="text" loxiaType="input" ></input>
				</td>
       			<td><label>上下架状态</label></td>
                <td> 
                	<span id="searchkeytext"> 
                		<opt:select name="q_int_lifecycle" loxiaType="select" id="lifecycle" expression="chooseOption.ITEM_STATUS" nullOption="role.list.label.unlimit"  />
					</span> 
				</td>
		</tr>
		<tr>
				<td><label>变化类型</label></td>
                <td> 
                	<span id="searchkeytext"> 
                		<opt:select name="q_int_type" loxiaType="select" id="type" expression="chooseOption.SKUINV_CHANGE_LOG_TYPE" nullOption="role.list.label.unlimit"  />
					</span> 
				</td>
       			<td><label>变化来源</label></td>
                <td> 
                	<span id="searchkeytext"> 
                		<opt:select name="q_int_source" loxiaType="select" id="source" expression="chooseOption.SKUINV_CHANGE_LOG_SOURCE" nullOption="role.list.label.unlimit"  />
					</span> 
				</td>
		</tr>
		<tr>		
       			<td><label>变化时间</label></td>
				<td>
                    <input type="text" id="startDate" name="q_date_createStartDate" loxiaType="date" mandatory="false"></input>
	            </td>
                <td align="center"><label>——</label></td>
                <td>
	            	<input type="text" id="endDate" name="q_date_createEndDate"  loxiaType="date" mandatory="false"></input>
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
   	 	<div class="table-border0 border-grey" id="tableList" caption="库存变更日志列表"></div>   
    </div>
</div>
</body>
</html>