<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/product/item/add-item-common.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-normal.js"></script>
</head>
<body>

<div class="content-box width-percent100">
    
   <form id="itemForm" name="itemForm" action="/i18n/item/saveItem.json" method="post">
    <input type="hidden" id="industryId" name="itemCommand.industryId"  value=""/>
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="first">
				<div class="ui-block border-grey">
				   <div class="ui-block-title1"><spring:message code="item.add.industry"/></div>
				   <div class="ui-block ui-block-fleft w240">
				        <div class="ui-block-content ui-block-content-lb">
				            <div class="tree-control">
				                <input type="text" id="key" loxiatype="input" placeholder="<spring:message code='item.add.keyword'/>" />
				                <div><span id="search_result"></span></div>
				            </div>
				            <ul id="industrytreeDemo" class="ztree"></ul>
				        </div>
			      </div>
				  <div class="button-line">
			         <input type="button" value="<spring:message code='system.property.next'/>" class="button orange next" title="<spring:message code='system.property.next'/>"/>
			         <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
			         
			      </div>
				</div>
			</div>
			<div id="second"  class="none-normal">
				<div class="ui-block">
			        <div class="ui-block-content ui-block-content-lb pb10">
			            <table>
			                <tr>
			                    <td><label><spring:message code="item.add.selectIndustry"/></label></td>
			                    <td><span id="chooseIndustry"></span></td>
			                    
			                </tr>
			            </table>
			        </div>
			    </div>
				<div class="ui-block ">
					 <div class="ui-block-title1"><spring:message code="item.add.addItem"/></div>
				     <%-- 基本信息 --%>
				     <%@include file="/pages/product/item/add-item-baseInfo.jsp"%>
					 <div class="mt10"></div>
					 <%-- 商品价格 --%>
					 <%@include file="/pages/product/item/add-item-price.jsp"%>
				   	 <div class="mt10"></div>
				   	 <%-- 一般属性信息 销售属性信息 --%>
					 <%@include file="/pages/product/item/add-item-property.jsp"%>
				     <div class="mt10"></div>
					 <%-- SEO --%>
				     <%@include file="/pages/product/item/add-item-seo.jsp"%>
				     <div class="mt10"></div>
				     <%-- 商品描述 --%>
					 <%@include file="/pages/product/item/add-item-description.jsp"%>
				     <div class="mt10"></div>
					 <div class="button-line">
				         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
				         <input type="button" value="<spring:message code='item.add.previous'/>" class="button back"  title="<spring:message code='item.add.previous'/>" />
					</div>
					<div class="mt20"></div>
				</div>
			</div>
		</div>
   </form>
</div>

<div id="menuContent" class="menuContent menulayer">
	<ul id="treeDemo" class="ztree"></ul>
</div>
<div id="defaultMenuContent" class="menuContent menulayer">
	<ul id="defaultCategoryTree" class="ztree"></ul>
</div>
</body>
</html>
