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
<script type="text/javascript" src="${base }/scripts/product/item/addSearchCondition.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<SCRIPT type="text/javascript">	
//<spring:message code="industry.list.expand"/>ROOT

          
var zNodes2 = [
            	{id:0, pId:-1, name:"ROOT", 
             		code:"ROOT", sortNo:1, 
             		open:true, lifecycle:1,nocheck:true},
             	<c:forEach var="navigation" items="${naviList}" varStatus="status">
                 	
                 	{id:${navigation.id}, pId:${navigation.parentId}, name:"${navigation.name}", 
                 		code:"${navigation.id}", sortNo:${navigation.sort}, 
                 		open:false, lifecycle:${navigation.lifecycle}}
                 	<c:if test="${!status.last}">,</c:if>
                 </c:forEach>
            ];
            
var zNodes = [
          	{id:0, pId:-1, name:"ROOT", 
           		code:"ROOT", sortNo:1, 
           		open:true, lifecycle:1},
               <c:forEach var="category" items="${industryList}" varStatus="status">
               	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
               		sortNo:${category.id}, 
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
    <spring:message code='item.searchCodition.codition'/>
    </div>

<div class="ui-tag-change">
<div class="tag-change-content">
            <div class="tag-change-in block">
                    <div class="ui-block">
                    <div class="ui-block-title1"><spring:message code='item.searchCodition.addCodition'/></div>
  <form id="submitForm1" method="post" action="/i18n/item/itemSearchCondition/managerSaveCondition.htm">
  <div class="ui-block-line">
    <label><spring:message code='item.searchCodition.type'/></label>
    <opt:select id="type" name="condition.type" loxiaType="select" defaultValue="1" expression="chooseOption.SEARCH_CONDITION_TYPE"/>
  </div>
  <div id="hiddenDiv">
	  <div class="ui-block-line navi-param" style="display: block;">
	    <label><spring:message code='item.searchCodition.industry'/></label>
	    <input type="text" mandatory="true" checkmaster="checkInput" readOnly="readOnly" value="" id="industry" placeholder="<spring:message code='item.searchCodition.industry'/>" loxiaType="input" aria-disabled="true">
	    <input type="hidden" value="" name="condition.industryId" id="industryId" >
	    <a href="javascript:void(0);" id="addIndustry" class="func-button view-block-item"><spring:message code='item.searchCodition.cIndustry'/></a>
	  </div>
	  <div class="ui-block-line">
	    <label><spring:message code='item.searchCodition.property'/></label>
	    <select id="property" loxiaType="select" mandatory="true" name="condition.propertyId" checkmaster="checkSelect">
	    	<option value=""><spring:message code='item.searchCodition.noSelect'/></option>
	    </select>
	  </div>
  </div>
  <c:if test="${i18nOnOff == true}">
  	<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	   <div class="ui-block-line">
	    <label><spring:message code='item.searchCodition.name'/></label>
	    <input type="text" loxiaType="input" mandatory="true" value="" name="condition.name.values[${status.index}]" id="name" placeholder="<spring:message code='item.searchCodition.name'/>" class="ui-loxia-default ui-corner-all" aria-disabled="false">
	 	<input type="text" class="i18n-lang" loxiaType="input" mandatory="true" value="${i18nLang.key}" name="condition.name.langs[${status.index}]" />
	 	<span>${i18nLang.value}</span>
	  </div>
	  </c:forEach>
  </c:if>
  <c:if test="${i18nOnOff == false}">
	   <div class="ui-block-line">
	    <label><spring:message code='item.searchCodition.name'/></label>
	    <input type="text" loxiaType="input" mandatory="true" value="" name="condition.name.value" id="name" placeholder="<spring:message code='item.searchCodition.name'/>" class="ui-loxia-default ui-corner-all" aria-disabled="false">
	  </div>
  </c:if>
 
  <div class="ui-block-line navi-param" style="display: block;">
    <label>导航</label>
    <input type="text" readOnly="readOnly" value="" id="category" placeholder="导航" loxiaType="input" aria-disabled="true">
    <input type="hidden" value="" name="condition.navigationId" id="categoryId" >
    <a href="javascript:void(0);" id="addType" class="func-button view-block-type">选择导航</a>
  </div>
  
  <div class="ui-block-line">
    <label><spring:message code='item.searchCodition.sort'/></label>
    <input type="text" value="" loxiaType="number" mandatory="true" name="condition.sort" id="name" aria-disabled="false">
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

<div id="dialog-category-select-left" class="proto-dialog" style="OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
	<h5><spring:message code='item.searchCodition.cIndustry'/></h5>
	 <div class="ui-block-content ui-block-content-lb">
                <div class="tree-control">
                    <input type="text" id="key-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
                    <div><span id="search_result_left"></span></div>
                </div>
                <ul id="tree-left" class="ztree"></ul>
      </div>
      <div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>
<div id="dialog-category-select-left2" class="proto-dialog" style="OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
	<h5><spring:message code='item.searchCodition.cCategory'/></h5>
	 <div class="ui-block-content ui-block-content-lb">
                <div class="tree-control">
                    <input type="text" id="key-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
                    <div><span id="search_result_left"></span></div>
                </div>
                <ul id="tree-left2" class="ztree"></ul>
      </div>
      <div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>
<div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
	<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
</div>

</body>
</html>