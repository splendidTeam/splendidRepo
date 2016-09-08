<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript">
var categoryDisplayMode = "${categoryDisplayMode}";
</script>

<div class="ui-block-title1 ui-block-title" >
	<spring:message code="item.add.bundleExtendedInfo" />
</div>

<div class="ui-block-content border-grey">
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.mainProduct" /></div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMainProduct">
				<li class="main-pro pro-empty" id="selectPro"><spring:message code="item.common.addMainProduct" /></li>
			</ul>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.memberProduct" /></div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix">
				<div class="setMemberProduct"></div>
				<li class="main-pro pro-empty" id="selectStyle"><spring:message code="item.common.addMemberProduct" /></li>
			</ul>
			<a class="user-refresh" id="refresh-table"><spring:message code="refresh" /></a>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.setRepertory" /></div>
		<div class="ui-block-content border-grey">
			<label class="label-line block pb10"> <spring:message code="item.common.repertoryNumber" /> <input type="text" name="availableQty" placeholder=""> </label>
	   		<label class="label-line block pb10"> <spring:message code="item.common.isSynchronization" /> <input type="radio" name="syncWithInv" value="1" checked="checked"><spring:message code="shop.property.true" />   <input type="radio" name="syncWithInv" value="0" ><spring:message code="shop.property.false" /> </label>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.setPrice" /></div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="1" checked="checked"> <spring:message code="item.common.onBundleTotalPrice" />  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="2"> <spring:message code="item.common.fixPrice" />  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="3"> <spring:message code="item.common.customPrice" />     </label>
	   		<!-- <label class="label-line block pb10"> <spring:message code="item.common.bundleTotalPrice" /> <input type="text" readonly placeholder="900"> </label> -->
		</div>
	   	<table class="inform-person product-table" style="display:none">
			<thead>
				<tr>
					<th width="10%"><spring:message code="item.common.memberId" /></th>
					<th width="25%"><spring:message code="item.common.product" /></th>
					<th width="25%"><spring:message code="item.common.oldSalePrice" /></th>
					<th width="15%"><spring:message code="item.common.newSalePrice" /></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		<table class="inform-person sku-table">
			<thead>
				<tr>
					<th width="10%"><spring:message code="item.common.memberId" /></th>
					<th width="25%"><spring:message code="item.common.saleElement" /></th>
					<th width="25%"><spring:message code="item.common.isJoin" /></th>
					<th width="25%"><spring:message code="item.common.oldSalePrice" /></th>
					<th width="15%"><spring:message code="item.common.newSalePrice" /></th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</div>					 
				 

