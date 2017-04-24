<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base }/scripts/freight/shipping/shipping-temeplate-create.js"></script>

</head>

<body>

<div class="content-box width-percent100">
    
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/cur_yen.png">
		运费模板
	</div>
	
<div class="ui-block">	
	<div class="ui-block-title1">运费模板</div>
			<div class="ui-block-content border-grey">
		    <input type="text" value="${shippingTemplate.calculationType}" style="display: none;" id="templateType"/>
		    <div class="ui-block-line ">
		         <label>模板名称</label>
		         <div>
		         	<input type="hidden" id="shipping_id"  value="${shippingTemplate.id }" />
		              <input type="text" loxiaType="input" value="${shippingTemplate.name }" name="name" mandatory="true" id="template-name"/>
		         </div>
		         <div></div>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>计价类型</label>
		         <div>
           			<select loxiaType="select" mandatory="true" name="calculationType" id="calculationType">
           			<c:choose> 
           			 <c:when test="${shippingTemplate.calculationType eq 'base'}">
					<option value="base">基础</option>
					<option value="weight">计重</option>
					<option value="unit">计件</option>
					<option value="custom">自定义</option>
           			 </c:when>
           			 <c:when test="${shippingTemplate.calculationType eq 'unit'}">
					<option value="unit">计件</option>
					<option value="base">基础</option>
					<option value="weight">计重</option>
					<option value="custom">自定义</option>
					
           			 </c:when>
           			 <c:when test="${shippingTemplate.calculationType eq 'weight'}">
					<option value="weight">计重</option>
           			 <option value="base">基础</option>
					<option value="unit">计件</option>
					<option value="custom">自定义</option>
           			 </c:when>
           			 <c:when test="${shippingTemplate.calculationType eq 'custom'}">
					<option value="custom">自定义</option>
					<option value="weight">计重</option>
           			 <option value="base">基础</option>
					<option value="unit">计件</option>
           			 </c:when>
           			 <c:otherwise>
					<option value="base">基础</option>
					<option value="weight">计重</option>
					<option value="unit">计件</option>
					<option value="custom">自定义</option>
           			 </c:otherwise>
           			 </c:choose> 
                </select>
		           </div>	
		    </div>
		    
		    <div class="ui-block-line color-select-line" id="beanNameDiv" style="display: block;">
		         <label>自定义类名</label>
		         <div>
		         <input type="text" loxiaType="input" value="${shippingTemplate.beanName }" name="beanName" mandatory="true" id="beanName"/>
		         </div>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>默认邮费</label>
		         <div>
		         <input type="text" loxiaType="number" value="${shippingTemplate.defaultFee }" mandatory="true" id="defaultFee"/>
		         </div>
		          <label>请填写数字</label>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>默认模板</label>
		         <div>
		         
		         <select loxiaType="select" mandatory="false" id="isDefault" value="${shippingTemplate['default'] }">
		         <c:if test="${shippingTemplate['default'] }">
					<option value="true">是</option>
					<option value="false">否</option>
		         </c:if>
		         <c:if test="${!shippingTemplate['default'] }">
					<option value="false">否</option>
					<option value="true">是</option>
		         </c:if>
                </select>
		         </div>
		    </div>
		    
		      <div class="ui-block-line dbm">
		         <label>物流方式</label>
		         <div class="wl-right" >
		         	 <c:forEach items="${dbms}" var="dbm" varStatus="status">
		         	 <div style="float: left;">
		         	 	 <c:if test="${dbm.selected}">
							 <input class="dbm-ids" type="checkbox" checked="checked" value="${dbm.id}" title="${dbm.name}">${dbm.name}
						 </c:if>
						  <c:if test="${dbm.selected == false}">
							<input class="dbm-ids" type="checkbox"  value="${dbm.id}" title="${dbm.name}">${dbm.name}
					 	</c:if>
						
					 </div>
					 <c:if test="${status.index!=0 && status.index%4==0}">
							 <div class="clear-line"></div>
					 </c:if>
					</c:forEach>
		         </div>
		    </div>
		 <%--    <c:if test="${!empty distributionModeList }">
		    <div class="ui-block-line color-select-line">
		         <label>已支持的物流</label>
			<c:forEach items="${distributionModeList }" var="mode">
				<span >
				${mode.name }&nbsp;
				
				</span>
			</c:forEach>
		    </div>
			</c:if> --%>
			
			</div>
	</div>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit template-save" />
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />

    </div>
</div>


</body>
</html>
