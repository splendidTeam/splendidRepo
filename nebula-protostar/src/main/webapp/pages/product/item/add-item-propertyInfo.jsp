<%-- 一般属性信息  --%>
<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.add.generalProperty" />
</div>

<div class="ui-block-content border-grey">
	<div id="notSalepropertySpace"></div>
</div>

<div style="margin-top: 10px"></div>

<%-- 销售属性信息 --%>
<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.add.sales" />
</div>
<c:if test="${i18nOnOff == true}">

	<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
		<c:set var="lang" value="${i18nLang.key}"></c:set>
		<c:set value="${status.index}" var="i"></c:set>
		<div class="ui-block-content border-grey saleInfo"
			lang="${i18nLang.key}"
			<c:if test="${i>0 }"> style="margin-top: 5px;" </c:if>>
			<div id="propertySpace"></div>

			<div id="exten" class="ui-block-line" style="display: none;">
				<div id="extension">
					<table id="extensionTable" class="border-grey extensionTable"
						style="padding: 5px;">

					</table>
				</div>
			</div>

			<div class="ui-block-line ">
				<label></label>
				<div style="clear: both;">
					<a href="javascript:void(0)" class="func-button extension"
						style="height: 30px;"><span><spring:message
								code="item.add.encodingSettings" /> </span></a>
				</div>
			</div>

		</div>
	</c:forEach>
</c:if>

<c:if test="${i18nOnOff == false}">
	<div class="ui-block-content border-grey saleInfo">
		<div id="propertySpace"></div>

		<div id="exten" class="ui-block-line  " style="display: none;">
			<div id="extension">
				<table id="extensionTable" class="border-grey" style="padding: 5px;">

				</table>
			</div>
		</div>

		<div class="ui-block-line ">
			<label></label>
			<div style="clear: both;">
				<a href="javascript:void(0)" class="func-button extension"
					style="height: 30px;"><span><spring:message
							code="item.add.encodingSettings" /> </span></a>
			</div>
		</div>

	</div>
</c:if>