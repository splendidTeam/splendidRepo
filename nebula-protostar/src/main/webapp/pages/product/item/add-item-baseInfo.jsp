<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.add.info" />
</div>

<div class="ui-block-content border-grey">
	<div class="ui-block-line ">
		<label><spring:message code="item.add.code" /></label>
		<div>
			<input type="text" class="fLeft" id="code" name="itemCommand.code"
				style="width: 600px" loxiaType="input" value="" mandatory="true"
				placeholder="<spring:message code='item.add.code'/>" />
		</div>
		<div id="loxiaTip-r" class="loxiaTip-r" style="display: none">
			<div class="arrow"></div>
			<div class="inner ui-corner-all codetip"
				style="padding: .3em .7em; width: auto;"></div>
		</div>
	</div>

	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label><spring:message code="item.update.name" /></label> <input
					type="text" id="title"
					name="itemCommand.title.values[${status.index}]"
					style='width: 600px' loxiaType="input" value="" mandatory="true"
					placeholder="<spring:message code='item.update.name'/>" /> <input
					class="i18n-lang" type="text"
					name="itemCommand.title.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label>商品副标题</label> <input type="text" id="subTitle"
					name="itemCommand.subTitle.values[${status.index}]"
					style='width: 600px' loxiaType="input" value="${ subTilte }"
					mandatory="false" placeholder="商品副标题" /> <input class="i18n-lang"
					type="text" name="itemCommand.subTitle.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
	</c:if>
	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label><spring:message code="item.update.name" /></label> <input
				type="text" id="title" name="itemCommand.title.value"
				style='width: 600px' loxiaType="input" value="${title}"
				mandatory="true"
				placeholder="<spring:message code='item.update.name'/>" />
		</div>
		<div class="ui-block-line ">
			<label>商品副标题</label> <input type="text" id="subTitle"
				name="itemCommand.subTitle.value" style='width: 600px'
				loxiaType="input" value="${ subTilte }" mandatory="false"
				placeholder="商品副标题" />
		</div>
	</c:if>
	<c:if test="${isStyleEnable}">
		<div class="ui-block-line ">
			<label><spring:message code='item.styleName' /></label>
			<div>
				<input type="text" id="style" name="itemCommand.style"
					style="width: 600px" loxiaType="input" value="" mandatory="true"
					placeholder="<spring:message code='item.add.name'/>" />
			</div>
		</div>

	</c:if>


	<div class="ui-block-line " style="display: none">
		<label><spring:message code="item.update.defaultCategory" /></label>
		<div>
			<input type="text" id="defaultCategory" loxiaType="button" readonly
				placeholder="<spring:message code='item.update.clickCategory'/>" />
		</div>
	</div>

	<div class="ui-block-line ">
		<label></label>
		<div id="chooseDefaultCategory">
			<c:if test="${ !empty defaultItemCategory }">
				<div class="${defaultItemCategory.categoryId }">${defaultItemCategory.categoryName }
					<input type='hidden' name='defaultCategoryId'
						value="${defaultItemCategory.categoryId }" /> <a
						href="javascript:void(0);" id="${defaultItemCategory.categoryId }"
						style="float: right; margin-right: 760px; text-decoration: underline; color: #F8A721"
						onclick='delDefaultCategroy(this.id)'><spring:message
							code="item.update.deleteThis" /></a><br />
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
			<input type='hidden' name='defCategroyId' id='defCategroyId' value="" />
		</div>
	</div>
	<div style="margin-top: 10px"></div>
	<div class="ui-block-line ">
		<label>商品类型</label>
		<div>
			<select loxiaType="select" name="itemCommand.type">
				<option value="1">主卖品</option>
				<option value="0">非卖品</option>
			</select>
		</div>
	</div>
</div>