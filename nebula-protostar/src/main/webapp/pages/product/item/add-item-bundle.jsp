<<<<<<< HEAD
<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/product/item/add-item-common.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-bundle.js"></script>
</head>
<body>

<div class="content-box width-percent100">
   <form id="itemForm" name="itemForm" action="/i18n/item/saveBundleItem.json" method="post">
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="second">
				<div class="ui-block ">
					 <div class="ui-block-title1"><spring:message code="item.add.addItem"/></div>
				     <%-- 基本信息 --%>
				     <%@include file="/pages/product/item/add-item-baseInfo.jsp"%>
					 <div class="mt10"></div>
					 <%-- 商品价格 --%>
					 <%@include file="/pages/product/item/add-item-price.jsp"%>
				   	 <div class="mt10"></div>
				   	 <%-- bundle扩展信息 --%>
					 <%@include file="/pages/product/item/add-item-bundleExtendedInfo.jsp"%>
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
=======
<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/product/item/add-item-common.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-bundle.js"></script>
</head>
<body>

<div class="content-box width-percent100">
   <form id="itemForm" name="itemForm" action="/i18n/item/saveBundleItem.json" method="post">
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="second">
				<div class="ui-block ">
					 <div class="ui-block-title1"><spring:message code="item.add.addItem"/></div>
				     <%-- 基本信息 --%>
				     <%@include file="/pages/product/item/add-item-baseInfo.jsp"%>
					 <div class="mt10"></div>
					 <%-- 商品价格 --%>
					 <%@include file="/pages/product/item/add-item-price.jsp"%>
				   	 <div class="mt10"></div>
				   	 <%-- bundle扩展信息 --%>
					 <%@include file="/pages/product/item/add-item-bundleExtendedInfo.jsp"%>
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
>>>>>>> branch 'master' of http://git.baozun.cn/nebula/nebula.git
