<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/property/nebula-product-propert.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


</head>
<body>

	<div class="content-box">
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/spechbubble.png"><spring:message code="product.property.list.title" />
			
			<input type="button"
				value="<spring:message code="product.property.button.delete"/>"
				class="button delete" title="<spring:message code="product.property.button.delete"/>"/>
				
			<input type="button"
				value="<spring:message code="product.property.button.insert"/>"
				class="button orange addpro" title="<spring:message code="product.property.button.insert"/>"/>
		</div>
		
		<div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
		
		<form action="/property/propertyList.json" id="searchForm">
		
				<table>
					<tr>
						<td><label><spring:message code="product.property.lable.name" /></label></td>
									
						<td>
							<span id="searchkeytext"> 
								<input type="text" loxiaType="input" mandatory="false" id="name" name="q_sl_name" placeholder="<spring:message code="product.property.lable.name" />"></input>
							</span>
						</td>
						
						
						<%-- <td><label><spring:message code="product.property.lable.industry" /></label></td>
									
						<td><span id="searchkeytext"> <input type="hidden"
								id="industryId" name="q_long_industryId" mandatory="true" /> <input 
								type="text" loxiaType="input" name="industryName" id="industryName"
								mandatory="false" placeholder="<spring:message code="shop.property.industry" />"/>
						</span></td> --%>


						<td><label><spring:message code="product.property.lable.lifecycle" /></label></td>
						<td> <span id="searchkeytext"> <opt:select name="q_int_lifecycle" loxiaType="select" id="lifecycle" expression="chooseOption.IS_AVAILABLE" nullOption="role.list.label.unlimit"  />
						</span> </td>
						
						<td><label><spring:message code="product.property.editingType" /></label></td>
						<td>
							<span id="searchkeytext">
								<opt:select name="q_int_editingType" loxiaType="select" id="editingType" expression="chooseOption.EDITING_TYPE" nullOption="role.list.label.unlimit"  />
							</span>
						</td>
						
					</tr>
				</table>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button search"><span><spring:message
										code="product.property.lable.search" /></span></a>
				</div>
		</form>

			</div>
		</div>

		<div class="ui-block">		
			<div class="border-grey" id="table1" caption="<spring:message code="product.property.lable.list" />"></div>
		</div>

		<div class="button-line">
			<input type="button" value="<spring:message code="product.property.button.insert"/>" class="button orange addpro" title="<spring:message code="product.property.button.insert"/>" />
			<input type="button" value="<spring:message code="product.property.button.delete"/>" class="button delete" title="<spring:message code="product.property.button.delete"/>" />
		</div>
	</div>
	<div id="menuContent" class="menuContent" style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="treeDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>
</body>
</html>