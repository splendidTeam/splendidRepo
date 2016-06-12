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
   <!-- 选择商品弹出层 -->
   <div class="select-pro-layer proto-dialog">
		<h5 id="bundle_dialog_title"></h5>
		<div class="proto-dialog-content">
			<div class="ui-block">
				<div class="ui-block">
					<div class="ui-block-content ui-block-content-lb">
						<form id="mainItemDialogSearchForm">
							<div class="form-group p10">
								<label>类型</label>
								<input type="radio" name="selectType" value="product" checked="checked" />商品
								<input type="radio" name="selectType" value="style" <c:if test="${isEnableStyle == false }">disable="disable"</c:if> />款
							</div> 
							<div class="form-group p10">
								<label>编码</label>
								<input type="text" loxiaType="input" name="q_sl_code" mandatory="false"/>
							</div>
							<div class="button-line1 right">
								<a href="javascript:void(0);" class="func-button orange" id="search_button"><span>搜索</span></a>
							</div>
						</form>
					</div>
				</div>
				<div class="ui-block">
					<div class="border-grey" id="selectProList_product" caption="商品列表 "></div>
				</div>
			</div>
		</div>
		<div class="proto-dialog-button-line right">
			<input type="button" value="确定" class="button orange" id="addMainProduct"/>
		</div>
	</div>
</div>

<div id="menuContent" class="menuContent menulayer">
	<ul id="treeDemo" class="ztree"></ul>
</div>
<div id="defaultMenuContent" class="menuContent menulayer">
	<ul id="defaultCategoryTree" class="ztree"></ul>
</div>
</body>
</html>
