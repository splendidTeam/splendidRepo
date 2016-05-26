<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.update.itemprice" />
</div>
<div class="ui-block-content border-grey">
	<div class="ui-block-line">
		<label><spring:message code="item.update.salesprice" /></label> <input
			id="salePriceValue" type="hidden" value="${ salePrice }" />
		<div id="salePriceDiv">
			<input mandatory='true' loxiaType='number' decimal='2'
				style="width: 160px; height: 25px" id="salePrice"
				name="itemCommand.salePrice" value="${ salePrice }" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label><spring:message code="item.update.stickerprice" /></label> <input
			id="listPriceValue" type="hidden" value="${ listPrice }" />
		<div id="listPriceDiv">
			<input loxiaType='number' decimal='2'
				style="width: 160px; height: 25px" id="listPrice"
				name="itemCommand.listPrice" value="${listPrice}" />
		</div>
	</div>
</div>