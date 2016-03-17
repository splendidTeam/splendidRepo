<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/promotion/promotion-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">促销活动启用管理
    </div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label><spring:message code="promotion.list.name"/></label></td>
            <td><input type="text" id="promotionName" placeHolder="<spring:message code="promotion.list.name"/>" name="q_sl_promotionName" loxiaType="input" mandatory="false"></input></td>
            <td><label><spring:message code="promotion.list.valid"/></label> </td>
            <td><input type="text" id="startTime" name="q_date_startTime" loxiaType="date" mandatory="false"/></td>
	        <td><label>——</label></td>
	        <td><input type="text" name="q_date_endTime" id="endTime" loxiaType="date" mandatory="false"/></td>
            <td><label>促销条件</label></td> 
			<td><opt:select name="q_string_conditionType" loxiaType="select" expression="chooseOption.PROMOTION_CONDITION_TYPE" nullOption="全部" /></td>
        </tr> 
        <tr>
            
          <td><label><spring:message code="promotion.list.activities"/></label></td>
          <td><opt:select loxiaType="select" id="memComboType" name="q_long_memComboType" expression="chooseOption.MEMBER_FILTER_TYPE" nullOption="全部" /></td>
          <td>生命周期</td>
          <td>
	      		<select loxiaType="select" name="q_Integer_lifecycle" id="lifecycle">
           	 		<option value="">全部</option>
					<option value="0"><spring:message code ='promotion.list.stuToenabled'/></option>
					<option value="1"><spring:message code ='promotion.list.enable'/></option>
					<option value="2" ><spring:message code ='promotion.list.rForce'/></option>
					<option value="3"><spring:message code ='promotion.list.cance'/></option>
				</select>
          </td>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="table1" caption="促销活动启用列表"></div>   
    </div>
</div>

<%-------------------------------------------------------- 查看浮层 --------------------------------------------------------%>
<div id="div-conflict" class="proto-dialog">
	<h5>促销冲突列表</h5>
	<div class="proto-dialog-content">
		<div id="table1" class="table-border0 border-grey ui-loxia-simple-table">
			<table cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th width="20%">
							<div>促销名称</div>
						</th>
						<th width="20%">
							<div>开始时间</div>
						</th>
						<th width="20%">
							<div>结束时间</div>
						</th>
						<th width="20%">
							<div>受益人群</div>
						</th>
						<th width="20%">
							<div>商品范围</div>
						</th>
					</tr>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
	</div>
	<div class="proto-dialog-button-line" style="">
		<input type="button" value="确认启用" class="button orange btn-ok" /> 
	</div>
</div>
</body>
</html>