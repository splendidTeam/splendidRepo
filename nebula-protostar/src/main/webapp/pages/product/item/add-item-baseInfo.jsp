<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<script type="text/javascript" src="${base}/scripts/product/item/add-item-baseinfo.js"></script>

<script type="text/javascript">
var categoryzNodes  = [
	{id:0, pId:-1, name:"ROOT",	  code:"ROOT", sortNo:1,	  open:true, lifecycle:1},  
    <c:forEach var="category" items="${categoryList}" varStatus="status">
		{id:${category.id}, pId:${category.parentId}, 
			name:"${category.name}",
			code:"${category.code}", sortNo:${category.sortNo}, 
			drag:false, open:false,
			lifecycle:${category.lifecycle} 
		} 
		<c:if test="${!status.last}">,</c:if>
	</c:forEach>
];
</script>

<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.add.info" />
</div>

<div class="ui-block-content border-grey baseInfo">
	<div class="ui-block-line ">
		<label><spring:message code="item.add.code" /></label>
		<div>
			<input type="text" class="fLeft" id="code" name="itemCommand.code"
				loxiaType="input" value="" mandatory="true"
				placeholder="<spring:message code='item.add.code'/>" />
		</div>
		<div id="loxiaTip-r" class="loxiaTip-r none-normal">
			<div class="arrow"></div>
			<div class="inner ui-corner-all codetip"></div>
		</div>
	</div>

	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label><spring:message code="item.update.name" /></label> <input
					type="text" id="title"
					name="itemCommand.title.values[${status.index}]"
					loxiaType="input" value="" mandatory="true"
					placeholder="<spring:message code='item.update.name'/>" /> <input
					class="i18n-lang" type="text"
					name="itemCommand.title.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label><spring:message code='item.baseInfo.productSubhead'/></label> <input type="text" id="subTitle"
					name="itemCommand.subTitle.values[${status.index}]"
					loxiaType="input" value="${ subTilte }"
					mandatory="false" placeholder="<spring:message code='item.baseInfo.productSubhead'/>" /> <input class="i18n-lang"
					type="text" name="itemCommand.subTitle.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
	</c:if>
	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label><spring:message code="item.update.name" /></label> <input
				type="text" id="title" name="itemCommand.title.value"
			    loxiaType="input" value="${title}"
				mandatory="true"
				placeholder="<spring:message code='item.update.name'/>" />
		</div>
		<div class="ui-block-line ">
			<label><spring:message code='item.baseInfo.productSubhead'/></label> <input type="text" id="subTitle"
				name="itemCommand.subTitle.value" 
				loxiaType="input" value="${ subTilte }" mandatory="false"
				placeholder="<spring:message code='item.baseInfo.productSubhead'/>" />
		</div>
	</c:if>
	<c:if test="${isStyleEnable}">
		<div class="ui-block-line ">
			<label><spring:message code='item.styleName' /></label>
			<div>
				<input type="text" id="style" name="itemCommand.style"
					loxiaType="input" value="" mandatory="true"
					placeholder="<spring:message code='item.add.name'/>" />
			</div>
		</div>

	</c:if>


	<div class="ui-block-line none-normal">
		<label><spring:message code="item.update.defaultCategory" /></label>
		<div>
			<input type="text" id="defaultCategory" loxiaType="button" readonly
				placeholder="<spring:message code='item.update.clickCategory'/>" />
		</div>
	</div>

	<div class="ui-block-line ">
		<label></label>
		<div id="chooseDefaultCategory" class="chooseLine">
			<c:if test="${ !empty defaultItemCategory }">
				<div class="${defaultItemCategory.categoryId }">${defaultItemCategory.categoryName }
						<a class="choose-tips" href="javascript:void(0);" id="${defaultItemCategory.categoryId }" onclick='delDefaultCategroy(this.id)'><spring:message
							code="item.update.deleteThis" /></a>
						<br />
				</div>
			</c:if>
		</div>
	</div>


	<div class="ui-block-line ">
		<label><spring:message code="item.add.classification" /></label>
		<div>
			<input type="text" id="category" loxiaType="button" readonly
				placeholder="<spring:message code='item.add.clickCategory'/>" />
		</div>
	</div>

	<div class="ui-block-line ">
		<label></label>
		<div id="chooseCategory"></div>
	</div>
	<div class="ui-block-line ">
		<label></label>
		<div id="chooseDefaultCategoryHid">
			<input type='hidden' id="defaultCategoryId" name='defaultCategoryId' value="${defaultItemCategory.categoryId }" /> 
		</div>
	</div>
	<div class="mt10"></div>
	<div class="ui-block-line ">
		<label><spring:message code='item.baseInfo.productType'/></label>
		<div>
			<select loxiaType="select" name="itemCommand.type">
				<option value="1"><spring:message code='item.common.mainProduct'/></option>
				<option value="0"><spring:message code='item.baseInfo.notSaleProduct'/></option>
			</select>
		</div>
	</div>
</div>