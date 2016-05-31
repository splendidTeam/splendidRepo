<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/product/item/update-item-common.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/item/update-item-normal.js"></script>
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
		        <div class="ui-block-content ui-block-content-lb ui-subtitle">
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
			   	 <div class="mt10"></div>
				 <%-- 商品价格 --%>
			     <%@include file="/pages/product/item/update-item-price.jsp"%>
				 <div class="mt10"></div>
				 <%-- 一般属性信息  销售属性信息 --%>
				 <%@include file="/pages/product/item/update-item-property.jsp"%>
			     <div class="mt10"></div>
			 	 <%-- SEO --%>
			     <%@include file="/pages/product/item/update-item-seo.jsp"%>
			     <div class="mt10"></div>
			     <%-- 商品描述 --%>
			     <%@include file="/pages/product/item/update-item-description.jsp"%>
		
			  
				 <%-- button --%>
				 <div class="button-line">
				         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
				         <input type="button" value="<spring:message code='btn.image'/>" class="button orange imageManage" title="<spring:message code='btn.image'/>"/>
				         <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
				 </div>
			
				 <div class="mt20"></div>
			</div>
			<input type="hidden" loxiaType="input" name="thumbnailConfig" id="thumbnailConfig" value="${thumbnailConfig[0].optionValue }" />
		</div>
	</div>
   </form>    
</div>

<div id="menuContent" class="menuContent menulayer">
	<ul id="treeDemo" class="ztree"></ul>
</div>
</body>
</html>
