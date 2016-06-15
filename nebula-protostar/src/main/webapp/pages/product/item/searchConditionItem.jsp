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
<script type="text/javascript" src="${base }/scripts/product/item/searchConditionItem.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<SCRIPT type="text/javascript">
//<spring:message code="industry.list.expand"/>ROOT
var category_ZNodes = [
		{id:0, pId:-1, name:"ROOT",
			  code:"ROOT", sortNo:1,
			  open:true, lifecycle:1},  
              <c:forEach var="navigation" items="${naviList}" varStatus="status">
              	
              	{id:${navigation.id}, pId:${navigation.parentId}, name:"${navigation.name}", 
              		code:"${navigation.id}", sortNo:${navigation.sort}, 
              		open:false, lifecycle:${navigation.lifecycle}}
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];
</SCRIPT>
</head>
<body>
	==============${naviList}
<div class="content-box width-percent100">
	<input type="hidden" id="pid" value="${searchConditionVo.id}"  mandatory="false"/>
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
    <spring:message code='item.searchCodition.coditionItem'/>
	    <input type="button" value="<spring:message code='item.searchCodition.add'/>" codid="${searchConditionVo.id}" class="button orange addCondition" title="<spring:message code='item.searchCodition.add'/>"/>
	    <input type="button" id="deleteAll" value="<spring:message code='item.searchCodition.deleteAll'/>" class="button orange addcomboproduct" title="<spring:message code='item.searchCodition.deleteAll'/>"/>
	    <input type="button" value="<spring:message code='item.searchCodition.back'/>" class="button orange backCondition" title="<spring:message code='item.searchCodition.back'/>"/>
    </div>
    <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tbody><tr>
                    <td><label>
                    <c:choose>
		        		<c:when test="${searchConditionVo.type!=1}">
		        			<spring:message code='item.searchCodition.byCodition'/>：
		        		</c:when>
		        		<c:otherwise>
		        			<spring:message code='item.searchCodition.byProperty'/>:
		        		</c:otherwise>
		        	</c:choose>
                    </label></td>
                    <td><span>
						<c:choose>
		        		<c:when test="${searchConditionVo.type!=1}">
		        			${searchConditionVo.name}
		        		</c:when>
		        		<c:otherwise>
		        			${searchConditionVo.propertyName}
		        		</c:otherwise>
		        		</c:choose>
						</span></td>
                </tr>
            </tbody></table>
    </div>
           
    
	
    </div>
	<form id="searchForm">
	  <div class="ui-block">
	
    <div class="ui-block-content ui-block-content-lb">
	    <table > 
	    	<tr style="display:none;">
	        	<td><label></label></td>
	            <td>
	            	<input type="text" id="type" name="q_int_type" value="${searchConditionVo.type}" loxiaType="input" mandatory="false"/>
	            </td>
	        </tr>
	        <c:choose>
	        <c:when test="${searchConditionVo.type!=1}">
	        <tr style="display:none;">
	        	<td><label></label></td>
	            <td>
	            	<input type="text" id="coditionId" name="q_long_coditionId" value="${searchConditionVo.id}" loxiaType="input" mandatory="false"/>
	            </td>
	        </tr>
	        </c:when>
	        <c:otherwise>
	        <tr style="display:none;">
	        	<td><label></label></td>
	            <td>
	            	<input type="text" id="propertyId" name="q_long_propertyId" value="${searchConditionVo.propertyId}" loxiaType="input" mandatory="false"/>
	            </td>
	        </tr>
	        </c:otherwise>
	        </c:choose>
	        <tr>
	        	<td><label><spring:message code='item.searchCodition.itemName'/></label></td>
	            <td><input type="text" id="name" placeHolder="<spring:message code='item.searchCodition.itemName'/>" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>
	        </tr>
	 
	    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code='item.searchCodition.search'/>"><spring:message code='item.searchCodition.search'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code='item.searchCodition.coditionItemList'/>"></div>   
    </div> 
</div>
