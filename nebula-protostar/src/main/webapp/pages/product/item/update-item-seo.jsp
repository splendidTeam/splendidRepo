<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${base}/scripts/product/item/update-item-seo.js"></script>

<div class="ui-block-title1 ui-block-title">
	seo
	<spring:message code="product.property.lable.search" />
</div>

<div class="ui-block-content border-grey seo-info">
	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label>seo<spring:message
						code="item.update.seoTitle" /></label>
				<c:if test="${not empty seoTitle }">
					<input loxiaType="input" type="text"
						name="itemCommand.seoTitle.values[${status.index}]"
						value="${ seoTitle.langValues[i18nLang.key] }" />
				</c:if>
				<c:if test="${empty seoTitle }">
					<input loxiaType="input" type="text"
						name="itemCommand.seoTitle.values[${status.index}]" value="" />
				</c:if>
				<input class="i18n-lang" type="text"
					name="itemCommand.seoTitle.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label>seo<spring:message
						code="item.update.seoKeywords" /></label>
				<c:if test="${not empty seoKeywords }">
					<input loxiaType="input" type="text"
						name="itemCommand.seoKeywords.values[${status.index}]"
						value="${ seoKeywords.langValues[i18nLang.key] }"/>
				</c:if>
				<c:if test="${empty seoKeywords }">
					<input loxiaType="input" type="text"
						name="itemCommand.seoKeywords.values[${status.index}]" value="" />
				</c:if>

				<input class="i18n-lang" type="text"
					name="itemCommand.seoKeywords.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label>seo<spring:message
						code="item.update.seoDesc" /></label>
				<c:if test="${not empty seoDescription }">
					<textarea rows="10px"
						name="itemCommand.seoDescription.values[${status.index}]"
						loxiaType="input" >${ seoDescription.langValues[i18nLang.key] }</textarea>
				</c:if>
				<c:if test="${empty seoDescription }">
					<textarea rows="10px"
						name="itemCommand.seoDescription.values[${status.index}]"
						loxiaType="input"></textarea>
				</c:if>
				<input class="i18n-lang" type="text" 
					name="itemCommand.seoDescription.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>

	</c:if>

	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label>seo<spring:message code="item.update.seoTitle" /></label> 
			<input loxiaType="input" type="text" name="itemCommand.seoTitle.value" value="${seoTitle.value}" />
		</div>
		<div class="ui-block-line ">
			<label>seo<spring:message code="item.update.seoKeywords" /></label> 
			<input loxiaType="input" type="text" name="itemCommand.seoKeywords.value" value="${ seoKeywords.value }" />
		</div>
		<div class="ui-block-line ">
			<label>seo<spring:message code="item.update.seoDesc" /></label>
			<textarea rows="10px" name="itemCommand.seoDescription.value"
				loxiaType="input">${ seoDescription.value }</textarea>
		</div>
	</c:if>
</div>