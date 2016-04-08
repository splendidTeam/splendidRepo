<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/property/edit-property-value.js"></script>
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
		                    	<input type="text" name="q_sl_code" loxiaType="input" mandatory="false" placeholder="<spring:message code="shop.property.group"/>"></input>
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
	    	 <div class="priDiv" style="margin-left:0px;">
	          	<span class="children-store"><input type="checkbox" id="color" value="红色"/>红色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="橙色"/>橙色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="黄色"/>黄色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="绿色"/>绿色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="青色"/>青色</span>
	            <span class="children-store"><input type="checkbox" id="color" value="蓝色"/>蓝色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="紫色"/>紫色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="酒红色"/>酒红色</span> 
	         </div>
	    </div>
	    
    </div>
	
	<div class="ui-block-title1"><spring:message code="shop.property.value.available"/></div>
	<div class="clear-line height10"></div>
    <div class="ui-block">
	    <div class="ui-block-line " >
	    	 <div class="priDiv" style="margin-left:0px;">
	          	<span class="children-store"><input type="checkbox" id="color" value="红色"/>红色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="橙色"/>橙色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="黄色"/>黄色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="绿色"/>绿色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="青色"/>青色</span>
	            <span class="children-store"><input type="checkbox" id="color" value="蓝色"/>蓝色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="紫色"/>紫色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="酒红色"/>酒红色</span> 
	            	          	<span class="children-store"><input type="checkbox" id="color" value="红色"/>红色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="橙色"/>橙色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="黄色"/>黄色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="绿色"/>绿色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="青色"/>青色</span>
	            <span class="children-store"><input type="checkbox" id="color" value="蓝色"/>蓝色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="紫色"/>紫色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="酒红色"/>酒红色</span> 
	            	          	<span class="children-store"><input type="checkbox" id="color" value="红色"/>红色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="橙色"/>橙色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="黄色"/>黄色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="绿色"/>绿色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="青色"/>青色</span>
	            <span class="children-store"><input type="checkbox" id="color" value="蓝色"/>蓝色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="紫色"/>紫色</span> 
	            <span class="children-store"><input type="checkbox" id="color" value="酒红色"/>酒红色</span> 
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
