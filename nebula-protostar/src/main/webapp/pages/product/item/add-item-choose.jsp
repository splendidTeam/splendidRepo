<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-choose.js"></script>
<script type="text/javascript">
<%-- 添加普通商品行业选择树 --%>
var zNodes =
	[
		{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
		<c:forEach var="industry" items="${industryList}" varStatus="status">
		<c:if test="${industry.isShow}">
			{
				id:${industry.id}, 
				pId:${industry.pId},
				name: "${industry.indu_name}",
				open:${industry.open}
				<c:if test="${industry.noCheck}">
					,nocheck:true
				</c:if>
			}
			<c:if test="${!status.last}">,</c:if>
			</c:if>
		</c:forEach>
	];
</script>
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