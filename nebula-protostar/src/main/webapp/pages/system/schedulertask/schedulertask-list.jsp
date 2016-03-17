<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/system/schedulertask/schedulertask-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
    定时任务管理
    <input type="button" value="<spring:message code='item.searchCodition.add'/>" class="button orange addCondition" title="<spring:message code='item.searchCodition.add'/>"/>
    <input type="button" id="deleteAll" value="<spring:message code='item.searchCodition.deleteAll'/>" class="button orange deleteCondition" title="<spring:message code='item.searchCodition.deleteAll'/>"/>
    </div>  
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
	    <table > 
	        <tr>
	        	<td><label>任务编码</label></td>
	            <td><input type="text" id="code" placeHolder="任务编码" name="q_sl_code" loxiaType="input" mandatory="false"></input></td>  
	                  
			   <td><label>状态</label></td>
	             <td>
	             	<opt:select id="lifecycle" name="q_long_lifecycle" loxiaType="select" nullOption="未选择" defaultValue="" expression="chooseOption.IS_AVAILABLE"/>
	             </td>
	        </tr>
	 
	    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code='item.searchCodition.search'/>"><spring:message code='item.searchCodition.search'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="定时任务管理列表"></div>   
    </div>
</div>
</body>
</html>
