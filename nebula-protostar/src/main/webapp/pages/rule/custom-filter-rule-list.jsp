<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/rule/custom-filter-rule-list.js"></script>

<script type="text/javascript" src="${base}/scripts/main.js"></script>

</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">自定义筛选器条件管理
    	<input type="button" value="创建" class="button orange addcsfiltercls" title="创建"/>
    </div>  
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table > 
        <tr> 
            <td><label>筛选器条件名称</label></td>
            <td><input type="text" id="scopeName" placeHolder="筛选器条件名称" name="q_sl_scopeName" loxiaType="input" mandatory="false"></input></td>  
                  
            <!--ChooseOption  PRODUCT_CUSTOMIZE_FILTER-->      
		   	<td><label>筛选器条件类型</label></td>
            <td> 
            	<span>
					<opt:select name="q_long_scopeType" loxiaType="select" expression="chooseOption.PRODUCT_CUSTOMIZE_FILTER" nullOption="option.label.unlimit"/>
				</span> 
            </td>
		   	<td><label>状态</label></td>
            <td> 
				<select name="q_int_lifecycle" loxiaType="select">
					<option value="">不限</option>
					<option value="1">启用</option>
					<option value="0">禁用</option>
				</select>
            </td>

        </tr>
 
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='btn.search'/>"><spring:message code ='btn.search'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="自定义筛选器条件列表"></div>   
    </div> 
</div>
</body> 		  

</html>
