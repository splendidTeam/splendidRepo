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
<script type="text/javascript" src="${base}/scripts/log/sysAuditLog-list.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">审计日志查询列表
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
       			<td><label>请求功能</label></td>
				<td>
					<input name="q_sl_uri" type="text" loxiaType="input" ></input>
				</td>
       			<td><label>请求类型</label></td>
				<td>
					<span id="searchkeytext"> 
                		<select name="q_string_method" loxiaType="select" id="method" >
                			<option value ="">不限</option>
                			<option value ="GET">GET</option>
                			<option value ="POST">POST</option>
                		</select>
					</span>
				</td>
				
       			<td><label>请求结果</label></td>
				<td>
					<input name="q_sl_responseCode" type="text" loxiaType="input" ></input>
				</td>
		</tr>
		<tr>
       			<td><label>异常</label></td>
				<td>
					<span id="searchkeytext"> 
                		<select name="q_string_exception" loxiaType="select" id="exception" >
                			<option value ="">不限</option>
                			<option value ="empty">空</option>
                			<option value ="nonempty">非空</option>
                		</select>
					</span>
				</td>
       			<td><label>操作人</label></td>
				<td>
					<input name="q_sl_operatorName" type="text" loxiaType="input" ></input>
				</td>
				<td><label>请求来源ip</label></td>
				<td>
					<input name="q_sl_ip" type="text" loxiaType="input" ></input>
				</td>
		</tr>
		<tr>
       			<td><label>操作时间</label></td>
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
   	 	<div class="table-border0 border-grey" id="tableList" caption="审计日志查询列表"></div>   
    </div>
</div>
</body>
</html>