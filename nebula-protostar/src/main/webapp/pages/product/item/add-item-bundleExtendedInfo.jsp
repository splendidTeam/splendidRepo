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
		<div class="ui-block-title1 ui-block-title">主卖品</div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMainProduct">
				<li class="main-pro pro-empty" id="selectPro">设置主卖品</li>
			</ul>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">捆绑成员</div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMemberProduct">
				<li class="main-pro pro-empty" id="selectStyle">+新成员</li>
			</ul>
			<a class="user-refresh"></a>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">库存设置</div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> 是否同步扣减sku库存 <input type="radio" name="syncWithInv" value="1" checked="checked">是   <input type="radio" name="syncWithInv" value="0" >否 </label>
	   		<label class="label-line block pb10"> 库存数量 <input type="text" name="availableQty" placeholder=""> </label>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">价格设置</div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="1" checked="checked"> 按捆绑商品总价  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="2"> 一口价（简单）  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="3"> 定制    </label>
	   		<label class="label-line block pb10"> 捆绑商品总价 <input type="text" readonly placeholder="900"> </label>
		</div>
	   	<table class="inform-person product-table" style="display:none">
			<thead>
				<tr>
					<th width="10%">成员序号</th>
					<th width="25%">商品</th>
					<th width="25%">原销售价 </th>
					<th width="15%">现售价</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
		<table class="inform-person sku-table">
			<thead>
				<tr>
					<th width="10%">成员序号</th>
					<th width="25%">销售单元</th>
					<th width="25%">是否参与 </th>
					<th width="25%">原销售价</th>
					<th width="15%">现售价</th>
				</tr>
			</thead>
			<tbody></tbody>
		</table>
	</div>
</div>					 
				 

