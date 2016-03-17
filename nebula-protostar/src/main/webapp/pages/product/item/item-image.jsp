<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${ base }/scripts/product/item/item-image.js"></script>
<script type="text/javascript" src="${ base }/scripts/ajaxfileupload.js"></script>
<script type="text/javascript">
var isImageTypeGroupFlag = "${isImageTypeGroup}";
</script>
</head>
<body>

<div class="content-box width-percent100">
	<div class="ui-title1">
		<input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
		<input type="button"   value="全部删除" class="button butch batchDelete"  title="全部删除"/>
		<img src="/images/wmi/blacks/32x32/cube.png"><spring:message code="item.image.manager" />
		<%-- <input type="hidden" loxiaType="input" name="thumbnailConfig" id="thumbnailConfig" value="${thumbnailConfig[0].optionValue }" /> --%>
		<input type="hidden" loxiaType="input" name="baseImageUrl" id="baseImageUrl" value="${baseImageUrl}" />
    </div>
 	<form name="itemForm" action="/i18n/itemImage/saveItemImage.json" method="post">
 	<input id="itemId" name="itemId" value="${ item.id }" type="hidden"/>
 	<input id="itemName" name="itemName" value="${itemInfo.title}" type="hidden"/>
 	<div class="ui-block">
	    <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
	    	<table>
	        	<tr>
	                <td><label><spring:message code="item.code"/></label></td>
	                <td><span>${item.code}</span></td>
	                <td><label><spring:message code="item.name"/></label></td>
	                <td><span>${itemInfo.title}</span></td>
	            </tr>
	        </table>
	    </div>
    </div>
 	<div class="ui-block">
	<div style="margin-top: 10px"></div>
	
    <div class="ui-block-title1" ><spring:message code="item.update.imageInformation"/></div>
    <div class="ui-block-content border-grey">
          <c:if test="${ hasColorProp }">
          	<div class="ui-block-line colorPropertySelect" style="padding-bottom: 15px;">
           		<c:forEach items="${ propertyValueMapByProperty }" var="propertyMap">
		           <label><b>${ propertyMap.key.name }</b><spring:message code="item.image.property.value" /></label>
		           <select loxiaType="select" id="colorProperty" name="propertyValueIdAndProperyId">
		           		<c:choose>
		           			<%-- 属性类型为多选 --%>
		           			<c:when test="${ propertyMap.key.editingType == 4 }">
			           			<c:forEach items="${ propertyMap.value }" var="propertyValue">
			           				<c:forEach items="${ itemProperties }" var="itemProperty">
	                      				<c:if test="${ propertyValue.id == itemProperty.propertyValueId }">
				  							<option value="${itemProperty.id }" > ${propertyValue.value }</option>
					  					</c:if>
					  				</c:forEach>
			           			</c:forEach>
		           			</c:when>
		           			<%-- 属性类型为自定义多选 --%>
		           			<c:otherwise>
		           				<c:forEach items="${ itemProperties }" var="itemProperty">
						  			<c:if test="${ propertyMap.key.id ==  itemProperty.propertyId }">
					  					<option value="${itemProperty.id }"> ${itemProperty.propertyValue }</option>
						  			</c:if>
					  			</c:forEach>
		           			</c:otherwise>
		           		</c:choose>
		           	</select>
				</c:forEach>
	          </div>
			</c:if>
           <div id="colorPropertyContent">
               <%-----------------------------------------------------begin 通过图片类型分组显示 begin--------------------------------------------------------%>
               <c:if test="${isImageTypeGroup}">
               	   <c:set var="num" value="0"></c:set>
		           <c:forEach items="${imageListByTypeByItemPropertiesId}" var="imageListByTypeByItemPropertiesId">
		         
	              <%-- 商品颜色属性ID  BEGIN --%>
	           	   <div id="itemProeprtiesIs${ imageListByTypeByItemPropertiesId.key eq 0?'':imageListByTypeByItemPropertiesId.key }">
		           	   <%-- 商品颜色属性ID  BEGIN --%>
		           	   <c:forEach items="${ imageListByTypeByItemPropertiesId.value }" var="itemImageByType" >
		           	   		<%-- 商品图片类型  BEGIN --%>
		           	   		<c:forEach items="${ imageTypeMap }" var="imageType">
		           	   			<c:if test="${ imageType.key eq itemImageByType.key }">
					           	   <div class="ui-block-title1 ${ imageType.key }" style="width: 600px;">
			           	   				${ imageType.value }图片:
					           	   </div>
		           	   			</c:if>
		           	   		</c:forEach>
		           	   		<%-- 商品图片类型  END --%>
		           	   		<%-- 商品图片列表  BEGIN --%>
		           	   	   <div class="ui-block-content border-grey"  style="width: 600px;">
				           	   <div id="${ itemImageByType.key }${ imageListByTypeByItemPropertiesId.key eq 0?'': imageListByTypeByItemPropertiesId.key}">
				           	   	   <c:forEach items="${ itemImageByType.value }" var="itemImage" >
				           	   	   	   <!--国际化 TODO  -->
							           <div class="ui-block-line color-select-line color-select-line-add" style="border-top: 1px solid gray;margin-top: 2px;">
							           		<c:if test="${i18nOnOff == false}">
							           			<label>
							           				<img src="${ baseImageUrl }${ itemImage.picUrl.value}" class="color-select-img"/>
							           				<a class="func-button ml5 uploadlink toggleBtn" href ="javascript:void(0);"><span>显示URL</span></a>
							           			</label>
							           		</c:if>
							          		<input type="hidden" loxiaType="input" name="itemImages.id" value="${ itemImage.id }" />
							          		<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="${ itemImage.itemProperties }" />
							           		<div class="imgTag" <c:if test="${i18nOnOff == true}"> style="margin-left: 0px;" </c:if>>
							           			<div class="color-select-line">
							           				<span style="font: inherit;font-size: 13px;<c:if test="${i18nOnOff == true}">margin-left: 134px; </c:if>"><spring:message code="item.image.type" />:
							           				</span><opt:select name="itemImages.type"  id="imageType" cssClass="isImageTypeGroupFlag imageType" expression="chooseOption.IMAGE_TYPE" defaultValue="${ itemImage.type }" otherProperties="loxiaType=\"select\" "/>
							           				<span class="common-ic delete-ic"></span>
							           			</div>
							           			<c:if test="${i18nOnOff}">
								           			
									           	   <c:forEach items="${i18nLangs}" var="lang" varStatus="status">
									      			<c:set var="key" value="${lang.key}"></c:set>
									      			<c:set var="i" value="${status.index}"></c:set>
									           		<div class="itemImage${num} itemImage-add" style="float: left;width: 100%;margin-top: 2px;">
									           			<label>
									           				<img lang="${key}" src="${ baseImageUrl }${itemImage.picUrl.values[i]}" class="color-select-img"/>
									           				<a class="func-button ml5 uploadlink toggleBtn" href ="javascript:void(0);"><span>显示URL</span></a>
									           			</label>
									           			<div style="float: left;margin-left: 15px;">
										           			<div class="color-select-line" >
										           				<input type="hidden" readonly="true" class="picUrl-mutl" lang="${key}" id="itemImageUrl" name="itemImages.picUrl.values[${num}-${status.index}]" value="${baseImageUrl }${itemImage.picUrl.values[i]}"/>
										           				<input type="hidden" class="picUrl-mutl-lang" name="itemImages.picUrl.langs[${num}-${status.index}]" value="${key}"/>
										           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
										           					<input style="left:2px;" callback="colorComplete" class="imgUploadComponet fileupload" role="${thumbnailConfig[0].optionValue }" model="C"
										           					 hName="itemImage${num}-${key}" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>
										           					${lang.value}
										           				</a>
										           				<c:if test="${key == defaultlang and fn:length(itemImageByType.value)>=2}">
										           				<span class="common-ic up-ic" style="margin-left: 140px;"></span>
										           				</c:if>
										           			</div>	
										           			<%-- <c:if test="${key == defaultlang}">
											           			<div class="color-select-line">
											           				<span style="font: inherit;font-size: 13px; line-height: 25px;"><spring:message code="item.image.position" />:</span>
											           				<input loxiaType="number" name="itemImages.position" placeholder="<spring:message code="item.image.position" />" mandatory="true" style="width:160px;float: right;margin-right: 120px;" value="${ itemImage.position }"/>
											           			</div>	
										           			</c:if> --%>
										           			<div class="color-select-line" >
										           				<label style="font: inherit;font-size: 13px;"><spring:message code="item.image.description" />:</label>
										           				<input loxiaType="input" class="description-mutl" name="itemImages.description.values[${num}-${status.index}]" placeholder="<spring:message code="item.image.description" />"  style="width: 156px;margin-left: -59px;" value="${ itemImage.description.values[i]}"/>
										           				<input type="hidden" class="description-mutl-lang"  name="itemImages.description.langs[${num}-${status.index}]"  value="${key}"/>
										           				<c:if test="${key == defaultlang and fn:length(itemImageByType.value)>=2}">
										           				<span class="common-ic down-ic" ></span>
										           				</c:if>
										           			</div>
									           			</div>
									           			<div class="urlTab" style="float: left;margin-left: 15px;display:none;">
										           			<table>
										           				<tr>
																  <th style="width:80px">规格</th>
																  <th style="width:380px">URL</th>
																</tr>
																<c:choose>
										           					<c:when test="${not empty itemImage.picUrl.values && fn:length(itemImage.picUrl.values) >i && not empty itemImage.picUrl.values[i]}">
										           						<c:forEach items ="${typeSizeMap[itemImage.type]}" var ="picSize">
												           					<tr style="left: 10px;">
												           						<td style="text-align: center;">${picSize}</td>
												           						<td style="text-align: center;">
												           							${ baseImageUrl }${fn:split(itemImage.picUrl.values[i],'_')[0]}_${picSize}.${fn:split(itemImage.picUrl.values[i],'.')[1]}
												           						</td>
												           					</tr>
												           				</c:forEach>
										           					</c:when>
										           					<c:otherwise>
										           						<tr style="left: 10px;">
											           						<td style="text-align: center;">暂无</td>
											           						<td style="text-align: center;">
											           							暂无
											           						</td>
											           					</tr>
										           					</c:otherwise>
										           				</c:choose>
										           				
										           			</table>
										           		</div>
									           		  </div>
									           		 </c:forEach>
							           			</c:if>
							           			<c:if test="${i18nOnOff == false}">
								           			<div class="color-select-line itemImage${num}">
								           				<input type="hidden" readonly="true" class="picUrl-mutl" id="itemImageUrl" name="itemImages.picUrl.value[${num}]" value="${baseImageUrl }${itemImage.picUrl.value}"/>
								           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
								           					<input style="left:2px;" callback="colorComplete" class="imgUploadComponet fileupload" 
								           					role="${thumbnailConfig[0].optionValue }" model="C" hName="itemImage${num}" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>
								           				</a>
								           				<%-- <span class="common-ic up-ic" style=" margin-left: 8px;"></span> --%>
								           			</div>	
								           			<%-- <div class="color-select-line">
								           				<span style="font: inherit;font-size: 13px; line-height: 25px;"><spring:message code="item.image.position" />:</span>
								           				<input loxiaType="number" name="itemImages.position" placeholder="<spring:message code="item.image.position" />" mandatory="true" style="width:160px;float: right;margin-right: 120px;" value="${ itemImage.position }"/>
								           			</div> --%>
								           			<div class="color-select-line">
								           				<label style="font: inherit;font-size: 13px;"><spring:message code="item.image.description" />:</label>
								           				<input loxiaType="input" name="itemImages.description.value[${num}]" class="description-mutl" placeholder="<spring:message code="item.image.description" />"  style="width: 156px;margin-left: -59px;" value="${ itemImage.description.value }"/>
								           				<%-- <span class="common-ic down-ic"></span> --%>
								           			</div>
							           			</c:if>
							           			
							           		</div>
							           		<c:if test="${i18nOnOff == false}">
							           			<div class="urlTab" style="float: left;margin-left: 15px;display:none;">
									           			<table>
									           				<tr>
															  <th style="width:80px">规格</th>
															  <th style="width:380px">URL</th>
															</tr>
															<c:choose>
									           					<c:when test="${not empty itemImage.picUrl.value && not empty itemImage.picUrl.value}">
									           						<c:forEach items ="${typeSizeMap[itemImage.type]}" var ="picSize">
											           					<tr style="left: 10px;">
											           						<td style="text-align: center;">${picSize}</td>
											           						<td style="text-align: center;">
											           							${ baseImageUrl }${fn:split(itemImage.picUrl.value,'_')[0]}_${picSize}.${fn:split(itemImage.picUrl.value,'.')[1]}
											           						</td>
											           					</tr>
											           				</c:forEach>
									           					</c:when>
									           					<c:otherwise>
									           						<tr style="left: 10px;">
										           						<td style="text-align: center;">暂无</td>
										           						<td style="text-align: center;">
										           							暂无
										           						</td>
										           					</tr>
									           					</c:otherwise>
									           				</c:choose>
									           				
									           			</table>
									           		</div>
							           		</c:if>
							           		
							          	</div>
							          	  <c:set var="num" value="${num+1}"></c:set>
							          </c:forEach>
						          </div>
						          <c:if test="${ empty itemImageByType.value }">
						          	<span class="noImage">没有上传图片</span>
						          </c:if>
						          <div class="ui-block-line">
			       	 				<a href="javascript:void(0);" optionValue="${ itemImageByType.key }" class="color-select-add"></a>
			       				  </div>	
					          </div>
				          </c:forEach>
			          </div>
		           	
			       </c:forEach>
			          <%-- 商品图片列表  END --%>
		         </c:if>
				<%------------------------------------------------------------------end 通过图片类型分组显示 end----------------------------------------------------------------------%>

		        <%------------------------------------------------------------------begin 不通过图片类型分组显示 begin----------------------------------------------------------------------%>
		          <c:if test="${ not isImageTypeGroup }">
		             <c:set var="num" value="0"></c:set>
		          	<c:forEach items="${imageListByTypeByItemPropertiesId}" var="imageListByTypeByItemPropertiesId">
		          	   <c:set var="noImage" value="true" />
		           	   <%-- 商品颜色属性ID  BEGIN --%>
		           	   <div id="itemProeprtiesIs${ imageListByTypeByItemPropertiesId.key eq 0?'':imageListByTypeByItemPropertiesId.key }">
			           	   <%-- 商品颜色属性ID  BEGIN --%>
			           	   <div class="ui-block-content border-grey"  style="width: 600px;border-top: 1px solid gray;margin-top: 2px;">
			           	   	
				           	   <c:forEach items="${ imageListByTypeByItemPropertiesId.value }" var="itemImageByType" varStatus="istatus" >
				           	   		<%-- 商品图片列表  BEGIN --%>
				           	   	   <c:forEach items="${ itemImageByType.value }" var="itemImage" varStatus="st">
							           <div class="ui-block-line color-select-line color-select-line-add" style="border-top: 1px solid gray;margin-top: 2px;">
							          		<c:if test="${i18nOnOff == false }">
							          		<label>
							          				<img src="${ baseImageUrl }${ itemImage.picUrl.value}" class="color-select-img"/>
							          				<a class="func-button ml5 uploadlink toggleBtn" href ="javascript:void(0);"><span>显示URL</span></a>
							          		</label>
							          		</c:if>
							          		<input type="hidden" loxiaType="input" name="itemImages.id" value="${ itemImage.id }" />
							          		<input type="hidden" loxiaType="input" name="itemImages.itemProperties" value="${ itemImage.itemProperties }" />
								           	<c:if test="${i18nOnOff == true }">
								           		<div style="margin-left: 0px;">
							           			<div class="color-select-line" style="float: right;">
							           				<span style="font: inherit;font-size: 13px;margin-left: 134px;"><spring:message code="item.image.type" />:</span>
							           				<opt:select name="itemImages.type" id="imageType" cssClass="isImageTypeGroupFlag" expression="chooseOption.IMAGE_TYPE" defaultValue="${ itemImage.type }" otherProperties="loxiaType=\"select\" "/>
							           				<span class="common-ic delete-ic"></span>
							           			</div>
								      			<c:forEach items="${i18nLangs}" var="lang" varStatus="status">
								      			<c:set var="key" value="${lang.key}"></c:set>
								      			<c:set var="i" value="${status.index}"></c:set>
								           		<div class="itemImage${num} itemImage-add" style="float: left;margin-top: 2px;">
								           			<div style="float: left;margin-top: 2px;">
									           			<label>
									           				<img lang="${key}" src="${ baseImageUrl }${itemImage.picUrl.values[i]}" class="color-select-img"/>
									           				<a class="func-button ml5 uploadlink toggleBtn" href ="javascript:void(0);"><span>显示URL</span></a>
									           			</label>
									           			<div style="float: left;margin-left: 15px;">
										           			<div class="color-select-line" >
										           				<input type="hidden" readonly="true" class="picUrl-mutl" lang="${key}" id="itemImageUrl" name="itemImages.picUrl.values[${num}-${status.index}]" value="${baseImageUrl }${itemImage.picUrl.values[i]}"/>
										           				<input type="hidden" class="picUrl-mutl-lang" name="itemImages.picUrl.langs[${num}-${status.index}]" value="${key}"/>
										           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
										           					<input style="left:2px;" callback="colorComplete" class="imgUploadComponet fileupload" role="${thumbnailConfig[0].optionValue }" model="C"
										           					 hName="itemImage${num}-${key}" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>
										           					${lang.value}
										           				</a>
										           				<c:if test="${key == defaultlang}">
										           				<span class="common-ic up-ic" style="margin-left: 140px;"></span>
										           				</c:if>
										           			</div>		
										           			<div class="color-select-line">
										           				<label style="font: inherit;font-size: 13px;"><spring:message code="item.image.description" />:</label>
										           				<input loxiaType="input" class="description-mutl" name="itemImages.description.values[${num}-${status.index}]" placeholder="<spring:message code="item.image.description" />"  style="width: 156px;margin-left: -59px;" value="${ itemImage.description.values[i]}"/>
										           				<input type="hidden" class="description-mutl-lang"  name="itemImages.description.langs[${num}-${status.index}]"  value="${key}"/>
										           				<c:if test="${key == defaultlang}">
										           				<span class="common-ic down-ic" ></span>
										           				</c:if>
										           			</div>
									           			</div>
								           			</div>
								           			<div class="urlTab" style="float: left;margin-left: 15px;display:none;">
									           			<table>
									           				<tr>
															  <th style="width:80px">规格</th>
															  <th style="width:380px">URL</th>
															</tr>
															<c:choose>
									           					<c:when test="${not empty itemImage.picUrl.values && fn:length(itemImage.picUrl.values) >i && not empty itemImage.picUrl.values[i]}">
									           						<c:forEach items ="${typeSizeMap[itemImage.type]}" var ="picSize">
											           					<tr style="left: 10px;">
											           						<td style="text-align: center;">${picSize}</td>
											           						<td style="text-align: center;">
											           							${ baseImageUrl }${fn:split(itemImage.picUrl.values[i],'_')[0]}_${picSize}.${fn:split(itemImage.picUrl.values[i],'.')[1]}
											           						</td>
											           					</tr>
											           				</c:forEach>
									           					</c:when>
									           					<c:otherwise>
									           						<tr style="left: 10px;">
										           						<td style="text-align: center;">暂无</td>
										           						<td style="text-align: center;">
										           							暂无
										           						</td>
										           					</tr>
									           					</c:otherwise>
									           				</c:choose>
									           				
									           			</table>
									           		</div>
								           		  </div>
								           		 </c:forEach>
								           		</div>
							           		</c:if>
							           		
							           		<c:if test="${i18nOnOff == false }">
								           		<div class="imgTag">
								           			<div class="color-select-line">
								           				<span style="font: inherit;font-size: 13px;"><spring:message code="item.image.type" />:</span>
								           				<opt:select name="itemImages.type" id="imageType" cssClass="isImageTypeGroupFlag" expression="chooseOption.IMAGE_TYPE" defaultValue="${ itemImage.type }" otherProperties="loxiaType=\"select\" "/>
								           				<span class="common-ic delete-ic"></span>
								           			</div>
								           			<div class="color-select-line itemImage${num}">
								           				<input type="hidden" readonly="true" class="picUrl-mutl" id="itemImageUrl" name="itemImages.picUrl.value[${num}]" value="${baseImageUrl }${itemImage.picUrl.value}"/>
								           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
								           					<input style="left:2px;" callback="colorComplete" class="imgUploadComponet fileupload" role="${thumbnailConfig[0].optionValue }" model="C"
								           					 hName="itemImage${num}" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>
								           				</a>
								           				<span class="common-ic up-ic" style=" margin-left: 168px;"></span>
								           			</div>		
								           			<div class="color-select-line">
								           				<label style="font: inherit;font-size: 13px;"><spring:message code="item.image.description" />:</label>
								           				<input loxiaType="input" name="itemImages.description.value[${num}]" class="description-mutl" placeholder="<spring:message code="item.image.description" />"  style="width: 156px;margin-left: -59px;" value="${itemImage.description.value}"/>
								           				<span class="common-ic down-ic"></span>
								           			</div>
								           		</div>
								           		<div class="urlTab" style="float: left;margin-left: 15px;display:none;">
									           			<table>
									           				<tr>
															  <th style="width:80px">规格</th>
															  <th style="width:380px">URL</th>
															</tr>
															<c:choose>
									           					<c:when test="${not empty itemImage.picUrl.value && not empty itemImage.picUrl.value}">
									           						<c:forEach items ="${typeSizeMap[itemImage.type]}" var ="picSize">
											           					<tr style="left: 10px;">
											           						<td style="text-align: center;">${picSize}</td>
											           						<td style="text-align: center;">
											           							${ baseImageUrl }${fn:split(itemImage.picUrl.value,'_')[0]}_${picSize}.${fn:split(itemImage.picUrl.value,'.')[1]}
											           						</td>
											           					</tr>
											           				</c:forEach>
									           					</c:when>
									           					<c:otherwise>
									           						<tr style="left: 10px;">
										           						<td style="text-align: center;">暂无</td>
										           						<td style="text-align: center;">
										           							暂无
										           						</td>
										           					</tr>
									           					</c:otherwise>
									           				</c:choose>
									           				
									           			</table>
									           		</div>
							           		</c:if>
							           		 <c:set var="num" value="${num+1}"></c:set>
							          	</div>
							          </c:forEach>
						          </c:forEach>
						          
						          <c:forEach items="${ imageListByTypeByItemPropertiesId.value }" var="itemImageByType">
						          	<c:if test="${ not empty itemImageByType.value }">
						          		<c:set var="noImage" value="false" />
						          	</c:if>
						          </c:forEach>
						          <c:if test="${ noImage }">
							      	<span class="noImage">没有上传图片</span>
							      </c:if>
					          </div>
					          <div class="ui-block-line">
		       	 				<a href="javascript:void(0);" optionValue="${ imageListByTypeByItemPropertiesId.key }" class="color-select-add"></a>
		       				  </div>
				          </div>
			          </c:forEach>
		          </c:if>
		          <%------------------------------------------------------------------end 不通过图片类型分组显示 end----------------------------------------------------------------------%>
	          </div>
			</div>
		</div>
    </form>
    <div id="chooseOptionItemImageType" style="display: none">
   		<opt:select name="itemImages.type" cssClass="isImageTypeGroupFlag" id="imageType" expression="chooseOption.IMAGE_TYPE" defaultValue="${thumbnailConfig[0].optionLabel }" otherProperties="loxiaType=\"select\" "/>
	</div>
    <div class="button-line">
        <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
        <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
	</div>
</div>
<script type="text/javascript">
	var thumbConfigList = <c:out value="${ thumbnailConfigStr }" default="[]" escapeXml=""></c:out>;
	var isImageTypeGroup = <c:out value="${ isImageTypeGroup }" default="false" escapeXml=""></c:out>;
</script>
</body>
</html>