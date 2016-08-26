<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/product/item/add-item-common.jsp"%>
<link rel="stylesheet" type="text/css" href="${base}/scripts/sortable/css/sortable-theme-bootstrap.css"/>
<script type="text/javascript" src="${base}/scripts/sortable/js/sortable.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-bundle.js"></script>
</head>
<body>

<div class="content-box width-percent100">
   <form id="itemForm" name="itemForm" action="/i18n/item/saveBundleItem.json" method="post">
    <input type="hidden" id="industryId" name="itemCommand.industryId"  value="${industry.id }"/>
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
    <input type="hidden" loxiaType="input" name="baseImageUrl" id="baseImageUrl" value="${baseImageUrl}" />
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="second">
				<div class="ui-block">
			        <div class="ui-block-content ui-block-content-lb ui-subtitle">
			            <table>
			                <tr>
			                    <%-- <td><label><spring:message code="item.update.selectedIndustry"/></label></td>
			                    <td><span id="chooseIndustry">${industry.name }</span></td> --%>
			                    
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
								<label><spring:message code="member.group.type" /></label>
								<input type="radio" name="selectType" value="product" checked="checked" /><spring:message code="item.common.product" />
								<input type="radio" name="selectType" value="style" <c:if test="${isEnableStyle == false }">disable="disable"</c:if> /><spring:message code="item.common.style" />
							</div> 
							<div class="form-group p10">
								<label><spring:message code="product.property.lable.code" /></label>
								<input type="text" loxiaType="input" name="q_sl_code" mandatory="false"/>
							</div>
							<div class="button-line1 right">
								<a href="javascript:void(0);" class="func-button orange" id="search_button"><span><spring:message code="btn.search" /></span></a>
							</div>
						</form>
					</div>
				</div>
				<div class="ui-block">
					<div class="border-grey" id="selectProList_product" caption="<spring:message code='item.list.itemList' /> "></div>
				</div>
			</div>
		</div>
		<div class="proto-dialog-button-line right">
			<input type="button" value="<spring:message code='btn.confirm' />" class="button orange" id="addMainProduct"/>
		</div>
	</div>
</div>

<div id="menuContent" class="menuContent menulayer">
	<ul id="treeDemo" class="ztree"></ul>
</div>
<div id="defaultMenuContent" class="menuContent menulayer">
	<ul id="defaultCategoryTree" class="ztree"></ul>
</div>

<div style="display:none">
	<form id="bundle_element_form" name="bundle_element_form" method="POST"></form>
</div>

</body>
</html>
