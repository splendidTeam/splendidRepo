<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="industry.list.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/dragarraylist.js"></script>
<script type="text/javascript" src="${base}/scripts/product/property/nebula-common-property.js"></script>
<script type="text/javascript" src="${base}/scripts/product/property/nebula-update-property.js"></script>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script src="${base}/scripts/layer/layer.js"></script>

</head>
<body>
<div class="content-box">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/spechbubble.png"><spring:message code="system.property.manager"/>
	</div>
    <div class="ui-block" style="padding-left: 10px;">
	   <div class="width-percent100">
    		<form name="userForm" action="" method="post">
    			<input name="propertyId" type="hidden" value="${property.id }" id="propertyId"/>
    			<input name="lifecycle" type="hidden" value="${property.lifecycle }" id="lifecycle"/>
				<div class="ui-block-title1"><spring:message code="shop.property.modify"/></div>
				<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
			        <c:if test="${i18nOnOff == true}">
				    	<c:forEach items="${propertyLangs}" var="i18nLang">
						    <div class="ui-block-line">
						         <label><spring:message code="shop.property.name"/></label>
						         <div class="wl-right">
						              <input name="name" placeholder="<spring:message code="shop.property.name"/>" type="text" loxiaType="input" id="name" class="mutl-lang-name ui-loxia-default ui-corner-all" value="${i18nLang.name }" lang="${i18nLang.lang}" mandatory="true" size="50"/>
							          <span>
							          	<c:forEach items="${i18nLangs}" var="lang">
							          		<c:if test="${i18nLang.lang eq lang.key }">
							          			${lang.value}
							          		</c:if>
							          	</c:forEach>
							          </span>
						         </div>
						    </div>
					    </c:forEach>
				    </c:if>
				    <c:if test="${i18nOnOff == false}">
					     <div class="ui-block-line">
					         <label><spring:message code="shop.property.showname"/></label>
					         <div class="wl-right">
					              <input name="name" placeholder="<spring:message code="shop.property.name"/>" type="text" loxiaType="input" id="name" class="ui-loxia-default ui-corner-all" lang="zh_CN" value="${property.name }" mandatory="true" size="50" "/>
					         </div>
					    </div>
					</c:if>
					         
				    <div class="ui-block-line">
				         <label><spring:message code="shop.property.edit.type"/></label>
				         <div class="wl-right">
				          <opt:select name="editingType" id="editingType"  expression="chooseOption.EDITING_TYPE" defaultValue="${property.editingType }" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
						</div>
				    </div>
					
					 
				    <div class="ui-block-line">
				         <label><spring:message code="shop.property.valuetype"/></label>
				         <div class="wl-right">
				            <opt:select name="valueType" id="valueType" expression="chooseOption.VALUE_TYPE" defaultValue="${property.valueType }" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
				         </div>
				    </div>
				    
				    <div class="ui-block-line">
				         <label><spring:message code="shop.property.issale"/></label>
				         <div class="wl-right">
				         	    		<opt:select name="isSaleProp"  id="isSaleProp"  expression="chooseOption.TRUE_OR_FALSE" defaultValue="${property.isSaleProp }" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
				         </div>
				    </div>
					
					<div class="ui-block-line">
				         <label><spring:message code="shop.property.iscolor"/></label>
				         <div class="wl-right">		
				    			<opt:select name="isColorProp" id="isColorProp" expression="chooseOption.TRUE_OR_FALSE" defaultValue="${property.isColorProp }" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
				    			</select>
				         </div>
				    </div>
					
					<div class="ui-block-line">
				         <label><spring:message code="shop.property.isoutput.necessary"/></label>
				         <div class="wl-right">
				            <opt:select name="required" id="required" expression="chooseOption.TRUE_OR_FALSE" defaultValue="${property.required }" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
				         </div>
				    </div>
					
					<div class="ui-block-line">
				         <label><spring:message code="shop.property.issearch"/></label>
				         <div class="wl-right">
				           <opt:select name="searchable" id="searchable" expression="chooseOption.TRUE_OR_FALSE" defaultValue="${property.searchable }" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
			
				         </div>
				    </div>
					
				    <c:if test="${i18nOnOff == true}">
				    	<c:forEach items="${propertyLangs}" var="i18nLang">
						    <div class="ui-block-line">
						         <label><spring:message code="shop.property.groupname"/></label>
						         <div class="wl-right">
						            <input name="groupName" placeholder="<spring:message code="shop.property.groupname"/>" type="text" loxiaType="input" id="groupName" class="mutl-lang-groupName" value="${i18nLang.groupName }" lang="${i18nLang.lang}" mandatory="true" size="50"/>
							     	<span><c:forEach items="${i18nLangs}" var="lang"><c:if test="${i18nLang.lang eq lang.key }">${lang.value}</c:if></c:forEach></span>
						         </div>
						    </div>
					    </c:forEach>
				    </c:if>
				    <c:if test="${i18nOnOff == false}">
					    <div class="ui-block-line">
					         <label><spring:message code="shop.property.groupname" /></label>
					         <div class="wl-right">
					             <input name="groupName" id="groupName" type="text" loxiaType="input" value="${property.groupName }"  size="50" placeholder="<spring:message code="shop.property.lable.groupname" />"/>
					         </div>
					    </div>
				    </c:if>
			    </div>
	    	</form>
	   </div>
    </div>

	
    <div class="button-line">
        <input type="button" value="<spring:message code="system.property.update"/>" class="button orange add" <c:if test="${property.lifecycle == 1}">style="display:none"</c:if> title="<spring:message code="system.property.update"/>"/>
       	<input type="button" value="<spring:message code="shop.update.update"/>" class="button orange add" <c:if test="${property.lifecycle != 1}">style="display:none"</c:if> title="<spring:message code="shop.update.update"/>"/>
        <input type="button" value="<spring:message code="btn.disable"/>" class="button orange add" <c:if test="${property.lifecycle != 1}">style="display:none;"</c:if> title="<spring:message code="btn.disable"/>"/>
        <input type="button" value="<spring:message code="btn.return"/>" class="button return_button" title="<spring:message code="btn.return"/>"/>
        <c:if test="${property.editingType ==2 || property.editingType ==3 || property.editingType ==4}">
        	<input type="button" value="<spring:message code="system.property.setvalue"/>" class="button" id="propertyValue_button" title="<spring:message code="system.property.setvalue"/>"/>
        </c:if>
    </div>
    
</div>
</div>
</body>
</html>
