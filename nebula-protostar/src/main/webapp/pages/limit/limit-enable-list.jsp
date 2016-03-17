<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>启用数量限购列表</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/limit/limit-enable-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">  
         <spring:message code ='promotion.list.enaManagement'/>
    </div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label>限购名称</label></td>
            <td><input type="text" id="name" placeHolder="<spring:message code="promotion.list.name"/>" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>
               
             
            <td>
            	<label><spring:message code="promotion.list.valid"/></label> 
	            <input type="text" id="startTime" name="q_date_startTime" loxiaType="date" mandatory="false"/>
	            <label>——</label>
	            <input type="text" name="q_date_endTime" id="endTime" loxiaType="date" mandatory="false"/>
	            
            </td>
            <td> 
            </td>
            <td></td> 
            <td></td>
        </tr> 
        <tr>
            
          <td><label>受限人群</label></td>
          <td>
               <opt:select id="memberComboType" name="q_long_memberComboType" loxiaType="select" nullOption="全部" expression="chooseOption.MEMBER_FILTER_TYPE"></opt:select>
           </td>
           
           
           <td><label>状态</label>
                 <select loxiaType="select" mandatory="false" id="lifecycle" name="q_Integer_lifecycle">
                 	<option value=""><spring:message code ='promotion.list.all'/></option>
                   <option value="0"><spring:message code ='promotion.list.stuToenabled'/></option>
					<option value="1"><spring:message code ='promotion.list.enable'/></option>
					<option value="2"><spring:message code ='promotion.list.rForce'/></option>
					<option value="3"><spring:message code ='promotion.list.cance'/></option> 
                </select> 
           </td>
          <td>
           </td>
           
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
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code ='promotion.list.enabName'/>"></div>   
    </div>
     
</div>

<%-------------------------------------------------------- 启用确认浮层 --------------------------------------------------------%>
<div id="div-conflict" class="proto-dialog">
	<h5>限购冲突列表</h5>
	<div class="proto-dialog-content">
		<div id="table1" class="table-border0 border-grey ui-loxia-simple-table">
			<table cellspacing="0" cellpadding="0">
				<thead>
					<tr>
						<th width="20%">
							<div>限购名称</div>
						</th>
						<th width="20%">
							<div>开始时间</div>
						</th>
						<th width="20%">
							<div>结束时间</div>
						</th>
						<th width="20%">
							<div>受限人群</div>
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