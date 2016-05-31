<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
</head>
<body>

<!-- 这是普通商品行业选择模块 -->
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="first">
				<div class="ui-block border-grey">
				   <div class="ui-block-title1"><spring:message code="item.add.industry"/></div>
				   <div class="ui-block ui-block-fleft w240">
				        <div class="ui-block-content ui-block-content-lb">
				            <div class="tree-control">
				                <input type="text" id="key" loxiatype="input" placeholder="<spring:message code='item.add.keyword'/>" />
				                <div><span id="search_result"></span></div>
				            </div>
				            <ul id="industrytreeDemo" class="ztree"></ul>
				        </div>
			      </div>
				  <div class="button-line">
			         <input type="button" value="<spring:message code='system.property.next'/>" class="button orange next" title="<spring:message code='system.property.next'/>"/>
			         <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
			      </div>
				</div>
			</div>
		</div>
	</div>
</div>

</body>