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
<script type="text/javascript" src="${base}/scripts/log/schedulerLog-list.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/clock.png">定时任务运行日志
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
       			<td><label>任务编码</label></td>
				<td>
					<input name="q_sl_code" type="text" loxiaType="input" placeholder="任务编码"></input>
				</td>
       			<td><label>Bean Name</label></td>
				<td>
					<input name="q_sl_beanName" type="text" loxiaType="input" placeholder="Bean Name"></input>
				</td>
				
       			<td><label>方法名称</label></td>
				<td>
					<input name="q_sl_methodName" type="text" loxiaType="input" placeholder="方法名称"></input>
				</td>
		</tr>
		<tr>
				<td><label>时间</label></td>
				<td>
                    <input name="q_date_beginTime" type="text"  loxiaType="date" mandatory="false"></input>
	            </td>
                <td align="center"><label style="left: -10px;">——</label></td>
                <td>
	            	<input name="q_date_endTime" type="text"  loxiaType="date" mandatory="false"></input>
	            </td>
	            <td><label>运行时长</label></td>
				<td>
                    <input name="q_long_min" type="text"  loxiaType="input" placeholder="毫秒值"></input>
	            </td>
                <td><label style="left: -10px;">——</label></td>
                <td>
	            	<input name="q_long_max" type="text"  loxiaType="input" placeholder="毫秒值"></input>
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
   	 	<div class="table-border0 border-grey logs" caption="定时任务运行日志"></div>   
    </div>
</div>
</body>
</html>