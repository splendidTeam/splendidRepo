<%-- 一般属性 --%>
<div class="ui-block-title1" style="background: #fff; color: #000;">
	<spring:message code="item.update.generalProperty" />
</div>

<div class="ui-block-content border-grey">
	<c:set var="notSalePropNum" value="0"></c:set>
	<c:if test="${i18nOnOff == true}">
		<div id="notSalepropertySpace">
			<c:set var="size" value="${fn:length(dynamicPropertyCommandList)}"></c:set>
			<c:forEach var="dynamicPropertyCommand"
				items="${dynamicPropertyCommandList}" varStatus="pstatus">
				<c:if test="${!dynamicPropertyCommand.property.isSaleProp }">

					<!-- 多语言属性 -->
					<c:set value="${i18nLang.key}" var="dl"></c:set>
					<div class="ui-block-line ">
						<c:if test="${dynamicPropertyCommand.property.editingType==1 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<div>
								<c:forEach items="${i18nLangs}" var="i18nLang"
									varStatus="status">
									<c:set value="${status.index}" var="i"></c:set>
									<c:if test="${dynamicPropertyCommand.property.valueType==1 }">
										<input type='text'
											name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]'
											style='width: 600px' loxiaType='input'
											<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                     <c:if test="${not empty itemProperty.propertyValue }">
						          	                      value='${itemProperty.propertyValue.values[i]}' 
						          	                     </c:if>
						          	                     <c:if test="${empty itemProperty.propertyValue }">
						          	                      value='' 
						          	                     </c:if>
						          	                    </c:if>
						          	             </c:forEach>
											<c:if test="${dynamicPropertyCommand.property.required==true }">
						          	                mandatory="true"
						          	             </c:if> />
									</c:if>

									<c:if test="${dynamicPropertyCommand.property.valueType==2 }">
										<input type='text'
											name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]'
											loxiaType='number'
											<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                       <c:if test="${not empty itemProperty.propertyValue }">
						          	                      value='${itemProperty.propertyValue.values[i]}' 
						          	                     </c:if>
						          	                     <c:if test="${empty itemProperty.propertyValue }">
						          	                      value='' 
						          	                     </c:if>
						          	                    </c:if>
						          	             </c:forEach>
											<c:if test="${dynamicPropertyCommand.property.required==true }">
						          	                mandatory="true"
						          	             </c:if> />
									</c:if>

									<c:if test="${dynamicPropertyCommand.property.valueType==3 }">
										<input type="text"
											name="iProperties.propertyValue.values[${notSalePropNum}-${status.index}]"
											loxiaType="date"
											<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      <c:if test="${not empty itemProperty.propertyValue }">
						          	                      value='${itemProperty.propertyValue.values[i]}' 
						          	                     </c:if>
						          	                     <c:if test="${empty itemProperty.propertyValue }">
						          	                      value='' 
						          	                     </c:if>
						          	                    </c:if>
						          	             </c:forEach>
											<c:if test="${dynamicPropertyCommand.property.required==true }">
														 mandatory="true"
											     </c:if> />
									</c:if>

									<c:if test="${dynamicPropertyCommand.property.valueType==4 }">
										<input type="text" showtime="true"
											name="iProperties.propertyValue.values[${notSalePropNum}-${status.index}]"
											loxiaType="date"
											<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      <c:if test="${not empty itemProperty.propertyValue }">
							          	                     <c:if test="${not empty itemProperty.propertyValue }">
							          	                      value='${itemProperty.propertyValue.values[i]}' 
							          	                     </c:if>
							          	                     <c:if test="${empty itemProperty.propertyValue }">
							          	                      value='' 
							          	                     </c:if>
						          	                     </c:if>
						          	                     <c:if test="${empty itemProperty.propertyValue }">
						          	                      value='' 
						          	                     </c:if>
						          	                    </c:if>
						          	             </c:forEach>
											<c:if test="${dynamicPropertyCommand.property.required==true }">
														 mandatory="true"
											     </c:if> />
									</c:if>
									<input class="i18n-lang" type="text"
										name="iProperties.propertyValue.langs[${notSalePropNum}-${status.index}]"
										value="${i18nLang.key}" />
									<span>${i18nLang.value}</span>
									<br>
								</c:forEach>
								<input type='hidden' name='iProperties.propertyId'
									value='${dynamicPropertyCommand.property.id }' /> <input
									type='hidden' name='iProperties.id' value='' /> <input
									type='hidden' name='iProperties.propertyDisplayValue' value='' />
								<input type='hidden' name='iProperties.createTime' value='' /> <input
									type='hidden' name='iProperties.modifyTime' value='' /> <input
									type='hidden' name='iProperties.version' value='' /> <input
									type='hidden' name='iProperties.itemId' value='' /> <input
									type='hidden' name='iProperties.propertyValueId' value='' /> <input
									type='hidden' name='iProperties.picUrl' value='' />

							</div>
						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==2 }">
							<label>${dynamicPropertyCommand.property.name}</label>
							<div>
								<c:forEach var="itemProperty" items="${itemProperties}"
									varStatus="status">
									<c:if
										test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
										<select style='width: 160px; height: 25px'
											name='iProperties.propertyValueId' mandatory='true'
											onchange='doOther(this,${itemProperty.propertyId })'>
											<c:forEach var="propertyValue"
												items="${dynamicPropertyCommand.propertyValueList}">
												<option value='${propertyValue.id }'
													<c:if test="${itemProperty.propertyValueId==propertyValue.id }">
						          	                        selected=selected
						          	                     </c:if>>${propertyValue.value }</option>
											</c:forEach>
											<option value=''
												<c:if test="${itemProperty.propertyValueId==null }">
						          	                        selected=selected
						          	                     </c:if>><spring:message
													code="item.update.otherProp" /></option>
										</select>
										<input type='hidden' name='iProperties.propertyId'
											value='${dynamicPropertyCommand.property.id }' />
										<input type='hidden' name='iProperties.id' value='' />
										<input type='hidden' name='iProperties.propertyDisplayValue'
											value='' />
										<input type='hidden' name='iProperties.createTime' value='' />
										<input type='hidden' name='iProperties.modifyTime' value='' />
										<input type='hidden' name='iProperties.version' value='' />
										<input type='hidden' name='iProperties.itemId' value='' />
										<c:forEach items="${i18nLangs}" var="i18nLang"
											varStatus="status">
											<c:set value="${status.index}" var="i"></c:set>
											<input
												<c:if test="${itemProperty.propertyValueId==null }">
						          	                        type='text'
						          	                      </c:if>
												<c:if test="${itemProperty.propertyValueId!=null }">
						          	                        type='hidden'
						          	                      </c:if>
												id="pv_${itemProperty.propertyId }"
												name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]'
												value='${itemProperty.propertyValue.values[i]}' />
											<span>${zh_cn_lang}</span>
											<input class="i18n-lang" type="text"
												name="iProperties.propertyValue.langs[${notSalePropNum}-${status.index}]"
												value="${i18nLang.key}" />
											<br>
										</c:forEach>
										<input type='hidden' name='iProperties.picUrl' value='' />

									</c:if>
								</c:forEach>

							</div>
						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==3 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<div>
								<select style='width: 160px; height: 25px'
									name='iProperties.propertyValueId' mandatory='true'>
									<c:forEach var="propertyValue"
										items="${dynamicPropertyCommand.propertyValueList}">
										<option value='${propertyValue.id }'
											<c:forEach var="itemProperty" items="${itemProperties}">
							          	                    <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
							          	                    selected=selected
							          	                     </c:if>
							          	                </c:forEach>>${propertyValue.value }</option>
									</c:forEach>
								</select> <input type='hidden' name='iProperties.propertyId'
									value='${dynamicPropertyCommand.property.id }' /> <input
									type='hidden' name='iProperties.id' value='' /> <input
									type='hidden' name='iProperties.propertyDisplayValue' value='' />
								<input type='hidden' name='iProperties.createTime' value='' /> <input
									type='hidden' name='iProperties.modifyTime' value='' /> <input
									type='hidden' name='iProperties.version' value='' /> <input
									type='hidden' name='iProperties.itemId' value='' />
								<c:forEach items="${i18nLangs}" var="i18nLang"
									varStatus="status">
									<input type='hidden'
										name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]'
										value='' />
									<input class="i18n-lang" type="text"
										name="iProperties.propertyValue.langs[${notSalePropNum}-${status.index}]"
										value="${i18nLang.key}" />
								</c:forEach>
								<input type='hidden' name='iProperties.picUrl' value='' />
							</div>
						</c:if>
						<!-- <spring:message code="shop.property.multiple.choose"/> -->
						<c:if test="${dynamicPropertyCommand.property.editingType==4 }">
							<c:if
								test="${not empty dynamicPropertyCommand.propertyValueList}">
								<label>${dynamicPropertyCommand.property.name }</label>
								<c:if
									test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
									<select loxiaType="select" onchange="changeProGroup(this,2)"
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
											<span class="children-store  normalCheckBoxSpan"> <input
												name="filtratecolor" tname="${propertyValue.value }"
												type='checkbox' class='normalCheckBoxCls'
												pid="${dynamicPropertyCommand.property.id }"
												mustCheck='${dynamicPropertyCommand.property.name }'
												value='${propertyValue.id }'
												<c:forEach var="itemProperty" items="${itemProperties}">
										          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
										          	        checked=checked 
										          	      </c:if>
										          	   </c:forEach> />${propertyValue.value }
											</span>
										</div>
									</c:forEach>
								</div>
							</c:if>

						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==5 }">
							<%-- <c:if test="${not empty dynamicPropertyCommand.propertyValueList}"> --%>
							<label>${dynamicPropertyCommand.property.name }</label>
							<div class="priDiv">
								<c:forEach items="${i18nLangs}" var="i18nLang"
									varStatus="istatus">
									<c:set value="${istatus.index}" var="i"></c:set>
									<c:set var="str" value=""></c:set>
									<c:forEach items="${ itemProperties }" var="itemProperty"
										varStatus="status">
										<c:if
											test="${dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
											<c:if test="${not empty itemProperty.propertyValue }">
												<c:set var="str"
													value="${str}${ itemProperty.propertyValue.values[i] }||"></c:set>
											</c:if>
											<c:if test="${empty itemProperty.propertyValue }">
												<c:set var="str" value=""></c:set>
											</c:if>
										</c:if>
									</c:forEach>
									<c:set var="str"
										value="${fn:substring(str,0,fn:length(str)-2) }"></c:set>
									<textarea class="customerSelect" loxiaType="input"
										name="iProperties.propertyValue.values[${notSalePropNum}-${istatus.index}]"
										isColorProp="false" editingtype='5'
										propertyId=${dynamicPropertyCommand.property.id }
										style="width: 600px; height: 45px">${str} </textarea>
									<input class="i18n-lang" type="text"
										name="iProperties.propertyValue.langs[${notSalePropNum}-${istatus.index}]"
										value="${i18nLang.key}" />
									<span style="margin: 17px 0 0 10px; position: absolute;">
										${i18nLang.value}&nbsp;&nbsp;<spring:message
											code="item.update.betweenPropertyName" />'||' <spring:message
											code="item.update.split" />
									</span>
									<br>
								</c:forEach>
								<input type="hidden"
									value='${dynamicPropertyCommand.property.id}'
									name="iProperties.propertyId"> <input type="hidden"
									value="" name="iProperties.id"> <input type="hidden"
									value="" name="iProperties.propertyDisplayValue"> <input
									type="hidden" value="" name="iProperties.createTime"> <input
									type="hidden" value="" name="iProperties.modifyTime"> <input
									type="hidden" value="" name="iProperties.version"> <input
									type="hidden" value="" name="iProperties.itemId"> <input
									type="hidden" value="" name="iProperties.picUrl"> <input
									type="hidden" value="" name="iProperties.propertyValueId">

							</div>
						</c:if>
					</div>
					<c:if test="${dynamicPropertyCommand.property.editingType ne 4}">
						<c:set var="notSalePropNum" value="${notSalePropNum+1}"></c:set>
					</c:if>
				</c:if>
			</c:forEach>
			<c:set var="notSalePropSize" value="0"></c:set>
			<c:forEach var="dynamicPropertyCommand"
				items="${dynamicPropertyCommandList}" varStatus="status">
				<c:if test="${!dynamicPropertyCommand.property.isSaleProp }">
					<c:if test="${dynamicPropertyCommand.property.editingType==4 }">
						<c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
							<c:forEach var="propertyValue"
								items="${dynamicPropertyCommand.propertyValueList}">
								<span class="hidBoxSpan">
									<div type='hidden' class='repNormalCheckBoxCls'
										pid="${dynamicPropertyCommand.property.id }"
										pvid='${propertyValue.id }'></div>
								</span>
							</c:forEach>
						</c:if>
					</c:if>
					<c:if test="${dynamicPropertyCommand.property.editingType ne 4 }">
						<c:set var="notSalePropSize" value="${notSalePropSize+1}"></c:set>
					</c:if>
				</c:if>
			</c:forEach>
			<input type="hidden" id="notSalePropSize" value="${notSalePropSize}" />
		</div>
	</c:if>
	<%-- 一般属性单语言 --%>
	<c:if test="${i18nOnOff == false}">
		<div id="notSalepropertySpace">
			<c:set var="size" value="${fn:length(dynamicPropertyCommandList)}"></c:set>
			<c:forEach var="dynamicPropertyCommand"
				items="${dynamicPropertyCommandList}" varStatus="status">
				<c:if test="${!dynamicPropertyCommand.property.isSaleProp }">

					<div class="ui-block-line ">
						<c:if test="${dynamicPropertyCommand.property.editingType==1 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<div>
								<c:if test="${dynamicPropertyCommand.property.valueType==1 }">
									<input type='text'
										name='iProperties.propertyValue.value[${notSalePropNum}]'
										style='width: 600px' loxiaType='input'
										<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
										<c:if test="${dynamicPropertyCommand.property.required==true }">
						          	                mandatory="true"
						          	             </c:if> />
								</c:if>
								<c:if test="${dynamicPropertyCommand.property.valueType==2 }">
									<input type='text'
										name='iProperties.propertyValue.value[${notSalePropNum}]'
										loxiaType='number'
										<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
										<c:if test="${dynamicPropertyCommand.property.required==true }">
						          	                mandatory="true"
						          	             </c:if> />
								</c:if>
								<c:if test="${dynamicPropertyCommand.property.valueType==3 }">
									<input type="text"
										name="iProperties.propertyValue.value[${notSalePropNum}]"
										loxiaType="date"
										<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
										<c:if test="${dynamicPropertyCommand.property.required==true }">
														 mandatory="true"
											     </c:if> />
								</c:if>

								<c:if test="${dynamicPropertyCommand.property.valueType==4 }">
									<input type="text" showtime="true"
										name="iProperties.propertyValue.value[${notSalePropNum}]"
										loxiaType="date"
										<c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
										<c:if test="${dynamicPropertyCommand.property.required==true }">
														 mandatory="true"
											     </c:if> />
								</c:if>
								<input type='hidden' name='iProperties.propertyId'
									value='${dynamicPropertyCommand.property.id }' /> <input
									type='hidden' name='iProperties.id' value='' /> <input
									type='hidden' name='iProperties.propertyDisplayValue' value='' />
								<input type='hidden' name='iProperties.createTime' value='' /> <input
									type='hidden' name='iProperties.modifyTime' value='' /> <input
									type='hidden' name='iProperties.version' value='' /> <input
									type='hidden' name='iProperties.itemId' value='' /> <input
									type='hidden' name='iProperties.propertyValueId' value='' /> <input
									type='hidden' name='iProperties.picUrl' value='' />

							</div>
						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==2 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<div>
								<c:forEach var="itemProperty" items="${itemProperties}">
									<c:if
										test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
										<select style='width: 160px; height: 25px'
											name='iProperties.propertyValueId' mandatory='true'
											onchange='doOther(this,${itemProperty.propertyId })'>
											<c:forEach var="propertyValue"
												items="${dynamicPropertyCommand.propertyValueList}">
												<option value='${propertyValue.id }'
													<c:if test="${itemProperty.propertyValueId==propertyValue.id }">
						          	                        selected=selected
						          	                     </c:if>>${propertyValue.value }</option>
											</c:forEach>
											<option value=''
												<c:if test="${itemProperty.propertyValueId==null }">
						          	                        selected=selected
						          	                     </c:if>><spring:message
													code="item.update.otherProp" /></option>
										</select>
										<input type='hidden' name='iProperties.propertyId'
											value='${dynamicPropertyCommand.property.id }' />
										<input type='hidden' name='iProperties.id' value='' />
										<input type='hidden' name='iProperties.propertyDisplayValue'
											value='' />
										<input type='hidden' name='iProperties.createTime' value='' />
										<input type='hidden' name='iProperties.modifyTime' value='' />
										<input type='hidden' name='iProperties.version' value='' />
										<input type='hidden' name='iProperties.itemId' value='' />
										<input
											<c:if test="${itemProperty.propertyValueId==null }">
						          	                        type='text'
						          	                      </c:if>
											<c:if test="${itemProperty.propertyValueId!=null }">
						          	                        type='hidden'
						          	                      </c:if>
											id="pv_${itemProperty.propertyId }"
											name='iProperties.propertyValue.value[${notSalePropNum}]'
											value='${itemProperty.propertyValue.value}' />
										<input type='hidden' name='iProperties.picUrl' value='' />

									</c:if>
								</c:forEach>

							</div>
						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==3 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<div>
								<select style='width: 160px; height: 25px'
									name='iProperties.propertyValueId' mandatory='true'>
									<c:forEach var="propertyValue"
										items="${dynamicPropertyCommand.propertyValueList}">
										<option value='${propertyValue.id }'
											<c:forEach var="itemProperty" items="${itemProperties}">
							          	                    <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
							          	                    selected=selected
							          	                     </c:if>
							          	                </c:forEach>>${propertyValue.value }</option>
									</c:forEach>
								</select> <input type='hidden' name='iProperties.propertyId'
									value='${dynamicPropertyCommand.property.id }' /> <input
									type='hidden' name='iProperties.id' value='' /> <input
									type='hidden' name='iProperties.propertyDisplayValue' value='' />
								<input type='hidden' name='iProperties.createTime' value='' /> <input
									type='hidden' name='iProperties.modifyTime' value='' /> <input
									type='hidden' name='iProperties.version' value='' /> <input
									type='hidden' name='iProperties.itemId' value='' /> <input
									type='hidden'
									name='iProperties.propertyValue.value[${notSalePropNum}]'
									value='' /> <input type='hidden' name='iProperties.picUrl'
									value='' />
							</div>
						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==4 }">
							<c:if
								test="${not empty dynamicPropertyCommand.propertyValueList}">
								<label>${dynamicPropertyCommand.property.name }</label>
								<%-- 属性值分组  start --%>
								<c:if
									test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
									<select loxiaType="select" onchange="changeProGroup(this,2)"
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
											<span class="children-store  normalCheckBoxSpan"> <input
												name="filtratecolor" tname="${propertyValue.value }"
												type='checkbox' class='normalCheckBoxCls'
												pid="${dynamicPropertyCommand.property.id }"
												mustCheck='${dynamicPropertyCommand.property.name }'
												value='${propertyValue.id }'
												<c:forEach var="itemProperty" items="${itemProperties}">
										          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
										          	        checked=checked 
										          	      </c:if>
										          	   </c:forEach> />${propertyValue.value }
											</span>
										</div>
									</c:forEach>
								</div>
							</c:if>
						</c:if>
						<c:if test="${dynamicPropertyCommand.property.editingType==5 }">
							<label>${dynamicPropertyCommand.property.name }</label>
							<div class="priDiv">
								<c:forEach items="${ itemProperties }" var="itemProperty"
									varStatus="status">
									<c:if
										test="${ dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
										<c:set var="str"
											value="${str}${ itemProperty.propertyValue}||"></c:set>
									</c:if>
								</c:forEach>

								<c:set var="str"
									value="${fn:substring(str,0,fn:length(str)-2) }"></c:set>
								<textarea class="customerSelect" loxiaType="input"
									name="iProperties.propertyValue.value[${notSalePropNum}]"
									isColorProp="false" editingtype='5'
									propertyId=${dynamicPropertyCommand.property.id }
									style="width: 600px; height: 45px">${str} </textarea>
								<input type="hidden"
									value='${dynamicPropertyCommand.property.id }'
									name="iProperties.propertyId"> <input type="hidden"
									value="" name="iProperties.id"> <input type="hidden"
									value="" name="iProperties.propertyDisplayValue"> <input
									type="hidden" value="" name="iProperties.createTime"> <input
									type="hidden" value="" name="iProperties.modifyTime"> <input
									type="hidden" value="" name="iProperties.version"> <input
									type="hidden" value="" name="iProperties.itemId"> <input
									type="hidden" value="" name="iProperties.picUrl"> <input
									type="hidden" value="" name="iProperties.propertyValueId">
								<span style="margin: 17px 0 0 10px; position: absolute;"><spring:message
										code="item.update.betweenPropertyName" />'||'<spring:message
										code="item.update.split" /></span>
							</div>
						</c:if>
					</div>
					<c:if test="${dynamicPropertyCommand.property.editingType ne 4}">
						<c:set var="notSalePropNum" value="${notSalePropNum+1}"></c:set>
					</c:if>
				</c:if>
			</c:forEach>
			<c:set var="notSalePropSize" value="0"></c:set>
			<c:forEach var="dynamicPropertyCommand"
				items="${dynamicPropertyCommandList}" varStatus="status">
				<c:if test="${!dynamicPropertyCommand.property.isSaleProp }">
					<c:if test="${dynamicPropertyCommand.property.editingType==4 }">
						<c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
							<c:forEach var="propertyValue"
								items="${dynamicPropertyCommand.propertyValueList}">
								<span class="hidBoxSpan">
									<div type='hidden' class='repNormalCheckBoxCls'
										pid="${dynamicPropertyCommand.property.id }"
										pvid='${propertyValue.id }'></div>
								</span>
							</c:forEach>
						</c:if>
					</c:if>
					<c:if test="${dynamicPropertyCommand.property.editingType ne 4 }">
						<c:set var="notSalePropSize" value="${notSalePropSize+1}"></c:set>
					</c:if>
				</c:if>
			</c:forEach>
			<input type="hidden" id="notSalePropSize" value="${notSalePropSize}" />
		</div>
	</c:if>
</div>


<div style="margin-top: 10px"></div>
 
<!-- 销售属性信息 多语言 -->
<div class="ui-block-title1" style="background: #fff; color: #000;">
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