<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${ base }/scripts/product/item/item-store.js"></script>
<script type="text/javascript" src="${ base }/scripts/ajaxfileupload.js"></script>
<SCRIPT type="text/javascript">	
         
var skuList=<c:out value="${skuList}" default="[]" escapeXml="false"></c:out>;
var dynamicPropertyCommandListJsonStr=<c:out value="${dynamicPropertyCommandListJsonStr}" default="[]" escapeXml="false"></c:out>;
var itemPropertiesStr = <c:out value="${itemPropertiesStr}" default = "[]" escapeXml="false"></c:out>;
var storeNumStr=<c:out value="${storeNumStr}" default="[]" escapeXml="false"></c:out>;
</SCRIPT>
<script type="text/javascript">
var salesOfPropertyIsNotRequired = "${salesOfPropertyIsNotRequired}";
</script>
</head>
<body>

<div class="content-box width-percent100">
    
   <form name="itemForm" action="/item/saveSkuInventory.json" method="post">
    <input type="hidden" id="industryId" name="industryId"  value=""/>
    <input type="hidden" id="jsonSku" name="jsonSku"  value=""/>
    <input type="hidden" name="id" id="itemid" value="${id }"/>
    <input type="hidden" id="propertyIdArray" value="${propertyIdArray }"/>
    <input type="hidden" id="propertyNameArray" value="${propertyNameArray }"/>
    <input type="hidden" id="mustCheckArray" value="${mustCheckArray }"/>
   
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.store.manager"/></div>
	<!-- <div class="ui-block"> -->
	<div id="second" >
	<!-- 已选行业 -->
		<div class="ui-block"> 
	        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
		    	<table>
		        	<tr>
		                <td><label><spring:message code="item.code"/></label></td>
		                <td><span>${code}</span></td>
		                <td><label><spring:message code="item.name"/></label></td>
		                <td><span>${title}</span></td>
		            </tr>
		        </table>
	        </div>
	    </div>
	
		<div class="ui-block ">	
		   <div class="ui-block-title1"><spring:message code="item.store.Information"/></div>		   
		</div>
	   	
	    <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="item.update.sales"/></div>

	    <div id="propertySpace">
	      <c:forEach var="dynamicPropertyCommand" items="${dynamicPropertyCommandList}">
	          <c:if test="${dynamicPropertyCommand.property.isSaleProp }">
			        <div class="ui-block-line " style="display: none;">
				    <c:if test="${ dynamicPropertyCommand.property.editingType == 4 }">
			         <c:if test="${not empty dynamicPropertyCommand.propertyValueList}">
				         <%-- <label>${dynamicPropertyCommand.property.name }</label> --%>
				         <input type='hidden' value='${dynamicPropertyCommand.property.id }'  name = 'propertyIds' />
						 <input type='hidden' value='' name='propertyValueInputs' pid='${dynamicPropertyCommand.property.id}' />
					
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
										<c:set var="str" value="${str}${ itemProperty.propertyValue }||"></c:set>   
									</c:if>
								</c:forEach>	
											
								<c:set var = "str" value = "${fn:substring(str,0,fn:length(str)-2) }"></c:set>
						     	 	<textarea class="customerSelect spTa" loxiaType="input" name="propertyValue" editingtype='5' isColorProp="true"  propertyId=${dynamicPropertyCommand.property.id } style="width: 600px; height: 45px" mandatory ="true">${str}</textarea>


					          </div>
				       </c:if>
				    </div>
			    </c:if>
		    </c:forEach>
	    </div>
	   
	   <input type="hidden" id="ableQty" name="ableQty" value="">
         <div id="exten" class="ui-block-line  " >
          <div  id="extension">
             <table id="extensionTable" class="border-grey" style="padding:5px;">
               
              </table>
         </div>
         </div>
	  
	   
	    <div class="ui-block-line ">
	    	<label></label>
           <div style="clear:both;">          
           </div>
	    </div>
	  </div>
	  
	 <div class="button-line">
	         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
	         <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" />        
	</div>
	 <div style="margin-top: 20px"></div>
   </form>    

</div>

<!-- <div id="defaultMenuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="defaultCategoryTree" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
</div>
<div id="menuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
</div> -->

</body>
</html>