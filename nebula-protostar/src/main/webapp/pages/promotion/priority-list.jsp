<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/priority-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">  
            促销优先级
         <input type="button" value="创建优先级" class="button orange btn-create" title="创建优先级"/>
    </div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
    	<tr>
          <td>
          	<label>调整名称</label></label>
            <input type="text" id="adjustName" placeHolder="调整名称" 
               name="q_sl_adjustName" loxiaType="input" mandatory="false" />
          	<label>状态</label></label>
            <select loxiaType="select" id="status" name="q_int_status">
            	<option value="">不限</option>
            	<option value="0">待启用</option>
            	<option value="1">已启用</option>
            	<option value="3">已禁用</option>
            </select>
          </td> 
        </tr>
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button btn-search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="table1" caption="促销活动优先级调整列表"></div>   
    </div>
     
</div>
 
</body>
</html>