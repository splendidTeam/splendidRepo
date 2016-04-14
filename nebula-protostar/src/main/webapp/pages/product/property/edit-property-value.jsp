<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/property/edit-property-value.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/jqueryplugin/jquery.blockUI.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/sortgrid/jquery.ui.core.min.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/sortgrid/jquery.ui.widget.min.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/sortgrid/jquery.ui.mouse.min.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/sortgrid/jquery.ui.sortable.min.js"></script>
<style type="text/css">
.i18n-lang{
	display: none;
}
.lang_lable{
width: 15%;
display:block;
float: left;
text-align: left;
padding-left: 3px;
}
tbody input[type="text"]{
	float: left;
	width:85%;
}

</style>
</head>

<body>

<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/wrench.png"><spring:message code="system.property.manager"/> --【${property.name }】
			
			<input type="button" value="<spring:message code='shop.property.value.sort'/>" class="button orange propertyValueSort" title="<spring:message code='shop.property.value.sort'/>"/>
		</div>
		<input type="hidden" id="propertyId" value="${property.id}" />
		<div class="ui-block ui-block-fleft" style="width: 400px;">
			<div class="ui-block-content ui-block-content-lb">
  				<table >
			        <tr>
			            <td>
			            	<label><spring:message code='shop.property.group'/></label>
			            </td>
			            <td>
			             <form id="searchPropertyValueForm" action="" method="">
			                <span >
			                	<select name="proValueGroupId" loxiaType="select"  mandatory="true" class="orgtype selectedGroupId" > 
			                		<option value=""> ---请选择属性值组---</option>
									<c:forEach items="${propertyValueGroupList}" var="group">
										<option value="${group.id}">${group.name}</option>
									</c:forEach>
								</select>
			                </span>
			                <input type="hidden" name="propertyId" value="${property.id}"  />
			              </form>
			            </td>
			            <td>
			                <span ><a href="javascript:void(0);" class="func-button addPropertyValueGroup"   title="<spring:message code='btn.add'/>">
			                		<span><spring:message code="btn.add"/></span></a>
			                </span>
			            </td>
			            <td>
			            	<c:if test="${not empty propertyValueGroupList }">
				                <span >
				                	<a href="javascript:void(0);" class="func-button updatePropertyValueGroup"   title="<spring:message code='btn.update'/>">
				                		<span><spring:message code="btn.update"/></span></a>
				                </span>
			                </c:if>
			            </td>
			        </tr>
			    </table>
			</div>
			<div class="table-border0 border-grey" id="table2" caption="<spring:message code='shop.property.value.list'/>"></div>
		</div>
		 
		<div class="ui-block " style="margin-left:400px;width:auto;padding-left: 10px;" >
			<div class="ui-block-title1">属性值操作</div>
			<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
				<div class="ui-block-line">
					<label>属性值：</label>
					<div>
						<form name="propertyForm" action="/i18n/property/addOrUpdatePropertyValue.json" method="POST">
							<input type="hidden" name="groupId" id="groupId" value="" />
						
							<input type="hidden" name="propertyValues.id" value=""/>
							<input type="hidden" name="propertyValues.propertyId" value="${property.id}" class="propertyValues-propertyId" />
							<input type="hidden" name="propertyValues.sortNo" value="">
							
						 <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
						   <div class="ui-block-line propertyValueInput">
					         
							<input id="input3" 
								name="propertyValues.value.values[${status.index}]" placeholder="<spring:message code='system.propertyvalue'/>"
							  value=""  style='width: 200px' mandatory="true" type="text" class="name" loxiaType="input" trim="true"/>
							
							<input name="propertyValues.value.langs[${status.index}]"  value="${i18nLang.key}" 
							 	mandatory="true" class="i18n-lang" type="text" loxiaType="input" trim="true" />
						   
						   
						   	<span>${i18nLang.value}</span>		 
						  
						   </div>
						   
						   
						 </c:forEach>
						</form>
					</div>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button refreshPropertyValue" title="<spring:message code='refresh'/>">
    				 		<span><spring:message code="refresh"/></span>
    				</a>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button savePropertyValue" title="<spring:message code='btn.save'/>">
    				 		<span><spring:message code="btn.save"/></span>
    				</a>
    				<a href="javascript:void(0);" class="func-button savePropertyValue continue" title="<spring:message code='shop.property.value.save.continue'/>">
    				 		<span><spring:message code="shop.property.value.save.continue"/></span>
    				</a> 		
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" id="downLoadTmplOfPropertyValue" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.export'/>">
    				 		<span><spring:message code="shop.property.value.export"/></span></a>
    				<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.import'/>">
    				 		<span><spring:message code="shop.property.value.import"/></span></a>
				</div>
				
			</div>
			 
     <div class="button-line">
		<input type="button" value="<spring:message code='shop.property.value.sort'/>" class="button orange propertyValueSort" title="<spring:message code='shop.property.value.sort'/>"/>
    </div>
		</div>
	</div>
	
    <!-- 评价详细  dialog-->
    <div id="detail-dialog" class="proto-dialog">
		 <h5>属性值排序</h5>
		 <div class="proto-dialog-content p10" id="sortable">	

			
			
			
		 </div>
		 
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="提交回复" class="button orange copyok"/>
		 	  <input type="button" value="取消" class="button black copycancel"/>
		 </div>
	</div>
</body>
</html>
