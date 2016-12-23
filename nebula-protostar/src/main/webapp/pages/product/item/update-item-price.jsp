<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${base}/scripts/product/item/update-item-price.js"></script>

<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.update.itemprice" />
</div>
<div class="ui-block-content border-grey">
	<div class="ui-block-line">
		<label><spring:message code="item.update.salesprice" /></label> <input
			id="salePriceValue" type="hidden" value="${ salePrice }" />
		<div id="salePriceDiv" class="sale-price">
			<input mandatory='true' loxiaType='number' decimal='2' id="salePrice" name="itemCommand.salePrice" value="${ salePrice }" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label><spring:message code="item.update.stickerprice" /></label> <input
			id="listPriceValue" type="hidden" value="${ listPrice }" />
		<div id="listPriceDiv" class="sale-list">
			<input loxiaType='number' decimal='2' id="listPrice"
				name="itemCommand.listPrice" value="${listPrice}" />
		</div>
	</div>
</div>