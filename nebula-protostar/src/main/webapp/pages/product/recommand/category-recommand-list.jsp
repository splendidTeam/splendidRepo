<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="item.update.manage" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/recommand/category-recommand-list.js"></script>
<%-- <script type="text/javascript" src="${base}/scripts/search-filter.js"></script> --%>
<script type="text/javascript">
var categoryZnodes = <c:out value="${ categoryJson }" default="[]" escapeXml="false" />
</script>
<script type="text/javascript">
$j(window).load(function(){
	$j('#category-recommand-item-table').tabledrag();
});
</script>
</head>
<body>
	<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/cube.png">分类推荐管理
			<input type="button" value="<spring:message code="btn.all.delete"/>" class="button butch delete" title="<spring:message code="btn.all.delete"/>" />
			<input type="button" value="添加" class="button orange add" title="添加"/> 
		</div>
		<div class="ui-block ui-block-fleft w240">
			<div class="ui-block-content ui-block-content-lb">
				<div class="tree-control">
					<input type="text" id="keyWord" loxiatype="input" placeholder="<spring:message code="shop.property.keyword"/>" />
					<div><span id="searchResult"></span></div>
				</div>
				<ul id="categoryRecTree" class="ztree"></ul>
			</div>
		</div>
		<div class="ui-block ml240" style="padding-left: 10px;">
			<div class="ui-block-title1">推荐商品列表</div>
			<form id="recommandForm" action="/recommand/saveRecommandItem.json" method="post">
				<input id="type" name="type" type="hidden" value="" />
				<input id="param" name="param" type="hidden" value="" />
				<div id="edittable" loxiaType="edittable" >
					<table id="category-recommand-item-table" append="1" width="2000" class="inform-person" >
						<thead>
							<tr>
								<th width="10%"><input type="checkbox"/></th>
								<th width="10%"></th>
								<th width="25%">商品编码 </th>
								<th width="25%">商品名称</th>
								<th width="20%">推荐分类</th>
								<%-- <th width="5%">排序</th> --%>
								<th width="10%">操作</th>
							</tr>
						</thead>
						<tbody><%-- 推荐商品信息 --%></tbody>
						<tbody></tbody>
					</table>
					<div class="button-line">
						<input type="button" value="添加" class="button orange add"   title="添加"/>
						<input type="button" value="保存" class="button orange save"   title="保存"/>
						<input type="button" value="<spring:message code="btn.all.delete"/>" class="button butch delete"   title="<spring:message code="btn.all.delete"/>"/>
					</div>
				</div>
			</form>
		</div>
	</div>
<%@include file="/pages/product/recommand/recommand-dialog.jsp" %>
</body>
</html>
