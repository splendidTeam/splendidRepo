<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript" src="${base}/scripts/product/item/add-item-price.js"></script>

<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.update.itemprice" />
</div>

<div class="ui-block-content border-grey">
	<div class="ui-block-line ">
		<label><spring:message code="item.update.salesprice" /></label>
		<div class="sale-price">
			<input decimal='2'
				loxiaType='number' mandatory='true' id="salePrice"
				name="itemCommand.salePrice" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label><spring:message code="item.update.stickerprice" /></label>
		<div class="sale-list">
			<input decimal='2'
				loxiaType='number' id="listPrice" name="itemCommand.listPrice" />
		</div>
	</div>
</div>