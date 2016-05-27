<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";

</script>
<script type="text/javascript" src="${base}/scripts/product/item/update-item.js"></script>
<SCRIPT type="text/javascript">	
var pdValidCode = "${pdValidCode}";
var categoryzNodes  = [
				{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	{id:${category.id}, pId:${category.parentId}, 
              		name:"${category.name}",
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		<c:forEach var="checkedCategory" items="${categories}">
              		       <c:if test="${checkedCategory.id ==category.id}">
              		            checked:true,
              		       </c:if>
              		</c:forEach>
              		drag:false, open:true,
              		lifecycle:${category.lifecycle} } 
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];

var skuList=<c:out value="${skuList}" default="[]" escapeXml="false" ></c:out>;
var dynamicPropertyCommandListJsonStr=<c:out value="${dynamicPropertyCommandListJsonStr}" default="[]" escapeXml="false"></c:out>;
var lastSelectPropertyId=<c:out value="${lastSelectPropertyId}" default="[]" escapeXml="false"></c:out>;
var lastSelectPropertyValueId=<c:out value="${lastSelectPropertyValueId}" default="[]" escapeXml="false"></c:out>;
var itemPropertiesStr = <c:out value="${itemPropertiesStr}" default = "[]" escapeXml="false"></c:out>;
</SCRIPT>
<style type="text/css">
.i18n-lang{
display: none;
}
</style>
</head>
<body>

<div class="content-box width-percent100">
    
   <form name="itemForm" action="/i18n/item/saveItem.json" method="post">
    <input type="hidden" id="industryId" name="itemCommand.industryId"  value=""/>
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
    <input type="hidden" name="itemCommand.id" id="itemid" value="${id }"/>
    <input type="hidden" id="propertyIdArray" value="${propertyIdArray }"/>
    <input type="hidden" id="propertyNameArray" value="${propertyNameArray }"/>
    <input type="hidden" id="mustCheckArray" value="${mustCheckArray }"/>
   

	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.update.manage"/></div>
	<div class="ui-block">
	<div id="second" >
	
		<div class="ui-block">
	        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
	            <table>
	                <tr>
	                    <td><label><spring:message code="item.update.selectedIndustry"/></label></td>
	                    <td><span id="chooseIndustry">${industry.name }</span></td>
	                    
	                </tr>
	            </table>
	        </div>
	    </div>
	
	
	<div class="ui-block ">
		
	
		 <div class="ui-block-title1"><spring:message code="item.update.updateItem"/></div>
	   <div class="ui-block-title1 ui-block-title"><spring:message code="item.update.info"/></div>
	
	   <div class="ui-block-content border-grey">
	   <div class="ui-block-line ">
         <label ><spring:message code="item.update.code"/></label>
          <div >
              <input type="text" class="fLeft" id="code" name="itemCommand.code" style='width: 600px'  loxiaType="input" value="${code}" mandatory="true" placeholder="<spring:message code='item.update.code'/>"/>
         </div>
         <div id="loxiaTip-r" class="loxiaTip-r" style="display:none">
		 	  <div class="arrow"></div>
		      <div class="inner ui-corner-all codetip" style="padding: .3em .7em; width: auto;"></div>
	     </div>
	   </div>
	   
	   <c:if test="${i18nOnOff == true}">
	   <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	   <c:set value="${status.index}" var="i"></c:set>
	   <div class="ui-block-line ">
         <label ><spring:message code="item.update.name"/></label>
         <c:if test="${not empty title }">
         <input type="text" id="title" name="itemCommand.title.values[${status.index}]" style='width: 600px' loxiaType="input" value="${title.langValues[i18nLang.key]}" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
         </c:if>
         <c:if test="${empty title }">
         <input type="text" id="title" name="itemCommand.title.values[${status.index}]" style='width: 600px' loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
         </c:if>
         <input class="i18n-lang" type="text" name="itemCommand.title.langs[${status.index}]" value="${i18nLang.key}" />
        
         <span>${i18nLang.value}</span>
	   </div>
	   </c:forEach>
	   <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	   <c:set value="${status.index}" var="i"></c:set>
	     <div class="ui-block-line ">
         <label>商品副标题</label>
         <c:if test="${not empty subTilte }">
         <input type="text" id="subTitle" name="itemCommand.subTitle.values[${status.index}]" style='width: 600px' loxiaType="input" value="${ subTilte.langValues[i18nLang.key] }" mandatory="false" placeholder="商品副标题"/>
         </c:if>
         <c:if test="${empty subTilte }">
          <input type="text" id="subTitle" name="itemCommand.subTitle.values[${status.index}]" style='width: 600px' loxiaType="input" value="" mandatory="false" placeholder="商品副标题"/>
         </c:if>
	     <input class="i18n-lang" type="text" name="itemCommand.subTitle.langs[${status.index}]" value="${i18nLang.key}" />
         <span>${i18nLang.value}</span>
	     </div>
	      </c:forEach>
	    </c:if>
	    
	    <c:if test="${i18nOnOff == false}">
	     <div class="ui-block-line ">
         <label ><spring:message code="item.update.name"/></label>
         <input type="text" id="title" name="itemCommand.title.value" style='width: 600px' loxiaType="input" value="${title.value}" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
	     </div>
	     <div class="ui-block-line ">
         <label>商品副标题</label>
         <textarea id="subTitle" name="itemCommand.subTitle.value" rows="10" style="width: 600px;" class="ui-loxia-default ui-corner-all"><c:out value='${subTilte.value}' escapeXml="false"></c:out></textarea>
<%--          <input type="text" id="subTitle" name="itemCommand.subTitle.value" style='width: 600px' loxiaType="input" value="<c:out value='${subTilte.value}' escapeXml="false"></c:out>" mandatory="false" placeholder="商品副标题"/> --%>
         
	     </div>
	   	</c:if>
	    
	   <c:if test="${isStyleEnable}">
	     	<div class="ui-block-line ">
		        <label ><spring:message code='item.styleName'/></label>
		          <div >
		              <input type="text" id="style" name="itemCommand.style" style="width: 600px" loxiaType="input" value="${style }" mandatory="true" placeholder=""/>
		         </div>
		   </div>
      
		</c:if>
	   <%-- <div class="ui-block-line " >
         <label ><spring:message code="item.update.defaultCategory"/></label>
         
          <div style="line-height:30px;">
				${defaultItemCategory.categoryName }[${defaultItemCategory.categoryCode}]
         </div>
	   </div> --%>
	   <div class="ui-block-line ">
         <label></label>
         
         </div>
	   <div class="ui-block-line ">
         <label ><spring:message code="item.update.classification"/></label>
          <div >
              <input type="text" id="category" loxiaType="button" readonly placeholder="<spring:message code='item.update.clickCategory'/>"  />
         </div>
	   </div>
	   <div class="ui-block-line ">
         <label></label>
          <div id="chooseCategory">
            <c:forEach var="category" items="${categories}">
			  <div class="${category.id }">${category.name }
				  <input type='hidden' name='categoriesIds'  value="${category.id }" />
				  <span style='float:right;margin-right: 1000px;'>
				  	<c:choose>
				  		<c:when test="${category.id eq defaultItemCategory.categoryId}">
				  			<spring:message code="item.update.defaultspan"/>
				  		</c:when>
				  		<c:otherwise>
				  			<a href="javascript:void(0);" id="${category.id }" style="text-decoration: underline;color:#F8A721"
				   				onclick='setCategroyDef(this.id)'><spring:message code="item.update.setdefault"/></a>
				  		</c:otherwise>
				  	</c:choose>
				  	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				  	<a href="javascript:void(0);" id="${category.id }" style="text-decoration: underline;color:#F8A721"
				   		onclick='delCategroy(this.id)'><spring:message code="item.update.deleteThis"/></a>
				   </span>
				   <br/>
			   </div>
			</c:forEach>
         </div>
	   </div>
	   <div class="ui-block-line ">
         <label></label>
          <div id="chooseDefaultCategoryHid">
			<input type='hidden' name='defCategroyId' id='defCategroyId'  value="${defaultItemCategory.categoryId}" />
         </div>
        </div>
		<div class="ui-block-line ">
         <label>商品类型</label>
         <div>
         	<select loxiaType="select" name="itemCommand.type">
				<option value="1" ${ type == '1'?'selected="selected"':'' }>主卖品</option>
         		<option value="0" ${ type == '0'?'selected="selected"':'' }>非卖品</option>
			</select>
         </div>
	   </div>
	   	</div>
	   	<div style="margin-top: 10px"></div>
	   	
		   <div class="ui-block-title1 ui-block-title" ><spring:message code="item.update.generalProperty"/></div>
		
		   <div class="ui-block-content border-grey">
		   		<%-- 一般属性 --%>
		    <c:set var="notSalePropNum" value="0"></c:set>
		   	<c:if test="${i18nOnOff == true}">
		      <div id="notSalepropertySpace">
		      	<c:set var="size" value="${fn:length(dynamicPropertyCommandList)}"></c:set>
		        <c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}" varStatus="pstatus">
			          <c:if test="${!dynamicPropertyCommand.property.isSaleProp }">
			       
			          <!-- 多语言属性 -->
			           	<c:set value="${i18nLang.key}" var="dl"></c:set>
					        <div class="ui-block-line ">
					              <c:if test="${dynamicPropertyCommand.property.editingType==1 }">
							          <label>${dynamicPropertyCommand.property.name }</label>
							          <div>
							             <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			           					  <c:set value="${status.index}" var="i"></c:set>
							               <c:if test="${dynamicPropertyCommand.property.valueType==1 }">
					          	            <input type='text' name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]' style='width: 600px' loxiaType='input' 
					          	          
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
						          	             </c:if>
					          	                 />
				          	            </c:if>
				          	           
				          	            <c:if test="${dynamicPropertyCommand.property.valueType==2 }">
					          	            <input type='text' name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]' loxiaType='number' 
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
						          	             </c:if>
					          	                 />
				          	           </c:if>
						          	           
				          	            <c:if test="${dynamicPropertyCommand.property.valueType==3 }">
				          	                     <input type="text"  name="iProperties.propertyValue.values[${notSalePropNum}-${status.index}]" loxiaType="date" 
				          	             
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
											     </c:if>
											     />
				          	             </c:if>
				          	             
				          	            <c:if test="${dynamicPropertyCommand.property.valueType==4 }">
				          	                     <input type="text" showtime="true"  name="iProperties.propertyValue.values[${notSalePropNum}-${status.index}]" loxiaType="date" 
				          	             
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
											     </c:if>
											     />
				          	           </c:if>
				          	            <input class="i18n-lang"  type="text" name="iProperties.propertyValue.langs[${notSalePropNum}-${status.index}]"  value="${i18nLang.key}"/>
				          	            <span>${i18nLang.value}</span><br>
				          	            </c:forEach>
				                       <input type='hidden' name='iProperties.propertyId' value='${dynamicPropertyCommand.property.id }'/>
				                       <input type='hidden' name='iProperties.id' value=''/>
				                       <input type='hidden' name='iProperties.propertyDisplayValue' value=''/>
				                       <input type='hidden' name='iProperties.createTime' value=''/>
				                       <input type='hidden' name='iProperties.modifyTime' value=''/>
				                       <input type='hidden' name='iProperties.version' value=''/>
				                       <input type='hidden' name='iProperties.itemId' value=''/>
				                       <input type='hidden' name='iProperties.propertyValueId' value=''/>
				                       <input type='hidden' name='iProperties.picUrl' value=''/>
						          	      
							          </div>
						           </c:if>
						           <c:if test="${dynamicPropertyCommand.property.editingType==2 }">
							          <label>${dynamicPropertyCommand.property.name}</label>
							          <div>
							              <c:forEach var="itemProperty" items="${itemProperties}" varStatus="status">
						          	         <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	             <select style='width:160px;height:25px' name='iProperties.propertyValueId' mandatory='true' onchange='doOther(this,${itemProperty.propertyId })'>
						          	                 <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
						          	                    <option value ='${propertyValue.id }'
						          	                      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
						          	                        selected=selected
						          	                     </c:if> 
						          	                     >${propertyValue.value }</option>
						          	                 </c:forEach>
						          	                 <option value =''
						          	                     <c:if test="${itemProperty.propertyValueId==null }">
						          	                        selected=selected
						          	                     </c:if>
						          	                 ><spring:message code="item.update.otherProp"/></option>
						          	             </select>
						          	             <input type='hidden' name='iProperties.propertyId' value='${dynamicPropertyCommand.property.id }'/>
						          	               <input type='hidden' name='iProperties.id' value=''/>
							                       <input type='hidden' name='iProperties.propertyDisplayValue' value=''/>
							                       <input type='hidden' name='iProperties.createTime' value=''/>
							                       <input type='hidden' name='iProperties.modifyTime' value=''/>
							                       <input type='hidden' name='iProperties.version' value=''/>
							                       <input type='hidden' name='iProperties.itemId' value=''/>
							                       <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
			           					  		   <c:set value="${status.index}" var="i"></c:set>
							                       <input <c:if test="${itemProperty.propertyValueId==null }">
						          	                        type='text'
						          	                      </c:if> 
						          	                      <c:if test="${itemProperty.propertyValueId!=null }">
						          	                        type='hidden'
						          	                      </c:if>
						          	                      id="pv_${itemProperty.propertyId }" name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]' value='${itemProperty.propertyValue.values[i]}'/>
						          	                       <span>${zh_cn_lang}</span>
						          	               <input class="i18n-lang"  type="text" name="iProperties.propertyValue.langs[${notSalePropNum}-${status.index}]"  value="${i18nLang.key}"/><br>
							                      </c:forEach>
							                       <input type='hidden' name='iProperties.picUrl' value=''/>
						          	               
						          	         </c:if>
						          	      </c:forEach>
								         
							          </div>
						          </c:if>
						          
						          <!-- <spring:message code="shop.property.single.choose"/> -->
						           <c:if test="${dynamicPropertyCommand.property.editingType==3 }">
							          <label>${dynamicPropertyCommand.property.name }</label>
							           <div>        
					          	             <select style='width:160px;height:25px' name='iProperties.propertyValueId' mandatory='true'>
					          	                 <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
					          	                    <option value ='${propertyValue.id }'
					          	                    
					          	                       <c:forEach var="itemProperty" items="${itemProperties}">
							          	                    <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
							          	                    selected=selected
							          	                     </c:if>
							          	                </c:forEach>  
							          	                    
					          	                    >${propertyValue.value }</option>
					          	                 </c:forEach>
					          	             </select>
					          	             <input type='hidden' name='iProperties.propertyId' value='${dynamicPropertyCommand.property.id }'/>
					          	             <input type='hidden' name='iProperties.id' value=''/>
						                     <input type='hidden' name='iProperties.propertyDisplayValue' value=''/>
						                     <input type='hidden' name='iProperties.createTime' value=''/>
						                     <input type='hidden' name='iProperties.modifyTime' value=''/>
						                     <input type='hidden' name='iProperties.version' value=''/>
						                     <input type='hidden' name='iProperties.itemId' value=''/>
						                     <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
						                 	 <input type='hidden' name='iProperties.propertyValue.values[${notSalePropNum}-${status.index}]' value=''/>
						                     <input class="i18n-lang"  type="text" name="iProperties.propertyValue.langs[${notSalePropNum}-${status.index}]"  value="${i18nLang.key}"/>
						                     </c:forEach>
						                     <input type='hidden' name='iProperties.picUrl' value=''/>
							          </div>
						          </c:if>
						           <!-- <spring:message code="shop.property.multiple.choose"/> -->
						           <c:if test="${dynamicPropertyCommand.property.editingType==4 }">
						             <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
							         <label>${dynamicPropertyCommand.property.name }</label>
									 <c:if test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
										 <select  loxiaType="select" onchange="changeProGroup(this,2)"  propertyId='${dynamicPropertyCommand.property.id }'>
										 	<option value=""><spring:message code="item.update.proGroupSelect" /></option>
										 	<c:forEach var="propertyValueGroup"  items="${dynamicPropertyCommand.propertyValueGroupList}">
										 		<option value="${propertyValueGroup.id}">${propertyValueGroup.name}</option>
										 	</c:forEach>
										 </select>
									  </c:if>
									  <div>
							          <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
								     	 <div class="priDiv">
									          	 <span class="children-store  normalCheckBoxSpan">
									          	 <input name="filtratecolor" tname="${propertyValue.value }"  type='checkbox' class = 'normalCheckBoxCls' pid="${dynamicPropertyCommand.property.id }" mustCheck='${dynamicPropertyCommand.property.name }'value='${propertyValue.id }' 
										          	   <c:forEach var="itemProperty" items="${itemProperties}">
										          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
										          	        checked=checked 
										          	      </c:if>
										          	   </c:forEach>
										          	  />${propertyValue.value }
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
										   <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="istatus">
			           					   <c:set value="${istatus.index}" var="i"></c:set>
			           					    <c:set var="str" value=""></c:set> 
											<c:forEach items="${ itemProperties }" var="itemProperty" varStatus="status">
											<c:if test="${dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
													 <c:if test="${not empty itemProperty.propertyValue }">
													 <c:set var="str" value="${str}${ itemProperty.propertyValue.values[i] }||"></c:set> 
					          	                     </c:if>
					          	                     <c:if test="${empty itemProperty.propertyValue }">
					          	                     <c:set var="str" value=""></c:set> 
					          	                     </c:if>
											</c:if>
											</c:forEach>	
											
											<c:set var = "str" value = "${fn:substring(str,0,fn:length(str)-2) }"></c:set>
											<textarea class="customerSelect" loxiaType="input" name="iProperties.propertyValue.values[${notSalePropNum}-${istatus.index}]" isColorProp="false" editingtype='5' 
											 propertyId=${dynamicPropertyCommand.property.id } style="width: 600px; height: 45px" >${str} </textarea>
											   <input class="i18n-lang"  type="text" name="iProperties.propertyValue.langs[${notSalePropNum}-${istatus.index}]"  value="${i18nLang.key}"/>
													<span style="margin: 17px 0 0 10px; position: absolute;"> ${i18nLang.value}&nbsp;&nbsp;<spring:message code="item.update.betweenPropertyName"/>'||'
											<spring:message code="item.update.split"/></span><br>
											 </c:forEach>  
											<input type="hidden" value='${dynamicPropertyCommand.property.id}' name="iProperties.propertyId"> 
											<input type="hidden" value="" name="iProperties.id">
											<input type="hidden" value="" name="iProperties.propertyDisplayValue">
											<input type="hidden" value="" name="iProperties.createTime">
											<input type="hidden" value="" name="iProperties.modifyTime">
											<input type="hidden" value="" name="iProperties.version">
											<input type="hidden" value="" name="iProperties.itemId">
											<input type="hidden" value="" name="iProperties.picUrl">
											<input type="hidden" value="" name="iProperties.propertyValueId">

								         </div> 
						          </c:if>
						    </div>
						    	<c:if test="${dynamicPropertyCommand.property.editingType ne 4}">
						    		<c:set var="notSalePropNum" value="${notSalePropNum+1}"></c:set>
						    	</c:if>
						    	
					    </c:if>
		           </c:forEach>
		           <c:set var="notSalePropSize" value="0"></c:set>
					<c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}" varStatus="status">
						<c:if test="${!dynamicPropertyCommand.property.isSaleProp }">
							<c:if test="${dynamicPropertyCommand.property.editingType==4 }">
					         	 <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
						          <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
							          	 <span class="hidBoxSpan">
							          	 	<div type='hidden' class ='repNormalCheckBoxCls' pid="${dynamicPropertyCommand.property.id }" pvid='${propertyValue.id }'>
							          	 	</div>
							          	 </span>
							        </c:forEach>
							   </c:if>
					        </c:if>
					        <c:if test="${dynamicPropertyCommand.property.editingType ne 4 }">
					        	<c:set var="notSalePropSize" value="${notSalePropSize+1}"></c:set>
					        </c:if>
						</c:if>
					</c:forEach>
					<input type ="hidden" id="notSalePropSize" value="${notSalePropSize}"/>
			   </div>
			   </c:if>
			 <%-- 一般属性单语言 --%>
			 <c:if test="${i18nOnOff == false}">
		      <div id="notSalepropertySpace">
		      <c:set var="size" value="${fn:length(dynamicPropertyCommandList)}"></c:set>
		        <c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}" varStatus="status">
			          <c:if test="${!dynamicPropertyCommand.property.isSaleProp }">
			          
					        <div class="ui-block-line ">
					              <c:if test="${dynamicPropertyCommand.property.editingType==1 }">
							          <label>${dynamicPropertyCommand.property.name }</label>
							          <div>
				          	           <c:if test="${dynamicPropertyCommand.property.valueType==1 }">
					          	            <input type='text' name='iProperties.propertyValue.value[${notSalePropNum}]' style='width: 600px' loxiaType='input' 
					          	          
						          	             <c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
					          	            
						          	             <c:if test="${dynamicPropertyCommand.property.required==true }">
						          	                mandatory="true"
						          	             </c:if>
					          	                 />
				          	            </c:if>
				          	           
				          	            <c:if test="${dynamicPropertyCommand.property.valueType==2 }">
					          	            <input type='text' name='iProperties.propertyValue.value[${notSalePropNum}]' loxiaType='number' 
						          	             <c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
				 
						          	             <c:if test="${dynamicPropertyCommand.property.required==true }">
						          	                mandatory="true"
						          	             </c:if>
					          	                 />
				          	           </c:if>
						          	           
				          	            <c:if test="${dynamicPropertyCommand.property.valueType==3 }">
				          	                     <input type="text"  name="iProperties.propertyValue.value[${notSalePropNum}]" loxiaType="date" 
				          	             
						          	             <c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
										     	 <c:if test="${dynamicPropertyCommand.property.required==true }">
														 mandatory="true"
											     </c:if>
											     />
				          	             </c:if>
				          	             
				          	            <c:if test="${dynamicPropertyCommand.property.valueType==4 }">
				          	                     <input type="text" showtime="true"  name="iProperties.propertyValue.value[${notSalePropNum}]" loxiaType="date" 
				          	             
						          	             <c:forEach var="itemProperty" items="${itemProperties}">
						          	                    <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	                      value='${itemProperty.propertyValue.value}' 
						          	                    </c:if>
						          	             </c:forEach>
										     	 <c:if test="${dynamicPropertyCommand.property.required==true }">
														 mandatory="true"
											     </c:if>
											     />
				          	           </c:if>
					                       <input type='hidden' name='iProperties.propertyId' value='${dynamicPropertyCommand.property.id }'/>
					                       <input type='hidden' name='iProperties.id' value=''/>
					                       <input type='hidden' name='iProperties.propertyDisplayValue' value=''/>
					                       <input type='hidden' name='iProperties.createTime' value=''/>
					                       <input type='hidden' name='iProperties.modifyTime' value=''/>
					                       <input type='hidden' name='iProperties.version' value=''/>
					                       <input type='hidden' name='iProperties.itemId' value=''/>
					                       <input type='hidden' name='iProperties.propertyValueId' value=''/>
					                       <input type='hidden' name='iProperties.picUrl' value=''/>
						          	      
							          </div>
						           </c:if>
						           <c:if test="${dynamicPropertyCommand.property.editingType==2 }">
							          <label>${dynamicPropertyCommand.property.name }</label>
							          <div>
							              <c:forEach var="itemProperty" items="${itemProperties}">
						          	         <c:if test="${itemProperty.propertyId==dynamicPropertyCommand.property.id }">
						          	             <select style='width:160px;height:25px' name='iProperties.propertyValueId' mandatory='true' onchange='doOther(this,${itemProperty.propertyId })'>
						          	                 <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
						          	                    <option value ='${propertyValue.id }'
						          	                      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
						          	                        selected=selected
						          	                     </c:if> 
						          	                     >${propertyValue.value }</option>
						          	                 </c:forEach>
						          	                 <option value =''
						          	                     <c:if test="${itemProperty.propertyValueId==null }">
						          	                        selected=selected
						          	                     </c:if>
						          	                 ><spring:message code="item.update.otherProp"/></option>
						          	             </select>
						          	             <input type='hidden' name='iProperties.propertyId' value='${dynamicPropertyCommand.property.id }'/>
						          	               <input type='hidden' name='iProperties.id' value=''/>
							                       <input type='hidden' name='iProperties.propertyDisplayValue' value=''/>
							                       <input type='hidden' name='iProperties.createTime' value=''/>
							                       <input type='hidden' name='iProperties.modifyTime' value=''/>
							                       <input type='hidden' name='iProperties.version' value=''/>
							                       <input type='hidden' name='iProperties.itemId' value=''/>
							                       <input <c:if test="${itemProperty.propertyValueId==null }">
						          	                        type='text'
						          	                      </c:if> 
						          	                      <c:if test="${itemProperty.propertyValueId!=null }">
						          	                        type='hidden'
						          	                      </c:if>
						          	                      id="pv_${itemProperty.propertyId }" name='iProperties.propertyValue.value[${notSalePropNum}]' value='${itemProperty.propertyValue.value}'/>
							                       <input type='hidden' name='iProperties.picUrl' value=''/>
						          	               
						          	         </c:if>
						          	      </c:forEach>
								         
							          </div>
						          </c:if>
						          
						          <!-- <spring:message code="shop.property.single.choose"/> -->
						           <c:if test="${dynamicPropertyCommand.property.editingType==3 }">
							          <label>${dynamicPropertyCommand.property.name }</label>
							           <div>        
					          	             <select style='width:160px;height:25px' name='iProperties.propertyValueId' mandatory='true'>
					          	                 <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
					          	                    <option value ='${propertyValue.id }'
					          	                    
					          	                       <c:forEach var="itemProperty" items="${itemProperties}">
							          	                    <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
							          	                    selected=selected
							          	                     </c:if>
							          	                </c:forEach>  
							          	                    
					          	                    >${propertyValue.value }</option>
					          	                 </c:forEach>
					          	             </select>
					          	             <input type='hidden' name='iProperties.propertyId' value='${dynamicPropertyCommand.property.id }'/>
					          	             <input type='hidden' name='iProperties.id' value=''/>
						                     <input type='hidden' name='iProperties.propertyDisplayValue' value=''/>
						                     <input type='hidden' name='iProperties.createTime' value=''/>
						                     <input type='hidden' name='iProperties.modifyTime' value=''/>
						                     <input type='hidden' name='iProperties.version' value=''/>
						                     <input type='hidden' name='iProperties.itemId' value=''/>
						                     <input type='hidden' name='iProperties.propertyValue.value[${notSalePropNum}]' value=''/>
						                     <input type='hidden' name='iProperties.picUrl' value=''/>
							          </div>
						          </c:if>
						           <!-- <spring:message code="shop.property.multiple.choose"/> -->
						           <c:if test="${dynamicPropertyCommand.property.editingType==4 }">
						             <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
							         <label>${dynamicPropertyCommand.property.name }</label>
							         
							         <%-- 属性值分组  start --%>
									 <c:if test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
										 <select  loxiaType="select" onchange="changeProGroup(this,2)"  propertyId='${dynamicPropertyCommand.property.id }'>
										 	<option value=""><spring:message code="item.update.proGroupSelect" /></option>
										 	<c:forEach var="propertyValueGroup"  items="${dynamicPropertyCommand.propertyValueGroupList}">
										 		<option value="${propertyValueGroup.id}">${propertyValueGroup.name}</option>
										 	</c:forEach>
										 </select>
									  </c:if>
									  <%-- 属性值分组  end --%>
									  
									  <div>
							          <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
								     	 <div class="priDiv">
									          	 <span class="children-store  normalCheckBoxSpan">
									          	 <input name="filtratecolor" tname="${propertyValue.value }" type='checkbox' class = 'normalCheckBoxCls' pid="${dynamicPropertyCommand.property.id }" mustCheck='${dynamicPropertyCommand.property.name }'  value='${propertyValue.id }' 
										          	   <c:forEach var="itemProperty" items="${itemProperties}">
										          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
										          	        checked=checked 
										          	      </c:if>
										          	   </c:forEach>
										          	  />${propertyValue.value }
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
											<c:forEach items="${ itemProperties }" var="itemProperty" varStatus="status">
											<c:if test="${ dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
												<c:set var="str" value="${str}${ itemProperty.propertyValue}||"></c:set>
											</c:if>
											</c:forEach>	
											
											<c:set var = "str" value = "${fn:substring(str,0,fn:length(str)-2) }"></c:set>
											<textarea class="customerSelect" loxiaType="input" name="iProperties.propertyValue.value[${notSalePropNum}]" isColorProp="false" editingtype='5'  propertyId=${dynamicPropertyCommand.property.id } style="width: 600px; height: 45px" >${str} </textarea>
											<input type="hidden" value='${dynamicPropertyCommand.property.id }' name="iProperties.propertyId"> 
											<input type="hidden" value="" name="iProperties.id">
											<input type="hidden" value="" name="iProperties.propertyDisplayValue">
											<input type="hidden" value="" name="iProperties.createTime">
											<input type="hidden" value="" name="iProperties.modifyTime">
											<input type="hidden" value="" name="iProperties.version">
											<input type="hidden" value="" name="iProperties.itemId">
											<input type="hidden" value="" name="iProperties.picUrl">
											<input type="hidden" value="" name="iProperties.propertyValueId">
											<span style="margin: 17px 0 0 10px; position: absolute;"><spring:message code="item.update.betweenPropertyName"/>'||'<spring:message code="item.update.split"/></span>

								         </div> 
						          </c:if>
						    </div>
						    
						    <c:if test="${dynamicPropertyCommand.property.editingType ne 4}">
						    	<c:set var="notSalePropNum" value="${notSalePropNum+1}"></c:set>
						    </c:if>
					    </c:if>
		           </c:forEach>
		            <c:set var="notSalePropSize" value="0"></c:set>
					<c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}" varStatus="status">
						<c:if test="${!dynamicPropertyCommand.property.isSaleProp }">
							<c:if test="${dynamicPropertyCommand.property.editingType==4 }">
					         	 <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
						          <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
							          	 <span class="hidBoxSpan">
							          	 	<div type='hidden' class ='repNormalCheckBoxCls' pid="${dynamicPropertyCommand.property.id }" pvid='${propertyValue.id }'></div>
							          	 </span>
							        </c:forEach>
							   </c:if>
					        </c:if>
					        <c:if test="${dynamicPropertyCommand.property.editingType ne 4 }">
					        	<c:set var="notSalePropSize" value="${notSalePropSize+1}"></c:set>
					        </c:if>
						</c:if>
					</c:forEach>
					<input type ="hidden" id="notSalePropSize" value="${notSalePropSize}"/>
			   </div>
			   </c:if>
			    
				
			   
		   </div>
		   
		<div style="margin-top: 10px"></div>
		
		<%-- 销售属性 --%>
	   <div class="ui-block-title1 ui-block-title"><spring:message code="item.update.itemprice" /></div>
	   <div class="ui-block-content border-grey">
	         <div class="ui-block-line">
		         <label ><spring:message code="item.update.salesprice" /></label>
		          <input id="salePriceValue" type="hidden" value="${ salePrice }" />
		          <div id="salePriceDiv"> 
				    <input   mandatory='true' loxiaType='number' decimal='2'style="width:160px;height:25px" id="salePrice" name="itemCommand.salePrice"  value="${ salePrice }"/>
		         </div>
		     </div>
		     <div class="ui-block-line ">
		         <label ><spring:message code="item.update.stickerprice" /></label>
		         <input id="listPriceValue" type="hidden" value="${ listPrice }" />
		          <div id="listPriceDiv">
			    	<input  loxiaType='number' decimal='2' style="width:160px;height:25px" id="listPrice" name="itemCommand.listPrice" value="${listPrice}"/>
		         </div>
		     </div>
	   	</div>
		<div style="margin-top: 10px"></div>
		<!-- 销售属性信息 多语言 -->
	    <div class="ui-block-title1 ui-block-title"><spring:message code="item.update.sales"/></div>
	    <c:if test="${i18nOnOff == true}">
	      <c:set var="size" value="${fn:length(dynamicPropertyCommandList)}"></c:set>
	      <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	      <c:set var="lang" value="${i18nLang.key}"></c:set>
	   	  <c:set value="${status.index}" var="i"></c:set>
	   	  <div class="ui-block-content border-grey saleInfo" lang="${i18nLang.key}"  <c:if test="${i>0 }"> style="margin-top: 5px;" </c:if> >
	      <div id="propertySpace">
	      <c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}">
	          <c:if test="${dynamicPropertyCommand.property.isSaleProp }">
			        <div class="ui-block-line ">
			        <c:if test="${ dynamicPropertyCommand.property.editingType == 4 && lang == defaultlang}">
				        <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
					         <label>${dynamicPropertyCommand.property.name }</label>
					         <input type='hidden' value='${dynamicPropertyCommand.property.id }'  name = 'propertyIds' />
							 <input type='hidden' value='' name='propertyValueInputIds' pid='${dynamicPropertyCommand.property.id}' />
						
						  	 <%-- 属性值分组  start --%>
							 <c:if test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
								 <select  loxiaType="select" onchange="changeProGroup(this,1)"  propertyId='${dynamicPropertyCommand.property.id }'>
								 	<option value=""><spring:message code="item.update.proGroupSelect" /></option>
								 	<c:forEach var="propertyValueGroup"  items="${dynamicPropertyCommand.propertyValueGroupList}">
								 		<option value="${propertyValueGroup.id}">${propertyValueGroup.name}</option>
								 	</c:forEach>
								 </select>
							  </c:if>
							  <%-- 属性值分组  end --%>
							  
							    <div>
						          <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
							     	 <div class="priDiv">
								          	 <span class="children-store">
									          	 <input type="checkbox" class='spCkb' name="propertyValueIds"  
									          	 pvId="${propertyValue.id }"
									          	 pvValue="${propertyValue.value }"
									          	 propertyId="${dynamicPropertyCommand.property.id }" 
									          	 propertyName ="${dynamicPropertyCommand.property.name }"
									          	 value="${propertyValue.id }"
									          	   <c:forEach var="itemProperty" items="${itemProperties}">
									          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
									          	        checked=checked 
									          	      </c:if>
									          	   </c:forEach>
									          	 
									          	  />
									          	  <c:if test="${dynamicPropertyCommand.property.isColorProp && dynamicPropertyCommand.property.hasThumb }">
								             	      <img src="${base}/images/1.png">
								                  </c:if>
									          	  ${propertyValue.value }
								          	 </span> 
							         </div>
						          </c:forEach>
						        </div>
					      	 </c:if>
				      	 </c:if>
				         <c:if test="${ dynamicPropertyCommand.property.editingType == 5 }">
				         	  <label>${dynamicPropertyCommand.property.name }</label>
				         	  <c:if test="${lang == defaultlang}">
				         	  <input type='hidden' value='${dynamicPropertyCommand.property.id }'  name = 'propertyIds' />
							   </c:if>
							  <input type='hidden' value='' class="propertyValueInputs" name='propertyValueInputs' pid='${dynamicPropertyCommand.property.id}' />
							  <input type='hidden' value='${lang}'  name='propertyValueInputs'/>
					     	  <div class="priDiv">
					     	   
					     	  <c:set var="str" value=""></c:set> 
								<c:forEach items="${itemProperties}" var="itemProperty" varStatus="status">
									<c:if test="${ dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
										<c:forEach items="${itemProperty.propertyValue.langs}" var="l">
										 <c:set value="${status.index}" var="j"></c:set>
										<c:if test="${lang == l}">
										<c:set var="str" value="${str}${itemProperty.propertyValue.values[i] }||"></c:set>
										</c:if>
										</c:forEach>
									</c:if>
								</c:forEach>	
											
								<c:set var = "str" value = "${fn:substring(str,0,fn:length(str)-2) }"></c:set>
					     	 	<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingtype='5' isColorProp="true"  propertyId=${dynamicPropertyCommand.property.id } style="width: 600px; height: 45px" mandatory ="true">${str}</textarea>
					     	 	<span style="margin: 17px 0 0 10px; position: absolute;">${i18nLang.value}&nbsp;&nbsp;<spring:message code="item.update.betweenPropertyName"/>'||'<spring:message code="item.update.split"/></span>

					          </div>
				       </c:if>
				    </div>
			    </c:if>
		    </c:forEach>
	    </div>
	   
         <div id="exten" class="ui-block-line exten" style="display:none;">
          <div  id="extension">
             <table id="extensionTable" class="border-grey extensionTable" style="padding:5px;">
               
              </table>
         	</div>
         </div>
	  
	    <div class="ui-block-line ">
	    	<label></label>
           <div style="clear:both;">
              	<a href="javascript:void(0)" class="func-button extension" style="height:30px;"><span><spring:message code="item.update.encodingSettings"/></span></a>
             
         </div>
	    </div>
	  </div>
	  </c:forEach>
	   </c:if>
	   
	   <!--销售属性信息 单语言-->
	   <c:if test="${i18nOnOff == false}">
	    <div class="ui-block-content border-grey saleInfo">
	   <div id="propertySpace">
	      <c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}">
	          <c:if test="${dynamicPropertyCommand.property.isSaleProp }">
			        <div class="ui-block-line ">
				    <c:if test="${ dynamicPropertyCommand.property.editingType == 4 }">
			         <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
				         <label>${dynamicPropertyCommand.property.name }</label>
				         <input type='hidden' value='${dynamicPropertyCommand.property.id }'  name = 'propertyIds' />
						 <input type='hidden' value='' name='propertyValueInputIds' pid='${dynamicPropertyCommand.property.id}' />
							 <c:if test="${fn:length(dynamicPropertyCommand.propertyValueGroupList)>0}">
								 <select  loxiaType="select" onchange="changeProGroup(this,1)"  propertyId='${dynamicPropertyCommand.property.id }'>
								 	<option value=""><spring:message code="item.update.proGroupSelect" /></option>
								 	<c:forEach var="propertyValueGroup"  items="${dynamicPropertyCommand.propertyValueGroupList}">
								 		<option value="${propertyValueGroup.id}">${propertyValueGroup.name}</option>
								 	</c:forEach>
								 </select>
							  </c:if>
							 
							  <div>
					          <c:forEach var="propertyValue" items="${dynamicPropertyCommand.propertyValueList}">
						     	 <div class="priDiv">
							          	 <span class="children-store">
								          	 <input type="checkbox" class='spCkb' name="propertyValueIds"  
								          	 pvId="${propertyValue.id }"
								          	 pvValue="${propertyValue.value }"
								          	 propertyId="${dynamicPropertyCommand.property.id }" 
								          	 propertyName ="${dynamicPropertyCommand.property.name }"
								          	 value="${propertyValue.id }"
								          	   <c:forEach var="itemProperty" items="${itemProperties}">
								          	     <c:out value="${itemProperty.propertyValueId}"></c:out>
								          	      <c:if test="${itemProperty.propertyValueId==propertyValue.id }">
								          	        checked=checked 
								          	      </c:if>
								          	   </c:forEach>
								          	 
								          	  />
								          	  <c:if test="${dynamicPropertyCommand.property.isColorProp && dynamicPropertyCommand.property.hasThumb }">
							             	      <img src="${base}/images/1.png">
							                  </c:if>
								          	  ${propertyValue.value }
							          	 </span> 
						         </div>
					        </c:forEach>
					        </div>
				      	 </c:if>
					   </c:if>
				         <c:if test="${ dynamicPropertyCommand.property.editingType == 5 }">
				         	  <label>${dynamicPropertyCommand.property.name }</label>
				         	  <input type='hidden' value='${dynamicPropertyCommand.property.id }'  name = 'propertyIds' />
							  <input type='hidden' value='' name='propertyValueInputs' pid='${dynamicPropertyCommand.property.id}' />
					     	  <div class="priDiv">
					     	  <c:set var="str" value=""></c:set> 
								<c:forEach items="${ itemProperties }" var="itemProperty" varStatus="status">
									<c:if test="${ dynamicPropertyCommand.property.id ==  itemProperty.propertyId}">
										<c:set var="str" value="${str}${ itemProperty.propertyValue.value }||"></c:set>   
									</c:if>
								</c:forEach>	
											
								<c:set var = "str" value = "${fn:substring(str,0,fn:length(str)-2) }"></c:set>
						     	 	<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingtype='5' isColorProp="true"  propertyId=${dynamicPropertyCommand.property.id } style="width: 600px; height: 45px" mandatory ="true">${str}</textarea>
						     	 	<span style="margin: 17px 0 0 10px; position: absolute;"><spring:message code="item.update.betweenPropertyName"/>'||'<spring:message code="item.update.split"/></span>

					          </div>
				       </c:if>
				    </div>
			    </c:if>
		    </c:forEach>
	    </div>
         <div id="exten" class="ui-block-line exten" style="display:none;">
          <div  id="extension">
             <table id="extensionTable" class="border-grey extensionTable" style="padding:5px;">
               
              </table>
         	</div>
         </div>
	    <div class="ui-block-line ">
	    	<label></label>
           <div style="clear:both;">
              	<a href="javascript:void(0)" class="func-button extension" style="height:30px;"><span><spring:message code="item.update.encodingSettings"/></span></a>
         </div>
	   </div>
	  </div>
	   </c:if>
	   
	  <%-- SEO --%>
	  <div style="margin-top: 10px"></div>
		
	   <div class="ui-block-title1 ui-block-title">seo<spring:message code="product.property.lable.search"/></div>
	
	   <div class="ui-block-content border-grey">
	   	  <c:if test="${i18nOnOff == true}">
	   	  <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	   	  <c:set value="${status.index}" var="i"></c:set>
		  <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoTitle"/></label>
	         <c:if test="${not empty seoTitle }">
	         <input loxiaType="input" style='width: 600px' name="itemCommand.seoTitle.values[${status.index}]" value="${ seoTitle.langValues[i18nLang.key] }" />
	         </c:if>
	          <c:if test="${empty seoTitle }">
	          <input loxiaType="input" style='width: 600px' name="itemCommand.seoTitle.values[${status.index}]" value="" />
	         </c:if>
			<input class="i18n-lang" type="text" name="itemCommand.seoTitle.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>			
	      </div>
	      </c:forEach>
	       <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	       <c:set value="${status.index}" var="i"></c:set>
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoKeywords"/></label>
	        <c:if test="${not empty seoKeywords }">
	        <input loxiaType="input" name="itemCommand.seoKeywords.values[${status.index}]" value="${ seoKeywords.langValues[i18nLang.key] }" style="width:600px" />
	        </c:if>
	        <c:if test="${empty seoKeywords }">
	        <input loxiaType="input" name="itemCommand.seoKeywords.values[${status.index}]" value="" style="width:600px" />
	        </c:if>
			
			<input class="i18n-lang" type="text" name="itemCommand.seoKeywords.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>				
	      </div>
	       </c:forEach>
	      <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	      <c:set value="${status.index}" var="i"></c:set>
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoDesc"/></label>
	          <c:if test="${not empty seoDescription }">
	           <textarea rows="10px" name="itemCommand.seoDescription.values[${status.index}]" loxiaType="input" style="width: 600px;">${ seoDescription.langValues[i18nLang.key] }</textarea>
	          </c:if>
	           <c:if test="${empty seoDescription }">
	            <textarea rows="10px" name="itemCommand.seoDescription.values[${status.index}]" loxiaType="input" style="width: 600px;"></textarea>
	          </c:if>
	        <input class="i18n-lang" type="text" name="itemCommand.seoDescription.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>		
	      </div>
	      </c:forEach>
	      
	      </c:if>
	      
	      <c:if test="${i18nOnOff == false}">
		  <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoTitle"/></label>
			<input loxiaType="input" style='width: 600px' name="itemCommand.seoTitle.value" value="${seoTitle.value}" />			
	      </div>
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoKeywords"/></label>
			<input loxiaType="input" name="itemCommand.seoKeywords.value" value="${ seoKeywords.value }" style="width:600px" />			
	      </div>
	      <div class="ui-block-line ">
	        <label style="">seo<spring:message code="item.update.seoDesc"/></label>
	        <textarea rows="10px" name="itemCommand.seoDescription.value" loxiaType="input" style="width: 600px;">${ seoDescription.value }</textarea>
	      </div>
	      </c:if>
	   </div>
	   
	  <div style="margin-top: 10px"></div>
		
	   <div class="ui-block-title1 ui-block-title"><spring:message code="item.update.description"/></div>
	
	   <div class="ui-block-content border-grey">
	   
	   
	   <c:if test="${i18nOnOff == true}">
	    <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	    <c:set value="${status.index}" var="i"></c:set>
		 <div class="ui-block-line ">
	        <label style=""><spring:message code="item.update.sketch"/></label>
	        <c:if test="${not empty sketch }">
	        	<textarea rows="10px" name="itemCommand.sketch.values[${status.index}]" loxiaType="input" style="width: 600px;">${sketch.langValues[i18nLang.key]}</textarea>
			</c:if>
			 <c:if test="${empty sketch}">
	        	<textarea rows="10px" name="itemCommand.sketch.values[${status.index}]" loxiaType="input" style="width: 600px;"></textarea>
			</c:if>
	     	<input class="i18n-lang" type="text" name="itemCommand.sketch.langs[${status.index}]" value="${i18nLang.key}" />
        	<span>${i18nLang.value}</span>		
	     </div>
	     </c:forEach>
	  	<c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
	  	<c:set value="${status.index}" var="i"></c:set>
	     <div class="ui-block-line ">
        <label style=""><spring:message code="item.update.lable.description"/></label>
          <div style="float: left;margin-left:0px">
           <c:if test="${not empty description }">
              <textarea id="editor${status.index}" name="itemCommand.description.values[${status.index}]" rows="20" cols="120">
				${description.langValues[i18nLang.key]}
			 </textarea>
          </c:if>
          <c:if test="${empty description }">
               <textarea id="editor${status.index}" name="itemCommand.description.values[${status.index}]" rows="20" cols="120">
		 		</textarea>
           </c:if>
       
		  <input class="i18n-lang" type="text" name="itemCommand.description.langs[${status.index}]" value="${i18nLang.key}" />
		 </div> 
		  <span style="display: block; float: left;" >${i18nLang.value}</span>
          </div>
	    </c:forEach> 
	 </c:if>
	     
	     
     <c:if test="${i18nOnOff == false}">
	  <div class="ui-block-line ">
        <label style=""><spring:message code="item.update.sketch"/></label>
           <div>
				<textarea rows="10px" name="itemCommand.sketch.value" loxiaType="input" style="width: 600px;">${ sketch.value }</textarea>			
           </div>
    	</div>

	   <div class="ui-block-line ">
        <label style=""><spring:message code="item.update.lable.description"/></label>
          
           <div>
<%--          		<textarea id="editor" name="itemCommand.description.value" rows="20" cols="120">${description.value}</textarea> --%>
            <textarea id="" name="itemCommand.description.value" rows="20" style="width: 600px;" class="ui-loxia-default ui-corner-all">${description.value}</textarea>
          </div>
    	</div>
    	</c:if>
    	
    	
	  </div>

	 <div class="button-line">
	         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
	         <input type="button" value="<spring:message code='btn.image'/>" class="button orange imageManage" title="<spring:message code='btn.image'/>"/>
	         <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />
	</div>
	
	<div style="margin-top: 20px"></div>
	</div>
	<input type="hidden" loxiaType="input" name="thumbnailConfig" id="thumbnailConfig" value="${thumbnailConfig[0].optionValue }" />
   </form>    
</div>
</div>

<div id="menuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:auto; height: 100%;"></ul>
</div>
</body>
</html>
