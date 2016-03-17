<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/priorityAdjust-selectEdit.js"></script>
<script type="text/javascript" src="${base }/scripts/jquery/jqueryplugin/jquery.json.js"></script> 
<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">  
            促销活动优先级调整 修改
    </div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr> 
            <td>
            	<label>调整时间</label> 
	            <input type="text" id="startTime" name="q_date_startTime"  loxiaType="input" mandatory="false"
	            value="${promotionPriorityAdjust.startTime}" readonly="readonly"/>
	            <label>——</label>
	            <input type="text" name="q_date_endTime" id="endTime"  loxiaType="input"  mandatory="false"
	            value="${promotionPriorityAdjust.endTime}"  readonly="readonly"/>
	            
            </td> 
        </tr> 
        <tr>
             
          <td><label>调整名称</label></label>
               <input type="text" id="adjustname" placeHolder="调整名称" value="${promotionPriorityAdjust.adjustName}"
               name="q_sl_adjustname" loxiaType="input" mandatory="false"/>
               <input type="hidden" name="q_Long_adjustId" id="adjustId" value="${promotionPriorityAdjust.id}"/>
          </td> 
            

        </tr>
    </table> 
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="促销活动优先级调整列表"></div>   
    </div>
     
</div>
<div style="margin-left:1200px; "> 
<label> <input type="button" value="调整重置" class="button refresh" title="调整重置"/></label>
 <label> <input type="button" value="保  存" class="button orange  updatePromotion" title="保存"/></label>
 </div>
        
</body>
</html>