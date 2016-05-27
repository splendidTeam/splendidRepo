<!-- 销售属性信息 多语言 -->
<div class="ui-block-title1 ui-block-title">
	<spring:message code="item.update.sales" />
</div>
<c:if test="${i18nOnOff == true}">
	<c:set var="size" value="${fn:length(dynamicPropertyCommandList)}"></c:set>
	<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
		<c:set var="lang" value="${i18nLang.key}"></c:set>
		<c:set value="${status.index}" var="i"></c:set>
		<div class="ui-block-content border-grey saleInfo"
			lang="${i18nLang.key}"
			<c:if test="${i>0 }"> style="margin-top: 5px;" </c:if>>
			<div id="propertySpace">
				<c:forEach var="dynamicPropertyCommand"
					items="${dynamicPropertyCommandList}">
					<c:if test="${dynamicPropertyCommand.property.isSaleProp }">
						<div class="ui-block-line ">
							<c:if
								test="${ dynamicPropertyCommand.property.editingType == 4 && lang == defaultlang}">
								<c:if
									test="${not empty dynamicPropertyCommand.propertyValueList}">
									<label>${dynamicPropertyCommand.property.name }</label>
									<input type='hidden'
										value='${dynamicPropertyCommand.property.id }'
										name='propertyIds' />
									<input type='hidden' value='' name='propertyValueInputIds'
										pid='${dynamicPropertyCommand.property.id}' />

									<%-- 属性值分组  start --%>
									<c:if
										test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
										<select loxiaType="select" onchange="changeProGroup(this,1)"
											propertyId='${dynamicPropertyCommand.property.id }'>
											<option value=""><spring:message
													code="item.update.proGroupSelect" /></option>
											<c:forEach var="propertyValueGroup"
												items="${dynamicPropertyCommand.propertyValueGroupList}">
												<option value="${propertyValueGroup.id}">${propertyValueGroup.name}</option>
											</c:forEach>
										</select>
									</c:if>
									<%-- 属性值分组  end --%>

									<div>
										<c:forEach var="propertyValue"
											items="${dynamicPropertyCommand.propertyValueList}">
											<div class="priDiv">
												<span class="children-store"> <input type="checkbox"
													class='spCkb' name="propertyValueIds"
													pvId="${propertyValue.id }"
													pvValue="${propertyValue.value }"
													propertyId="${dynamicPropertyCommand.property.id }"
													propertyName="${dynamicPropertyCommand.property.name }"
													value="${propertyValue.id }"
													<c:forEach var="itemProperty" items="${itemProperties}">
									          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
									          	        checked=checked 
									          	      </c:if>
									          	   </c:forEach> />
													<c:if
														test="${dynamicPropertyCommand.property.isColorProp && dynamicPropertyCommand.property.hasThumb }">
														<img src="${base}/images/1.png">
													</c:if> ${propertyValue.value }
												</span>
											</div>
										</c:forEach>
									</div>
								</c:if>
							</c:if>
							<c:if
								test="${ dynamicPropertyCommand.property.editingType == 5 }">
								<label>${dynamicPropertyCommand.property.name }</label>
								<c:if test="${lang == defaultlang}">
									<input type='hidden'
										value='${dynamicPropertyCommand.property.id }'
										name='propertyIds' />
								</c:if>
								<input type='hidden' value='' class="propertyValueInputs"
									name='propertyValueInputs'
									pid='${dynamicPropertyCommand.property.id}' />
								<input type='hidden' value='${lang}' name='propertyValueInputs' />
								<div class="priDiv">

									<c:set var="str" value=""></c:set>
									<c:forEach items="${itemProperties}" var="itemProperty"
										varStatus="status">
										<c:if
											test="${ dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
											<c:forEach items="${itemProperty.propertyValue.langs}"
												var="l">
												<c:set value="${status.index}" var="j"></c:set>
												<c:if test="${lang == l}">
													<c:set var="str"
														value="${str}${itemProperty.propertyValue.values[i] }||"></c:set>
												</c:if>
											</c:forEach>
										</c:if>
									</c:forEach>

									<c:set var="str"
										value="${fn:substring(str,0,fn:length(str)-2) }"></c:set>
									<textarea class="customerSelect spTa" loxiaType="input"
										name="propertyValue" editingtype='5' isColorProp="true"
										propertyId=${dynamicPropertyCommand.property.id }
										style="width: 600px; height: 45px" mandatory="true">${str}</textarea>
									<span style="margin: 17px 0 0 10px; position: absolute;">${i18nLang.value}&nbsp;&nbsp;<spring:message
											code="item.update.betweenPropertyName" />'||'<spring:message
											code="item.update.split" /></span>

								</div>
							</c:if>
						</div>
					</c:if>
				</c:forEach>
			</div>

			<div id="exten" class="ui-block-line exten" style="display: none;">
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
								code="item.update.encodingSettings" /></span></a>

				</div>
			</div>
		</div>
	</c:forEach>
</c:if>

<!--销售属性信息 单语言-->
<c:if test="${i18nOnOff == false}">
	<div class="ui-block-content border-grey saleInfo">
		<div id="propertySpace">
			<c:forEach var="dynamicPropertyCommand"
				items="${dynamicPropertyCommandList}">
				<c:if test="${dynamicPropertyCommand.property.isSaleProp }">
					<div class="ui-block-line ">
						<c:if test="${ dynamicPropertyCommand.property.editingType == 4 }">
							<c:if
								test="${not empty dynamicPropertyCommand.propertyValueList}">
								<label>${dynamicPropertyCommand.property.name }</label>
								<input type='hidden'
									value='${dynamicPropertyCommand.property.id }'
									name='propertyIds' />
								<input type='hidden' value='' name='propertyValueInputIds'
									pid='${dynamicPropertyCommand.property.id}' />
								<c:if
									test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
									<select loxiaType="select" onchange="changeProGroup(this,1)"
										propertyId='${dynamicPropertyCommand.property.id }'>
										<option value=""><spring:message
												code="item.update.proGroupSelect" /></option>
										<c:forEach var="propertyValueGroup"
											items="${dynamicPropertyCommand.propertyValueGroupList}">
											<option value="${propertyValueGroup.id}">${propertyValueGroup.name}</option>
										</c:forEach>
									</select>
								</c:if>

								<div>
									<c:forEach var="propertyValue"
										items="${dynamicPropertyCommand.propertyValueList}">
										<div class="priDiv">
											<span class="children-store"> <input type="checkbox"
												class='spCkb' name="propertyValueIds"
												pvId="${propertyValue.id }"
												pvValue="${propertyValue.value }"
												propertyId="${dynamicPropertyCommand.property.id }"
												propertyName="${dynamicPropertyCommand.property.name }"
												value="${propertyValue.id }"
												<c:forEach var="itemProperty" items="${itemProperties}">
								          	     <c:out value="${itemProperty.propertyValueId}"></c:out>
								          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
								          	        checked=checked 
								          	      </c:if>
								          	   </c:forEach> />
												<c:if
													test="${dynamicPropertyCommand.property.isColorProp && dynamicPropertyCommand.property.hasThumb }">
													<img src="${base}/images/1.png">
												</c:if> ${propertyValue.value }
											</span>
										</div>
									</c:forEach>
								</div>
							</c:if>
						</c:if>
						<c:if test="${ dynamicPropertyCommand.property.editingType == 5 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<input type='hidden'
								value='${dynamicPropertyCommand.property.id }'
								name='propertyIds' />
							<input type='hidden' value='' name='propertyValueInputs'
								pid='${dynamicPropertyCommand.property.id}' />
							<div class="priDiv">
								<c:set var="str" value=""></c:set>
								<c:forEach items="${ itemProperties }" var="itemProperty"
									varStatus="status">
									<c:if
										test="${ dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
										<c:set var="str"
											value="${str}${ itemProperty.propertyValue.value }||"></c:set>
									</c:if>
								</c:forEach>

								<c:set var="str"
									value="${fn:substring(str,0,fn:length(str)-2) }"></c:set>
								<textarea class="customerSelect spTa" loxiaType="input"
									name="propertyValue" editingtype='5' isColorProp="true"
									propertyId=${dynamicPropertyCommand.property.id }
									style="width: 600px; height: 45px" mandatory="true">${str}</textarea>
								<span style="margin: 17px 0 0 10px; position: absolute;"><spring:message
										code="item.update.betweenPropertyName" />'||'<spring:message
										code="item.update.split" /></span>

							</div>
						</c:if>
					</div>
				</c:if>
			</c:forEach>
		</div>
		<div id="exten" class="ui-block-line exten" style="display: none;">
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
							code="item.update.encodingSettings" /></span></a>
			</div>
		</div>
	</div>
</c:if>