<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>限购编辑列表</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/limit/limit-edit-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body> 

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png">
		<spring:message code="promotion.editorList.title"/>
        <input type="button" value="<spring:message code="promotion.editorList.create"/>" class="button orange  addPromotion" title="<spring:message code="promotion.editorList.create"/>"/>
         
    </div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label>限购名称</label></td>
            <td><input type="text" id="name" placeHolder="<spring:message code="promotion.list.name"/>" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>
               
             
            <td><label><spring:message code="promotion.list.valid"/></label></td>
            <td><input type="text" id="startTime" name="q_date_startTime" loxiaType="date" mandatory="false"></input>
            </td>
            
            <td><label>——</label></td>
            <td>
            	<input type="text" name="q_date_endTime" id="endTime" loxiaType="date" mandatory="false"></input>
            </td>
            
          </tr> 
          <tr>
            
          <td><label>受限人群</label></td>
          <td>  
			<opt:select id="memberComboType" name="q_long_memberComboType" loxiaType="select" nullOption="全部" expression="chooseOption.MEMBER_FILTER_TYPE"></opt:select>
           </td> 
            
            
           

        </tr>
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    <div class="ui-block">
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code="promotion.editorList.cea"/>"></div>   
    </div>
     
</div>


</body>
</html>