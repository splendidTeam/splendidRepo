<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp"%>

<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript"
	src="${base }/scripts/promotion/promotion-information.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

	<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="${base }/images/wmi/blacks/32x32/calc.png">促销活动信息管理
		</div>
	
		<div class="ui-block ">
		
		<div class="ui-block-title1">编辑内容</div>

		<div class="ui-block-line ">
			<label>对应列表页 </label>
			<div>
				<select name="categoryCode" id="categoryCode" loxiaType="select">
					<option value="">-- 请选择 --</option>
					<c:forEach items="${promotionList}" var="category">
						<option value="${category.code}">${category.name }</option>
					</c:forEach>

				</select>
			</div>
			<div id="loxiaTip-r" class="loxiaTip-r" style="display: none">
				<div class="arrow"></div>
				<div class="inner ui-corner-all codetip"
					style="padding: .3em .7em; width: auto;"></div>
			</div>
		</div>
					
					
			<div class="ui-block-content border-grey">
				<div class="ui-block-line ">
					<label style="">内容描述</label> <input type="hidden" id="msgId"
						value="">
				 	<div>
				       <%-- <jsp:include page="/pages/utiles/ckeditor.jsp"> --%>
				      
						<jsp:include page="/pages/utiles/ckeditor.jsp"></jsp:include>
					
					</div>
				</div>
			</div>

		
		</div>

	</div>
	
			<div class="button-line">
							<input type="button" value="<spring:message code='btn.save'/>"
								class="button orange btn-save"
								title="<spring:message code='btn.save'/>" />
							<%-- 	        <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button btn-return" /> --%>
						</div>
	
		<div id="menuContent" class="menuContent"
		style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="treeDemo" class="ztree"
			style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>
	<div id="defaultMenuContent" class="menuContent"
		style="display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="defaultCategoryTree" class="ztree"
			style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>

</body>
</html>