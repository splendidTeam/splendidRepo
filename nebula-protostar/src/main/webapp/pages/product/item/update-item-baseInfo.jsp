<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${base}/scripts/product/item/update-item-baseinfo.js"></script>

<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.update.info" />
</div>

<div class="ui-block-content border-grey baseInfo">
	<div class="ui-block-line ">
		<label><spring:message code="item.update.code" /></label>
		<div>
			<input type="text" class="fLeft" id="code" name="itemCommand.code"  loxiaType="input" value="${code}" mandatory="true" placeholder="<spring:message code='item.update.code'/>" />
		</div>
		<div id="loxiaTip-r" class="loxiaTip-r none-normal">
			<div class="arrow"></div>
			<div class="inner ui-corner-all codetip"></div>
		</div>
	</div>

	<input type="hidden" name="itemCommand.id" id="itemid" value="${id }"/>
	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label><spring:message code="item.update.name" /></label>
				<c:if test="${not empty title }">
					<input type="text" id="title"
						name="itemCommand.title.values[${status.index}]"
					    loxiaType="input"
						value="${title.langValues[i18nLang.key]}" mandatory="true"
						placeholder="<spring:message code='item.update.name'/>" />
				</c:if>
				<c:if test="${empty title }">
					<input type="text" id="title"
						name="itemCommand.title.values[${status.index}]"
						loxiaType="input" value="" mandatory="true"
						placeholder="<spring:message code='item.update.name'/>" />
				</c:if>
				<input class="i18n-lang" type="text"
					name="itemCommand.title.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label>商品副标题</label>
				<c:if test="${not empty subTilte }">
					<input type="text" id="subTitle"
						name="itemCommand.subTitle.values[${status.index}]"
						loxiaType="input"
						value="${ subTilte.langValues[i18nLang.key] }" mandatory="false"
						placeholder="商品副标题" />
				</c:if>
				<c:if test="${empty subTilte }">
					<input type="text" id="subTitle"
						name="itemCommand.subTitle.values[${status.index}]"
						loxiaType="input" value="" mandatory="false"
						placeholder="商品副标题" />
				</c:if>
				<input class="i18n-lang" type="text"
					name="itemCommand.subTitle.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
	</c:if>

	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label><spring:message code="item.update.name" /></label> <input
				type="text" id="title" name="itemCommand.title.value"
				loxiaType="input" value="${title.value}"
				mandatory="true"
				placeholder="<spring:message code='item.update.name'/>" />
		</div>
		<div class="ui-block-line ">
			<label>商品副标题</label>
			<textarea id="subTitle" name="itemCommand.subTitle.value" rows="10"
				class="ui-loxia-default ui-corner-all"><c:out
					value='${subTilte.value}' escapeXml="false"></c:out></textarea>

		</div>
	</c:if>

	<c:if test="${isStyleEnable}">
		<div class="ui-block-line ">
			<label><spring:message code='item.styleName' /></label>
			<div>
				<input type="text" id="style" name="itemCommand.style"
					loxiaType="input" value="${style }"
					mandatory="true" placeholder="" />
			</div>
		</div>

	</c:if>
	<div class="ui-block-line ">
		<label></label>

	</div>
	<div class="ui-block-line ">
		<label><spring:message code="item.update.classification" /></label>
		<div>
			<input type="text" id="category" loxiaType="button" readonly
				placeholder="<spring:message code='item.update.clickCategory'/>" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label></label>
		<div id="chooseCategory" class="Category-line">
			<c:forEach var="category" items="${categories}">
				<div class="${category.id } ">${category.name }
					<input type='hidden' name='categoriesIds' value="${category.id }" />
					<span class="categories-tips"> 
						<c:choose>
							<c:when test="${category.id eq defaultItemCategory.categoryId}">
								<spring:message code="item.update.defaultspan" />
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0);" id="${category.id }"
									style="text-decoration: underline; color: #F8A721"
									onclick='setCategroyDef(this.id)'><spring:message
										code="item.update.setdefault" /></a>
							</c:otherwise>
						</c:choose> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
						<a class="del" href="javascript:void(0);" id="${category.id }"
						onclick='delCategroy(this.id)'><spring:message
								code="item.update.deleteThis" /></a>
					</span> <br />
				</div>
			</c:forEach>
		</div>
	</div>
	<div class="ui-block-line ">
		<label></label>
		<div id="chooseDefaultCategoryHid">
			<input type='hidden' id="defaultCategoryId" name='defaultCategoryId' value="${defaultItemCategory.categoryId }" />
		</div>
	</div>
	<div class="ui-block-line ">
		<label>商品类型</label>
		<div>
			<select loxiaType="select" name="itemCommand.type">
				<option value="1" ${ type == '1'?'selected="selected"':'' }>主卖品</option>
				<option value="0" ${ type == '0'?'selected="selected"':'' }>非卖品</option>
			</select>
		</div>
	</div>
</div>