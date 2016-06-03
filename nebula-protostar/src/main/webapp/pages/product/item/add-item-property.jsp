<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript" src="${base}/scripts/product/item/add-item-property.js"></script>

<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.add.generalProperty" />
</div>

<div class="ui-block-content border-grey">
	<div id="notSalepropertySpace"></div>
</div>

<div class="mt10"></div>

<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.add.sales" />
</div>
<c:if test="${i18nOnOff == true}">

	<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
		<c:set var="lang" value="${i18nLang.key}"></c:set>
		<c:set value="${status.index}" var="i"></c:set>
		<div class="ui-block-content border-grey saleInfo <c:if test='${i>0 }'> mt5 </c:if>" lang="${i18nLang.key}"	>
			<div id="propertySpace"></div>

			<div id="exten" class="ui-block-line none-normal">
				<div id="extension">
					<table id="extensionTable" class="border-grey extensionTable p5">

					</table>
				</div>
			</div>

			<div class="ui-block-line ">
				<label></label>
				<div class="clear">
					<a href="javascript:void(0)" class="func-button extension height30" ><span><spring:message
								code="item.add.encodingSettings" /> </span></a>
				</div>
			</div>

		</div>
	</c:forEach>
</c:if>

<c:if test="${i18nOnOff == false}">
	<div class="ui-block-content border-grey saleInfo">
		<div id="propertySpace"></div>

		<div id="exten" class="ui-block-line none-normal">
			<div id="extension">
				<table id="extensionTable" class="border-grey p5">

				</table>
			</div>
		</div>

		<div class="ui-block-line ">
			<label></label>
			<div class="clear">
				<a href="javascript:void(0)" class="func-button extension height30"><span><spring:message
							code="item.add.encodingSettings" /> </span></a>
			</div>
		</div>

	</div>
</c:if>