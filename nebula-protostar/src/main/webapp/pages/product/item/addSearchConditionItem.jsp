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
<script type="text/javascript" src="${base }/scripts/product/item/addSearchConditionItem.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<SCRIPT type="text/javascript">	
//<spring:message code="industry.list.expand"/>ROOT
var category_ZNodes = [
		{id:0, pId:-1, name:"ROOT",
			  code:"ROOT", sortNo:1,
			  open:true, lifecycle:1},  
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	
              	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		open:false, lifecycle:${category.lifecycle}}
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];
</SCRIPT>
<style type="text/css">
.i18n-lang{
display: none;
}
</style>
</head>
<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
    <spring:message code='item.searchCodition.coditionItem'/>
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
<br/>


<div class="ui-tag-change">
<div class="tag-change-content">
            <div class="tag-change-in block">
                    <div class="ui-block">
                    <div class="ui-block-title1"><spring:message code='item.searchCodition.addCoditionItem'/></div>
<form id="submitForm1" method="post" action="/i18n/item/itemSearchCondition/managerSaveConditionItem.htm">
<input id="pid" type="hidden" value="${searchConditionVo.id}" name="pid" />
  <c:choose>
  	<c:when test="${searchConditionVo.type==1}">
		<input id="propertyId" type="hidden" value="${searchConditionVo.propertyId}" name="searchConditionItem.propertyId" />
  		<div class="ui-block-line navi-param" style="display: block;">
	    	<label><spring:message code='item.searchCodition.cPropertyValue'/></label>
	    	<select id="propertyValueId" name="searchConditionItem.propertyValueId" loxiaType="select" mandatory="true" checkmaster="checkSelect"/>
	    		<option value=""><spring:message code='item.searchCodition.noSelect'/></option>
	    		<c:forEach var="propertyValue" items="${propertyValueList}">
	    			<option value="${propertyValue.id}">${propertyValue.value}</option>
	    		</c:forEach>
	    	</select>
	  	</div>
  	</c:when>
  	<c:otherwise>  	
  		<input id="coditionId" type="hidden" value="${searchConditionVo.id}" name="searchConditionItem.coditionId" />
  		<div class="ui-block-line">
	    	<label><spring:message code='item.searchCodition.min'/></label>
	    	<input type="text" checkmaster="minCheck" loxiaType="number" name="searchConditionItem.areaMin" id="areaMin" placeholder="<spring:message code='item.searchCodition.min'/>" mandatory="true">
	  	</div>
	  	<div class="ui-block-line">
	    	<label><spring:message code='item.searchCodition.max'/></label>
	    	<input type="text" checkmaster="maxCheck" loxiaType="number" name="searchConditionItem.areaMax" id="areaMax" placeholder="<spring:message code='item.searchCodition.max'/>" mandatory="true">
	  	</div>
  	</c:otherwise>
  </c:choose>
  
  <c:if test="${i18nOnOff==true }">
  <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	  <div class="ui-block-line">
	    <label><spring:message code='item.searchCodition.itemName'/></label>
	    <input type="text" loxiaType="input" name="searchConditionItem.name.values[${status.index}]" id="name" placeholder="<spring:message code='item.searchCodition.itemName'/>" mandatory="true">
	  	<input type="text"  class="i18n-lang" loxiaType="input" name="searchConditionItem.name.langs[${status.index}]" value="${i18nLang.key}">
	  	<span>${i18nLang.value}</span>
	  </div>
	</c:forEach>
  </c:if>
   <c:if test="${i18nOnOff==false }">
	   <div class="ui-block-line">
	    <label><spring:message code='item.searchCodition.itemName'/></label>
	    <input type="text" loxiaType="input" name="searchConditionItem.name.value" id="name" placeholder="<spring:message code='item.searchCodition.itemName'/>" mandatory="true">
	  </div>
  </c:if>
  
  <div class="ui-block-line">
    <label><spring:message code='item.searchCodition.sort'/></label>
    <input type="text" loxiaType="number" name="searchConditionItem.sort" id="sort" placeholder="<spring:message code='item.searchCodition.sort'/>" mandatory="true">
  </div>
  
  <div class="button-line">
  		<input type="button" id="submitButton" title="<spring:message code='item.searchCodition.submit'/>" value="<spring:message code='item.searchCodition.submit'/>" class="button orange submit"> 
		<input type="button" id="canel" title="<spring:message code='item.searchCodition.back'/>" value="<spring:message code='item.searchCodition.back'/>" class="button goBackBtn"> 
  </div>
  
</form>
</div>
</div>
</div>
</div>

</div>
</body>
</html>