<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/property/edit-property-value.js"></script>
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

<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/wrench.png"><spring:message code="system.property.manager"/> 【${property.name }】
		</div>
		<input type="hidden" id="propertyId" value="${property.id}" />
		<div class="ui-block ui-block-fleft" style="width: 400px;">
			<div class="ui-block-content ui-block-content-lb">
  				<table >
			        <tr>
			            <td>
			            	<label><spring:message code='shop.property.group'/></label>
			            </td>
			            <td>
			                <span >
			                	<select  loxiaType="select"  mandatory="true" class="orgtype selectedGroupId" > 
			                		<option value=""> ---请选择属性值组---</option>
									<c:forEach items="${propertyValueGroupList}" var="group">
										<option value="${group.id}">${group.name}</option>
									</c:forEach>
								</select>
			                </span>
			            </td>
			            <td>
			                <span ><a href="javascript:void(0);" class="func-button addPropertyValueGroup"   title="<spring:message code='btn.add'/>">
			                		<span><spring:message code="btn.add"/></span></a>
			                </span>
			            </td>
			            <td>
			                <span ><a href="javascript:void(0);" class="func-button updatePropertyValueGroup"   title="<spring:message code='btn.update'/>">
			                		<span><spring:message code="btn.update"/></span></a>
			                </span>
			            </td>
			        </tr>
			    </table>
			</div>
			<div class="table-border0 border-grey" id="table2" caption="会员分组列表"></div>
		</div>
		 
		<div class="ui-block " style="margin-left:400px;width:auto;padding-left: 10px;" >
			<div class="ui-block-title1">属性值操作</div>
			<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
				<div class="ui-block-line">
					<label>属性值：</label>
					<div>
						<form name="propertyForm" action="/i18n/property/addOrUpdatePropertyValue.json" method="POST">
							<input type="hidden" name="propertyId" id="propertyId" value="${property.id}" />
						
							<input type="hidden" name="propertyValues.id" value=""/>
							<input type="hidden" name="propertyValues.propertyId" value="${property.id}" class="propertyValues-propertyId" />
							<input type="hidden" name="propertyValues.sortNo" value="">
							
						 <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
						   <div class="ui-block-line ">
						   
	<%-- 				         <input id="title" name="itemCommand.title.values[${status.index}]" 
					          value="" type="text"  style='width: 200px' loxiaType="input" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
					         
					         <input class="i18n-lang" type="text" name="itemCommand.title.langs[${status.index}]" value="${i18nLang.key}" />
					         <span>${i18nLang.value}</span> --%>
					         
							<input id="input3" 
								name="propertyValues.value.values[${status.index}]" placeholder="<spring:message code='system.propertyvalue'/>"
							  value=""  style='width: 200px' mandatory="true" type="text" class="name" loxiaType="input" trim="true"/>
							
							<input name="propertyValues.value.langs[${status.index}]"  value="${i18nLang.key}" 
							 	mandatory="true" class="i18n-lang" type="text" loxiaType="input" trim="true" />
						   
						   
						   	<span>${i18nLang.value}</span>		 
						  
						   </div>
						   
						   
						 </c:forEach>
						</form>
					</div>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button savePropertyValue" title="<spring:message code='btn.save'/>">
    				 		<span><spring:message code="btn.save"/></span>
    				</a>
    				<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.save.continue'/>">
    				 		<span><spring:message code="shop.property.value.save.continue"/></span>
    				</a> 		
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.export'/>">
    				 		<span><spring:message code="shop.property.value.export"/></span></a>
    				<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.import'/>">
    				 		<span><spring:message code="shop.property.value.import"/></span></a>
				</div>
				
			</div>
		</div>
	</div>

</body>
</html>
