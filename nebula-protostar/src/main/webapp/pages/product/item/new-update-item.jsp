<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";

</script>
<script type="text/javascript" src="${base}/scripts/product/item/update-item.js"></script>
<SCRIPT type="text/javascript">	
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
<style type="text/css">
.i18n-lang{
display: none;
}
</style>
</head>
<body>

<div class="content-box width-percent100">
    
   <form name="itemForm" action="/i18n/item/saveItem.json" method="post">
    <input type="hidden" id="industryId" name="itemCommand.industryId"  value=""/>
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
    <input type="hidden" name="itemCommand.id" id="itemid" value="${id }"/>
    <input type="hidden" id="propertyIdArray" value="${propertyIdArray }"/>
    <input type="hidden" id="propertyNameArray" value="${propertyNameArray }"/>
    <input type="hidden" id="mustCheckArray" value="${mustCheckArray }"/>
   

	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.update.manage"/></div>
	<div class="ui-block">
		<div id="second" >
		
			<div class="ui-block">
		        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
		            <table>
		                <tr>
		                    <td><label><spring:message code="item.update.selectedIndustry"/></label></td>
		                    <td><span id="chooseIndustry">${industry.name }</span></td>
		                    
		                </tr>
		            </table>
		        </div>
		    </div>
		
		
			<div class="ui-block ">
				
			
				 <div class="ui-block-title1"><spring:message code="item.update.updateItem"/></div>
				 <%-- 基本信息 --%>
			   	 <%@include file="/pages/product/item/update-item-baseInfo.jsp"%>
			   	 <div style="margin-top: 10px"></div>
				 <%-- 商品价格 --%>
			     <%@include file="/pages/product/item/update-item-price.jsp"%>
				 <div style="margin-top: 10px"></div>
				 <%-- 一般属性信息  销售属性信息 --%>
				 <%@include file="/pages/product/item/update-item-propertyInfo.jsp"%>
			     <div style="margin-top: 10px"></div>
			 	 <%-- SEO --%>
			     <%@include file="/pages/product/item/update-item-seo.jsp"%>
			     <div style="margin-top: 10px"></div>
			     <%-- 商品描述 --%>
			     <%@include file="/pages/product/item/update-item-description.jsp"%>
		
			  
				 <%-- button --%>
				 <div class="button-line">
				         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
				         <input type="button" value="<spring:message code='btn.image'/>" class="button orange imageManage" title="<spring:message code='btn.image'/>"/>
				         <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
				 </div>
			
				 <div style="margin-top: 20px"></div>
			</div>
			<input type="hidden" loxiaType="input" name="thumbnailConfig" id="thumbnailConfig" value="${thumbnailConfig[0].optionValue }" />
		</div>
	</div>
   </form>    
</div>

<div id="menuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:auto; height: 100%;"></ul>
</div>
</body>
</html>
