<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.add.description" />
</div>

<div class="ui-block-content border-grey">

	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style=""><spring:message code="item.update.sketch" /></label>
				<textarea rows="10px"
					name="itemCommand.sketch.values[${status.index}]" loxiaType="input"
					style="width: 600px;">${ sketch }</textarea>
				<input class="i18n-lang" type="text"
					name="itemCommand.sketch.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>

		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<div class="ui-block-line ">
				<label style=""><spring:message
						code="item.update.lable.description" /></label>
				<div style="float: left; margin-left: 0px">
					<textarea id="editor${status.index}"
						name="itemCommand.description.values[${status.index}]" rows="20"
						cols="120">
			${description}
		 </textarea>
					<input class="i18n-lang" type="text"
						name="itemCommand.description.langs[${status.index}]"
						value="${i18nLang.key}" />
				</div>
				<span style="display: block; float: left;">${i18nLang.value}</span>
			</div>
		</c:forEach>
	</c:if>

	<c:if test="${i18nOnOff == false}">
		<div class="ui-block-line ">
			<label style=""><spring:message code="item.update.sketch" /></label>
			<div>
				<textarea rows="10px" name="itemCommand.sketch.value"
					loxiaType="input" style="width: 600px;">${ sketch }</textarea>
			</div>
		</div>

		<div class="ui-block-line ">
			<label style=""><spring:message
					code="item.update.lable.description" /></label>
			<div>
				<textarea id="editor" name="itemCommand.description.value" rows="20"
					cols="120">
			${description}
		  </textarea>
			</div>
		</div>
	</c:if>
</div>