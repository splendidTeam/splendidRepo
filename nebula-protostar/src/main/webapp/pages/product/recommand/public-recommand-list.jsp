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
<script type="text/javascript" src="${base}/scripts/product/recommand/public-recommand-list.js"></script>
<script type="text/javascript">
	var recommandParamJson = <c:out value="${ recommandParamJson }" default="[]" escapeXml="false" />;
</script>

<script type="text/javascript">
$j(window).load(function(){
	$j('#public-recommand-item-table').tabledrag();
});
</script>
</head>
<body>
    <div class="content-box width-percent100">
       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/tag.png">公共推荐管理
       		<input type="button" value="批量删除" class="button butch delete"   title="批量删除"/>
       		<input type="button" value="添加" class="button orange add"   title="添加"/>
       </div>
       <div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
				<table>
					<tr>
						<td><label>推荐类型</label></td>
						<td>
							<opt:select id="recommandParam" expression="chooseOption.RECOMMAND_PARAM" otherProperties="loxiaType=\"select\" "/>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="ui-block">
			<div class="ui-block-title1">推荐商品列表</div>
			<form id="recommandForm" action="/recommand/saveRecommandItem.json" method="post">
				<input id="type" name="type" type="hidden" value="" />
				<input id="param" name="param" type="hidden" value="" />
				<div id="edittable" loxiaType="edittable" >
					<table id="public-recommand-item-table" append="1" width="2000" class="inform-person" >
						<thead>
							<tr>
								<th width="10%"><input type="checkbox"/></th>
								<th width="10%"></th>
								<th width="25%">商品编码 </th>
								<th width="25%">商品名称</th>
								<%-- <th width="20%">推荐参数</th> --%>
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
						<input type="button" value="批量删除" class="button butch delete"   title="批量删除"/>
					</div>
				</div>
			</form>
		</div>
	</div>
<%@include file="/pages/product/recommand/recommand-dialog.jsp" %>
</body>
</html>
