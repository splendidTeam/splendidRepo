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
<script type="text/javascript" src="${base}/scripts/product/recommand/item-recommand-list.js"></script>
<%-- <script type="text/javascript" src="${base}/scripts/search-filter.js"></script> --%>

<script type="text/javascript">
$j(window).load(function(){
	$j('#item-recommand-table').tabledrag();
});
</script>
</head>
<body>
	    <div class="content-box width-percent100">
       <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/tag.png">商品搭配管理
       		<input type="button" value="批量删除" class="button butch delete" title="批量删除"/>
       		<input type="button" value="添加" class="button orange add" title="添加"/>
       </div>
       <div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
				<table>
					<tr>
						<td><label>主商品编码:</label></td>
						<td><input name="itemCode" id="itemCode" value="" loxiaType="input" /></td>
						<td><a href="javascript:void(0);" class="func-button mainItemQuery"><span>查找</span></a></td>
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
					<table id="item-recommand-table" append="1" width="2000" class="inform-person" >
						<thead>
							<tr>
								<th width="10%"><input type="checkbox"/></th>
								<th width="10%"></th>
								<th width="25%">商品编码 </th>
								<th width="25%">商品名称</th>
								<th width="20%">主商品名称</th>
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

<div class="main-item-dialog proto-dialog">
	<h5>商品筛选器</h5>
	<div class="proto-dialog-content">
		<div class="ui-block">
			<div class="ui-block">
				<div class="ui-block-content ui-block-content-lb">
					<form action="/recommand/findItemInfoList.json" id="mainItemDialogSearchForm">
							<table>
								<tbody>
									<tr>
										<td><label>商品编码</label></td>
										<td><span id="searchkeytext"> <input type="text" mandatory="false" id="mainItemCode" name="q_sl_code" placeholder="商品编码" class="ui-loxia-default ui-corner-all" aria-disabled="false"></span></td>
										<td><label>商品名称</label></td>
										<td><span id="searchkeytext"> <input type="text" mandatory="false" id="mainItemName" name="q_sl_title" placeholder="商品名称" class="ui-loxia-default ui-corner-all" aria-disabled="false"></span></td>
										<td><input id="categoryCode" type="hidden" mandatory="false" name="q_string_categoryCode" ></td>
									</tr>
								</tbody>
							</table>
							<div class="button-line1">
								<a href="javascript:void(0);" class="func-button mainItemSearch"><span>搜索</span></a>
							</div>
					</form>	
				</div>
			</div>
			<div class="border-grey" id="main-item-table" caption="商品列表 "></div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="确定" class="button orange confirm"/>
		<input type="button" value="取消" class="button cencal" />	
	</div>
</div>
</body>
</html>
