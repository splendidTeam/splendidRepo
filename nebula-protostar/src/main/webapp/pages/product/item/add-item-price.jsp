<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript" src="${base}/scripts/product/item/add-item-price.js"></script>

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