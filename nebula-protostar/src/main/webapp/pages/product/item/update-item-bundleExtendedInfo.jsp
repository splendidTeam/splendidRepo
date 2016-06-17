<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<div class="ui-block-title1 ui-block-title" >
	<spring:message code="item.add.bundleExtendedInfo" />
</div>

<div class="ui-block-content border-grey">
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">主卖品</div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMainProduct">
				<li class="main-pro">
					<c:choose>
						<c:when test="${mainElement.styleCode != null}">
							<a class="showpic"><img src=""><span class="dialog-close">X</span></a>
							<p class="title p10 validate-code"><span title="${mainElement.styleCode }">${mainElement.styleCode }</span></p>
						</c:when>
						<c:otherwise>
							<a class="showpic"><img src="${mainElement.bundleItemViewCommands[0].picUrl }"><span class="dialog-close">X</span></a>
							<p class="title p10 validate-code"><span title="${mainElement.itemCode }">${mainElement.itemCode }</span></p>
							<p class="sub-title"><span title="${mainElement.bundleItemViewCommands[0].title }">${mainElement.bundleItemViewCommands[0].title }</span></p>
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
		</div>
	</div>
	<c:choose>
		<c:when test=""></c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">捆绑成员</div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMemberProduct">
				<c:forEach var="element" items="${elements }" varStatus="status">
					<li class="main-pro">
						<a class="showpic"><img src="${element.bundleItemViewCommands[0].picUrl }"><span class="dialog-close">X</span></a>
						<p class="title p10 validate-code"><span title="${element.bundleItemViewCommands[0].itemCode }">${element.bundleItemViewCommands[0].itemCode }</span></p>
						<p class="sub-title"><span title="${element.bundleItemViewCommands[0].title }">${element.bundleItemViewCommands[0].title }</span></p>
					</li>
				</c:forEach>
				<li class="main-pro pro-empty" id="selectStyle">+新成员</li>
			</ul>
			<a class="user-refresh"></a>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">库存设置</div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> 库存数量 <input type="text" name="availableQty" value="${bundleViewCommand.availableQty }" /> </label>
	   		<label class="label-line block pb10"> 是否同步扣减sku库存 <input type="radio" name="syncWithInv" value="1" <c:if test="${bundleViewCommand.syncWithInv == true }">checked="checked"</c:if>>是   <input type="radio" name="syncWithInv" value="0" <c:if test="${bundleViewCommand.syncWithInv == false }">checked="checked"</c:if>>否 </label>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">价格设置</div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="1" <c:if test="${bundleViewCommand.priceType == 1 }">checked="checked"</c:if>> 按捆绑商品总价  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="2" <c:if test="${bundleViewCommand.priceType == 2 }">checked="checked"</c:if>> 一口价（简单）  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="3" <c:if test="${bundleViewCommand.priceType == 3 }">checked="checked"</c:if>> 定制    </label>
	   		<label class="label-line block pb10"> 捆绑商品总价 <input type="text" readonly placeholder="900" value=""> </label>
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
				 

