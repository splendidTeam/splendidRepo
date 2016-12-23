<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/update-item-global.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";
var pdValidCode = "${pdValidCode}";
var categoryzNodes  = [
				{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	{id:${category.id}, pId:${category.parentId}, 
              		name:"${category.name}",
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		<c:forEach var="checkedCategory" items="${categories}">
              		       <c:if test="${checkedCategory.id ==category.id}">
              		            checked:true,
              		       </c:if>
              		</c:forEach>
              		drag:false, open:true,
              		lifecycle:${category.lifecycle} } 
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];

var skuList=<c:out value="${skuList}" default="[]" escapeXml="false" ></c:out>;
var dynamicPropertyCommandListJsonStr=<c:out value="${dynamicPropertyCommandListJsonStr}" default="[]" escapeXml="false"></c:out>;
var lastSelectPropertyId=<c:out value="${lastSelectPropertyId}" default="[]" escapeXml="false"></c:out>;
var lastSelectPropertyValueId=<c:out value="${lastSelectPropertyValueId}" default="[]" escapeXml="false"></c:out>;
var itemPropertiesStr = <c:out value="${itemPropertiesStr}" default = "[]" escapeXml="false"></c:out>;
</SCRIPT>