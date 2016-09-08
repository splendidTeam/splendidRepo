<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<div class="ui-block-title1 ui-block-title" >
	<spring:message code="item.add.bundleExtendedInfo" />
</div>

<div class="ui-block-content border-grey">
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.mainProduct" /></div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMainProduct">
				<li class="main-pro">
					<c:choose>
						<c:when test="${mainElement.styleCode != null}">
							<a class="showpic"><img src=""><span class="dialog-close">X</span></a>
							<p class="title p10 validate-code" data-type="style"><span title="${mainElement.styleCode }">${mainElement.styleCode }</span></p>
						</c:when>
						<c:otherwise>
							<a class="showpic"><img src="${mainElement.bundleItemViewCommands[0].picUrl }"><span class="dialog-close">X</span></a>
							<p class="title p10 validate-code" data-type="product"><span title="${mainElement.itemCode }">${mainElement.itemCode }</span></p>
							<p class="sub-title"><span title="${mainElement.bundleItemViewCommands[0].title }">${mainElement.bundleItemViewCommands[0].title }</span></p>
						</c:otherwise>
					</c:choose>
				</li>
				<li class="main-pro pro-empty" id="selectPro" style="display:none"><spring:message code="item.common.setMainProduct" /></li>
			</ul>
		</div>
	</div>
	<c:choose>
		<c:when test=""></c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.memberProduct" /></div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix">
			 	<div class="setMemberProduct">
					<c:forEach var="element" items="${elements }" varStatus="status">
						<li class="main-pro">
						<c:choose>
							<c:when test="${element.styleCode != null}">
								<a class="showpic"><img src=""><span class="dialog-close">X</span></a>
								<p class="title p10 validate-code" data-type="style"><span title="${element.styleCode }">${element.styleCode }</span></p>
							</c:when>
							<c:otherwise>
								<a class="showpic"><img src="${element.bundleItemViewCommands[0].picUrl }"><span class="dialog-close">X</span></a>
								<p class="title p10 validate-code" data-type="product"><span title="${element.itemCode }">${element.itemCode }</span></p>
								<p class="sub-title"><span title="${element.bundleItemViewCommands[0].title }">${element.bundleItemViewCommands[0].title }</span></p>
							</c:otherwise>
						</c:choose>
						</li>
					</c:forEach>
				</div>
				<li class="main-pro pro-empty" id="selectStyle"><spring:message code="item.common.addMemberProduct" /></li>
			</ul>
			<a class="user-refresh" id="refresh-table"><spring:message code="refresh" /></a><span id="refresh_alert" style="color:red;padding-left:10px;display:none;"><spring:message code="item.common.bundle.alert" /></span>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.setRepertory" /></div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> <spring:message code="item.common.repertoryNumber" /> <input type="text" name="availableQty" value="${bundleViewCommand.availableQty }" /> </label>
	   		<label class="label-line block pb10"> <spring:message code="item.common.isSynchronization" /> <input type="radio" name="syncWithInv" value="1" <c:if test="${bundleViewCommand.syncWithInv == true || bundleViewCommand.syncWithInv == null}">checked="checked"</c:if>><spring:message code="shop.property.true" />   <input type="radio" name="syncWithInv" value="0" <c:if test="${bundleViewCommand.syncWithInv == false }">checked="checked"</c:if>><spring:message code="shop.property.false" /> </label>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title"><spring:message code="item.common.setPrice" /></div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="1" <c:if test="${bundleViewCommand.priceType == 1 }">checked="checked"</c:if>> <spring:message code="item.common.onBundleTotalPrice" />  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="2" <c:if test="${bundleViewCommand.priceType == 2 }">checked="checked"</c:if>> <spring:message code="item.common.fixPrice" />  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="priceType" value="3" <c:if test="${bundleViewCommand.priceType == 3 }">checked="checked"</c:if>> <spring:message code="item.common.customPrice" />   </label>
	   		<!-- <label class="label-line block pb10"> <spring:message code="item.common.bundleTotalPrice" /> <input type="text" readonly placeholder="900" value=""> </label> -->
		</div>
	   	<table class="inform-person product-table" <c:if test="${bundleViewCommand.priceType != 2 }">style="display:none"</c:if>>
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
				 

