<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.update.itemprice" />
</div>
<div class="ui-block-content border-grey">
	<div class="ui-block-line ">
		<label><spring:message code="item.update.salesprice" /></label>
		<div>
			<input style="width: 160px; height: 25px" decimal='2'
				loxiaType='number' mandatory='true' id="salePrice"
				name="itemCommand.salePrice" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label><spring:message code="item.update.stickerprice" /></label>
		<div>
			<input style="width: 160px; height: 25px" decimal='2'
				loxiaType='number' id="listPrice" name="itemCommand.listPrice" />
		</div>
	</div>
</div>