<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/industry/property-value.js"></script>
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
</style>
</head>

<body>
<div class="content-box">

	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/spechbubble.png"><spring:message code="system.property.manager"/>
		<input type="button" value="<spring:message code="btn.returnlist"/>" class="button returnlist" title="<spring:message code="btn.returnlist"/>"/>
	    <input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
	    <input type="button" value="<spring:message code="btn.save"/>" class="button orange submit" title="<spring:message code="btn.save"/>"/>       
	</div>
	<input type="hidden" id="industryIdhd" value="${industryId}" />
	<div class="ui-block">
	    <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
	    	<table>
	        	<tr>
	                <td><label><spring:message code="product.property.lable.industry"/></label></td>
	                <td><span>${industryName}</span></td>
	                <td><label><spring:message code="product.property.lable.name"/></label></td>
	                <td><span>${propertyName}</span></td>
	            </tr>
	        </table>
	    </div>
    </div>

	<div class="ui-block-title1"><spring:message code="system.property.setvalue"/></div>

	<form name="propertyForm" action="/i18n/property/savePropertyValue.json" method="post">
		<input type="hidden" class="propertyId" name="propertyId" value="${propertyId}" />
		<div id="edittable" loxiaType="edittable" >
			<table id="propertyTable" operation="add,delete" append="1" width="2000" class="inform-person" >
			    <thead>
					<tr>
						<th width="5%"><input type="checkbox"/></th>
						<th width="50%"><spring:message code="system.propertyvalue"/></th>
						<th width="8%"></th>
						<th width=""></th>
					</tr>
				</thead>
			    
			    <tbody>
				    <c:if test="${ propertyValue != '[]' }">
					     <c:forEach var="item" items="${propertyValue}" varStatus="index">
							<tr>
								<td>
									<input type="checkbox"/>
									<input type="hidden" name="propertyValues.id" value="${item.id}"/>
									<input type="hidden" class="propertyValues-propertyId" name="propertyValues.propertyId" value="${item.propertyId}"/>
									<input type="hidden" name="propertyValues.sortNo" value="${ index.count }">
								</td>
								<td class="mutl_lang">
									<c:if test="${i18nOnOff ==true}">
										<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
										<c:set value="${status.index}" var="i"></c:set>
										<div style="position:relative;overflow:hidden;height:30px">
										<input style="position:absolute;top:0;left:0;width:83%;" type="text" class="name" loxiaType="input" trim="true" id="input3" lang = "${i18nLang.key}"  name="propertyValues.value.values[${status.index}-${status.index}]"
										  mandatory="true" value="${item.value.langValues[i18nLang.key]}"/>
										<span style="position:absolute;top:0;left:87%;"  class="lang_lable">${i18nLang.value}</span>
										<input class="i18n-lang" type="text" loxiaType="input" trim="true"  name="propertyValues.value.langs[${status.index}-${status.index}]"  
										 mandatory="true" value="${i18nLang.key}"/>
										 </div>
										</c:forEach>
									</c:if>
									<c:if test="${i18nOnOff ==false}">
										<div style="position:relative;overflow:hidden;height:30px">
										<input style="position:absolute;top:0;left:0;width:83%;" type="text" loxiaType="input" trim="true"  class="name" id="input3" name="propertyValues.value.value[${index.index}]" style="width:95%" mandatory="true" value="${item.value.value}"/>
										</div>
									</c:if>
								</td>
								<td style="text-align: left;">
									<c:if test="${!index.first}">
										<a href="javascript:void(0)" class="arrow_up"><img src="${ base }/images/wmi/blacks/16x16/arrow_top.png" /></a>
									</c:if>
										<a href="javascript:void(0)" class="arrow_down ${ index.first?'first':'' }"><img src="${ base }/images/wmi/blacks/16x16/arrow_bottom.png" /></a>
								</td>
							</tr>
						</c:forEach>
					</c:if>
			    </tbody>
				<tbody> 
						<tr>
							<td><input type="checkbox"/>
								<input type="hidden" name="propertyValues.id" value=""/>
								<input type="hidden" name="propertyValues.propertyId" value=""/>
								<input type="hidden" name="propertyValues.sortNo" value="1"> 
							</td>
							<td class="mutl_lang" style="text-align:left">
								<c:if test="${i18nOnOff ==true}">
								<c:set var="size" value="${fn:length(propertyValue)}"></c:set>
								<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
								<div style="position:relative;overflow:hidden;height:30px">
									<input style="position:absolute;top:0;left:0;width:83%;" type="text" loxiaType="input" class="name" trim="true" id="input3" lang = "${i18nLang.key}" name="propertyValues.value.values[${size}-0]"  mandatory="true" "/>
									<span style="position:absolute;top:0;left:87%;" class="lang_lable">${i18nLang.value}</span>
									<input class="i18n-lang" type="text" loxiaType="input" trim="true" name="propertyValues.value.langs[${size}-0]"  mandatory="true" value="${i18nLang.key}"/></div>
								</c:forEach>
								</c:if>
								<c:if test="${i18nOnOff ==false}">
									<div style="position:relative;overflow:hidden;height:30px">
									<input style="position:absolute;top:0;left:0;width:83%;" type="text" loxiaType="input"  class="name" trim="true" id="input3" name="propertyValues.value.value[${index.index}]" style="width:95%" mandatory="true" value="${item.value.value}"/>
									</div>
								</c:if>
							</td>
						
							<td style="text-align: left;">
								<c:if test="${ propertyValue != '[]'}">
									<a href="javascript:void(0)" class="arrow_up"><img src="${ base }/images/wmi/blacks/16x16/arrow_top.png" /></a>
								</c:if> 
							</td>
						</tr>
				</tbody>
				<tfoot>
					<tr>
						<td colspan="2" >&nbsp;</td><td>
						
						</td>
					</tr>
				</tfoot>
			</table>
			<div class="button-line">
				<input type="hidden" id="propertyId" value="${propertyId}" />
		        <input type="button" value="<spring:message code="btn.save"/>" class="button orange submit" title="<spring:message code="btn.save"/>"/>
		      	<input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
		      	<input type="button" value="<spring:message code="btn.returnlist"/>" class="button returnlist" title="<spring:message code="btn.returnlist"/>"/>        
		   </div>
		</div>
	</form>
</div>
<select id="selectclone" class="gname-select ui-loxia-default ui-corner-all propertyvalueselect" aria-disabled="false" style="display: none; width:85%;" onchange="selectPropertyValue(this);" onblur="validatePropertyValue(this);"> 
	<option value="">请选择</option>
	<c:forEach items="${propertyValueList}" var="propertyValue">
			<option value="${propertyValue.value }" propertyValueId="${propertyValue.id }">${propertyValue.value }</option>
	</c:forEach>								
</select>
<!-- <select id="selectclone" style="display: none;"> 
	<option value="" ></option>
</select> -->
</body>
</html>
