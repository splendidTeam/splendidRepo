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
<script type="text/javascript" src="${base }/scripts/product/item/searchCondition.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<SCRIPT type="text/javascript">	
//<spring:message code="industry.list.expand"/>ROOT
var zNodes = [
          	{id:0, pId:-1, name:"ROOT", 
           		code:"ROOT", sortNo:1, 
           		open:true, lifecycle:1,nocheck:true},
               <c:forEach var="category" items="${categoryList}" varStatus="status">
               	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
               		code:"${category.code}", sortNo:${category.sortNo}, 
               		open:false, lifecycle:${category.lifecycle}}
               	<c:if test="${!status.last}">,</c:if>
               </c:forEach>
          ];
          
var zNodes2 = [
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
</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
    <spring:message code='item.searchCodition.codition'/> 
    <input type="button" value="<spring:message code='item.searchCodition.add'/>" class="button orange addCondition" title="<spring:message code='item.searchCodition.add'/>"/>
    <input type="button" id="deleteAll" value="<spring:message code='item.searchCodition.deleteAll'/>" class="button orange deleteCondition" title="<spring:message code='item.searchCodition.deleteAll'/>"/>
    </div>  
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
	    <table > 
	        <tr>
	        	<td><label><spring:message code='item.searchCodition.name'/></label></td>
	            <td><input type="text" id="name" placeHolder="<spring:message code='item.searchCodition.name'/>" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>  
	                  
			   <td><label>分类</label></td>
	             <td> 
	             	<input type="text" id="category" name="categoryN" readOnly="readOnly" placeHolder="<spring:message code='item.searchCodition.category'/>" loxiaType="input" mandatory="false"></input>
	             	<input type="hidden" id="categoryId" name="q_long_categoryId" loxiaType="input" />
	             </td>
	            <td><label><spring:message code='item.searchCodition.industry'/></label></td>
	            <td>
	            	<input type="text" id="industry" name="industryN" readOnly="readOnly" placeHolder="<spring:message code='item.searchCodition.industry'/>" loxiaType="input" mandatory="false">
	            	<input type="hidden" id="industryId" name="q_long_industryId" loxiaType="input" />
	            </td>  
	            <td><label><spring:message code='item.searchCodition.property'/></label></td>
	            <td>
	            	<input type="hidden" id="propertyId_" name="propertyId_" />
	            	<select loxiaType="select" mandatory="false" id="property" name="q_long_propertyId">
	            		<option value=''><spring:message code='item.searchCodition.noSelect'/></option>
	            	</select>
	            </td>
	        </tr>
	 
	    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code='item.searchCodition.search'/>"><spring:message code='item.searchCodition.search'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code='item.searchCodition.coditionList'/>"></div>   
    </div>
</div>

<div id="view-block-item" class="proto-dialog">
	<h5><spring:message code='item.searchCodition.cIndustry'/></h5>
	<div class="proto-dialog-content p10">
		
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>


<div id="categoryMenuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
</div>
<div id="menuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
	<ul id="treeDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
</div>
<div id="menuContent2" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
	<ul id="tree-left2" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
</div>

</body>

</body>
</html>
