<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/property/shop-property-value.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>

</head>

<body>
<div class="content-box width-percent100">
<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="shop.add.shopmanager"/>	 
</div>
<div class="ui-block">
        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
            <table>
                <tr>
                    <td><label><spring:message code='shop.property.lable.shopname'/></label></td>
                    <td><span> ${shopName}</span></td>
                    <td><label><spring:message code='shop.property.lable.industry'/></label></td>
                    <td><span>${industryName}</span></td>
                    <td><label><spring:message code='shop.property.lable.property'/></label></td>
                    <td><span>${propertyName}</span></td>
                </tr>
            </table>
        </div>
    </div>
<div class="ui-block-title1"><spring:message code="system.propertyvalue.optional"/></div>
 <div class="ui-block-content border-grey">
 <form name="propertyForm" action="/shop/savePropertyValue.json" method="post">
<div id="edittable" loxiaType="edittable" >
<table id="propertyTable" operation="add,delete" append="1" width="2000" class="inform-person">
	 <thead>
		<tr>
			<th width="5%"><input type="checkbox"/></th>
			<th width="15%"><spring:message code="system.propertyvalue"/></th>
			<%-- <th width="15%"><spring:message code="system.propertyshowvalue"/></th> --%>
			<th width="10%"><spring:message code="system.propertyvalue.thumb"/></th>
			<th width="8%"></th>
			<th width="8%"></th>
		</tr>
	</thead>
    
   
     	 <tbody>
	     	<c:if test="${ propertyValue != '[]' }">
		     		<c:forEach var="item" items="${propertyValue}" varStatus="index">
						<tr>
							<td width="5%"><input type="checkbox"/>
								<input type="hidden" name="propertyValues.id" value="${item.id}"/>
								<input type="hidden" name="propertyValues.propertyId" value="${item.propertyId}"/>
								<input type="hidden" name="propertyValues.sortNo" value="${index.count}"/>
							</td>
							<td width="15%"><input type="text" loxiaType="input" id="input3" name="propertyValues.value" style="width:95%" value="${item.value}"/></td>
							<%-- <td width="15%"><input type="text" loxiaType="input" id="showValue" name="propertyValues.showValue" style="width:95%" mandatory="true" value="${ item.showValue }" /></td> --%>
							<td width="15%">
								<input type="hidden" name="propertyValues.thumb" value="${ item.thumb }"/>
								<input complete="propertyImageComplete" class="imgUploadComponet" role="32X32" model="C" hName="uploadPropertyImage${item.id}" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>
							</td>
							<td width="8%" style="text-align: left;">
								<div id="imguploadPropertyImage${item.id}">
									<c:if test="${item.thumb != '' && item.thumb != null}"><img src="${item.thumb}" alt="${item.value}" title="${item.value}"  width="32px" height="32px" /></c:if>
									<c:if test="${item.thumb == '' || item.thumb == null}"><img src="../images/main/mrimg.jpg"   width="32px" height="32px" /></c:if>
								</div>
								<td width="8%" style="text-align: left;">
									<c:if test="${ !index.first }">
										<a href="javascript:void(0)" class="arrow_up"><img src="${ base }/images/wmi/blacks/16x16/arrow_top.png" /></a>
									</c:if>
									<a href="javascript:void(0)" class="arrow_down ${ index.first?'first':'' }"><img src="${ base }/images/wmi/blacks/16x16/arrow_bottom.png" /></a>
									
								</td>
							</td>
						</tr>
					</c:forEach>
	     	</c:if>
		</tbody>
		<tbody>
	  		<tr>
				<td width="5%"><input type="checkbox"/>
					<input type="hidden" name="propertyValues.id" value=""/>
					<input type="hidden" name="propertyValues.propertyId" value=""/>
					<input type="hidden" name="propertyValues.sortNo" value="1"> 
				</td>
				<td width="15%"><input type="text" loxiaType="input" trim="true" id="input3" name="propertyValues.value" style="width:95%"  value=""/></td>
				<%-- <td width="15%"><input type="text" loxiaType="input" trim="true" id="" style="width:95%" mandatory="true" name="propertyValues.showValue" value=""/></td> --%>
				<td width="15%">
					<input type="hidden" name="propertyValues.thumb" value="../images/main/mrimg.jpg"/>
					<input complete="propertyImageComplete" class="imgUploadComponet" role="32X32" model="C" hName="uploadPropertyImage" hValue="../images/main/mrimg.jpg" type="file" url="/demo/upload.json"/>
				</td>
				<td width="8%" style="text-align: left;">
					<div id="imguploadPropertyImage">
						<img src="../images/main/mrimg.jpg"   width="32px" height="32px" />
					</div>
				</td>
				<td width="8%" style="text-align: left;">
					<c:if test="${ propertyValue != '[]'}">
						<a href="javascript:void(0)" class="arrow_up"><img src="${ base }/images/wmi/blacks/16x16/arrow_top.png" /></a>
					</c:if> 
				</td>
			</tr>
		</tbody>
	<tfoot>
		<tr>
			<td colspan="2" >&nbsp;</td><td></td>
		</tr>
	</tfoot>

	<div class="button-line">
	        <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" /> 
	      	<input type="button" value="<spring:message code='btn.return'/>" class="button goback"/>	         
    </div>
</table>
</div>
</div>
</div>
</form>
</body>
</html>
