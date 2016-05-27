<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript" src="${base}/scripts/product/item/add-item-seo.js"></script>

<div class="ui-block-title1"
	style="background: #fff; color: #000; width: 600px">
	seo
	<spring:message code="product.property.lable.search" />
</div>

<div class="ui-block-content border-grey">
	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style="">seo<spring:message
						code="item.update.seoTitle" /></label> <input loxiaType="input"
					style='width: 600px'
					name="itemCommand.seoTitle.values[${status.index}]"
					value="${ seoTitle }" /> <input class="i18n-lang" type="text"
					name="itemCommand.seoTitle.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style="">seo<spring:message
						code="item.update.seoKeywords" /></label> <input loxiaType="input"
					name="itemCommand.seoKeywords.values[${status.index}]"
					value="${ seoKeywords }" style="width: 600px" /> <input
					class="i18n-lang" type="text"
					name="itemCommand.seoKeywords.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style="">seo<spring:message
						code="item.update.seoDesc" /></label>
				<textarea rows="10px"
					name="itemCommand.seoDescription.values[${status.index}]"
					loxiaType="input" style="width: 600px;">${ seoDescription }</textarea>
				<input class="i18n-lang" type="text"
					name="itemCommand.seoDescription.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
	</c:if>

	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label style="">seo<spring:message
					code="item.update.seoTitle" /></label> <input loxiaType="input"
				style='width: 600px' name="itemCommand.seoTitle.value"
				value="${seoTitle}" />
		</div>
		<div class="ui-block-line ">
			<label style="">seo<spring:message
					code="item.update.seoKeywords" /></label> <input loxiaType="input"
				name="itemCommand.seoKeywords.value" value="${ seoKeywords }"
				style="width: 600px" />
		</div>
		<div class="ui-block-line ">
			<label style="">seo<spring:message code="item.update.seoDesc" /></label>
			<textarea rows="10px" name="itemCommand.seoDescription.value"
				loxiaType="input" style="width: 600px;">${ seoDescription }</textarea>
		</div>
	</c:if>

</div>