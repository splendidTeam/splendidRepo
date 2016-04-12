<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/property/property-value-group.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<style type="text/css">
.i18n-lang{
	display: none;
}
.lang_lable{
width: 15%;
display:block;
float: left;
text-align: left;
padding-left: 3px;
}
tbody input[type="text"]{
	float: left;
	width:85%;
}
.ui-block {
    width: 100%;
    height: auto;
    padding-bottom: 25px;
}
</style>
</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/wrench.png"><spring:message code="system.property.manager"/>
        <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
		<input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
    </div>
        <form id="searchForm">
	    <div class="ui-block">
		    <div class="ui-block-content ui-block-content-lb">
		        <table>
		            <tr>
		                <td><label><spring:message code="shop.property.group"/>:</label></td>
		                <td>
		                    <span id="searchkeytext">
		                    	<input type="hidden" id="groupId" value="${proValueGroup.id}" />
		                    	<input type="hidden" id="propertyId" value="${propertyId}" />
		                    	<input type="text" id="groupName" name="groupName" loxiaType="input" mandatory="false" 
		                    		value="${proValueGroup.name}" placeholder="<spring:message code="shop.property.group"/>" 
		                    	/>
		                    </span>
		                </td>
		        	</tr>
		        </table>
		    </div>
	    </div>
    </form>
    
    <div class="clear-line height10"></div>
    
    <div class="ui-block-title1"><spring:message code="shop.property.value.selected"/></div>
    
    <div class="ui-block">
	    <div class="ui-block-line " >
	    	 <div class="priDiv boundPropertyValue" style="margin-left:0px;">
	    	 	<c:forEach items="${boundProValueList}" var="propertyValue">
		          	<span class="children-store"><input type="checkbox" checked='checked' value="${propertyValue.id}"/>${propertyValue.value }</span> 
	    	 	</c:forEach>
	         </div>
	    </div>
	    
    </div>
	
	
	<div class="clear-line height10"></div>
	<div class="clear-line height10"></div>
	  
	<div class="ui-block-title1"><spring:message code="shop.property.value.available"/></div>
	<div class="clear-line height10"></div>
    <div class="ui-block">
	    <div class="ui-block-line " >
	    	 <div class="priDiv freePropertyValue" style="margin-left:0px;">
	    	 	<c:forEach items="${freeProValueList}" var="propertyValue">
		          	<span class="children-store"><input type="checkbox" value="${propertyValue.id}"/>${propertyValue.value }</span> 
	    	 	</c:forEach>
	       	 </div>
	    </div>
	    
    </div>
 
 
 	<div class="button-line">
        <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
        <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
	</div>

</div>



</body>
</html>
