
<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.update.description" />
</div>

<div class="ui-block-content border-grey">


	<c:if test="${i18nOnOff == true}">
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label style=""><spring:message code="item.update.sketch" /></label>
				<c:if test="${not empty sketch }">
					<textarea rows="10px"
						name="itemCommand.sketch.values[${status.index}]"
						loxiaType="input" style="width: 600px;">${sketch.langValues[i18nLang.key]}</textarea>
				</c:if>
				<c:if test="${empty sketch}">
					<textarea rows="10px"
						name="itemCommand.sketch.values[${status.index}]"
						loxiaType="input" style="width: 600px;"></textarea>
				</c:if>
				<input class="i18n-lang" type="text"
					name="itemCommand.sketch.langs[${status.index}]"
					value="${i18nLang.key}" /> <span>${i18nLang.value}</span>
			</div>
		</c:forEach>
		<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			<c:set value="${status.index}" var="i"></c:set>
			<div class="ui-block-line ">
				<label style=""><spring:message
						code="item.update.lable.description" /></label>
				<div style="float: left; margin-left: 0px">
					<c:if test="${not empty description }">
						<textarea id="editor${status.index}"
							name="itemCommand.description.values[${status.index}]" rows="20"
							cols="120">
				${description.langValues[i18nLang.key]}
			 </textarea>
					</c:if>
					<c:if test="${empty description }">
						<textarea id="editor${status.index}"
							name="itemCommand.description.values[${status.index}]" rows="20"
							cols="120">
		 		</textarea>
					</c:if>

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
					loxiaType="input" style="width: 600px;">${ sketch.value }</textarea>
			</div>
		</div>

		<div class="ui-block-line ">
			<label style=""><spring:message
					code="item.update.lable.description" /></label>

			<div>
				<textarea id="" name="itemCommand.description.value" rows="20"
					style="width: 600px;" class="ui-loxia-default ui-corner-all">${description.value}</textarea>
			</div>
		</div>
	</c:if>


</div>