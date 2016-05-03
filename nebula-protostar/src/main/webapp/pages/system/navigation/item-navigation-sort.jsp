<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<link type="text/css" href="${base}/css/style.css">
<script type="text/javascript" src="${base}/scripts/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/jquery.sortable.js"></script>
<script>
	var $j = jQuery.noConflict();
	
	<%--用于js 拼接连接 contextPath 应用程序名--%>
	var base = "${base}";
	var pagebase = "${pagebase}";
	var staticbase = "${staticbase}";
	var imgbase="${imgbase}";
	
	$j(function() {
		loxia.init({
			debug : true,
			region : 'zh-CN'
		});
		nps.init();

	});
	
	//导航节点信息
	var zNodes =[           
	           	{ id:0, name:"ROOT",state:"1", open:true,root:"true"},
	           	<c:forEach var="navigation" items="${navigationList}" varStatus="status">
	           		{
	           			id:${navigation.id}, 
	           			pId:${null eq navigation.parentId?0:navigation.parentId}, 
	           			name: "${navigation.name}", 
	           			state:"${navigation.lifecycle}",
	           			diy_type:"${navigation.type}",
	           			diy_param:"${navigation.param}",
	           			diy_sort:"${navigation.sort}",
	           			diy_url:"${navigation.url}",
	           			diy_isNewWin:${navigation.isNewWin},
	           			open:${0 eq navigation.parentId?true:false}
	           		}<c:if test="${!status.last}">,</c:if>
	           	</c:forEach>
	           ];
	
	
</script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>

<script type="text/javascript" src="${base}/scripts/jquery/jquery-migrate-1.2.0.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/jqueryui/jquery-ui-1.10.2.custom.js"></script>

<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiacore-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiainput-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiaselect-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiatable-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxia.locale-zh-CN.js"></script>

<script type="text/javascript" src="${base}/scripts/jquery/jqueryui/jquery.ui.datepicker-zh-CN.js"></script>
<script type="text/javascript" src="${base}/scripts/nebula/jquery.nebula.protostar-1.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/jqueryui/jquery-ui-timepicker-addon.js"></script>

<script type="text/javascript" src="${base}/scripts/system/navigation/spin.min.js" ></script>
<script type="text/javascript" src="${base}/scripts/system/navigation/jquery.spin.js"></script>
<script type="text/javascript" src="${base}/scripts/system/navigation/item-navigation-sort.js"></script>

<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>

<script type="text/javascript" src="${base}/scripts/i18n/common.locale-zh-CN.js"></script>


</head>
<style>
ul,li{list-style: none;  margin: 0;  padding: 0;}
.gbin1-list{clear: both;}
.gbin1-list>li{  
	float: left;  
	width: 250px;   
	text-align: center;  
	border: 1px solid #ccc;  
	margin: 5px;  
	-webkit-user-drag: auto;
	-moz-user-select: auto; 
	-webkit-user-select: auto; 
	-ms-user-select: auto; 
	height: auto;
	min-height: 312px;
}
.ico_position{  
	position: absolute;
    left: 0px;
  	top: 1px;
  	width: 28px;
  	height: 14px;
  	cursor: pointer;
  	display: none;
  	z-index: 1000;
  	background: url(/images/ico_selected.png) no-repeat scroll 0% 0% transparent;
  	background-size: 100%;
  }
.ico_show{  
	z-index: 0;  
	display: block;
}
.ico_selected-grey {
  display: block;
  background: url(/images/ico_selected_1.png) no-repeat scroll 100% 100% transparent;
  z-index: 0;
  width: 29px;
  height: 19px;
  top: 0px;
  left: 3px
}
.badge-2{
  position: absolute;
  right: -42px;
  top: -194px;
  width: 50px;
  height: 50px;
}
.pop-up{ 
	position:fixed; 
	top: 0px; 
	left: 0px; 
	background: rgba(0,0,0,0.7); 
	width: 100%; 
	height: 100%; 
	text-align:center; 
	z-index: 1000;
}
.pop-up-loading {
	background: rgba(0,0,0,0.2); 
}
</style>
<body>
<div class="pop-up prompt-box pop-up-loading" style="display: none;"></div>
	<div class="content-box">
		 <div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/tag.png"> 前台导航管理
					<input type="button" style="display: none;" value="<spring:message code='itemcategorysort.action.del'/>" title="<spring:message code='itemcategorysort.action.del'/>" class="button orange removesort" />
					<input type="button" value="<spring:message code='itemcategorysort.action.add'/>" title="<spring:message code='itemcategorysort.action.add'/>" class="button orange addsort" />
					<a href="/base/navigation.htm?navigationId=${navigationId}" style="float: right;color:#069;font-size: 14px;" id="backNavigation">返回菜单导航</a>
		</div>
		
		<%-- 
		<div class="ui-block">
			<form action="/category/itemCtList.json" id="searchForm">
				<div class="ui-block-content ui-block-content-lb">
					<table>
						<tr>
							<td><label>
									<spring:message code="item.code" />
								</label></td>
							<td><span id="searchkeytext">
									<input type="text" loxiaType="input" mandatory="false" id="code" name="q_sl_code" placeholder="<spring:message code='item.code'/>"></input>
								</span></td>
							<td><label>
									<spring:message code="item.name" />
								</label></td>
							<td><span id="searchkeytext">
									<input type="text" loxiaType="input" mandatory="false" id="title" name="q_sl_title" placeholder="<spring:message code='item.name'/>">
								</span> </input></td>
						</tr>
					</table>
					<div class="button-line1">
						<input type="hidden" id="categoryId" name="q_long_categoryId" value="" />
						<input type="hidden" id="shopId" name="q_long_shopId" value="${shopId }" />
						<a href="javascript:void(0);" class="func-button search" title="<spring:message code='btn.search'/>">
							<spring:message code="btn.search" />
						</a>
					</div>
				</div>
			</form>
		</div>
		 --%>
		
		
		<div class="ui-block ui-block-fleft w240">
			<div class="ui-block-content ui-block-content-lb">
				<div class="tree-control">
					<input type="text" id="key" loxiatype="input" placeholder="<spring:message code="shop.property.keyword" />" />
					<div>
						<span id="search_result"></span>
					</div>
				</div>
				<ul id="tree" class="ztree"></ul>
			</div>
		</div>
		<div class="ui-block ml240">
			<div class="ui-block-content" style="padding-top: 0">
				<div class="ui-tag-change">
					<ul class="tag-change-ul">
						<li class="memberbase">已排序商品</li>
						<li class="memberbase">未排序商品</li>
					</ul>
					<form action="/navigation/updateItemSort.json" method="post" name="updateSortForm" id="updateSortForm">
					<input type="hidden" id="sequnce" name="sequence"  value=""/>
					<input type="hidden" id="navigationId" name="navigationId"  value="${navigationId}"/>
					<input type="hidden"  id="_csrf" name="_csrf"  value="${_csrf.token}"/>
						<div class="tag-change-content" id="newSortTable">
							<div class="tag-change-in sorted">
								<%@include file="/pages/system/navigation/item-navigation-detail.jsp" %>
							</div>
							<div class="tag-change-in">
								<div class="ui-block-content">
									<div style="margin-right: 10px;display: inline-block;">
										<div style="margin: 0px 0px 10px 0px;">商品编码</div>
										<textarea name="itemCodes" class="itemCodes ui-loxia-default ui-corner-all ui-loxia-highlight " mandatory="true" style="width: 300px; height: 300px;"placeholder="说明： 多个商品编码以换行区分。" aria-disabled="false"></textarea>
									</div>
									<div style="display: inline-block;">
										<div style="margin: 0px 0px 10px 0px;">操作结果</div>	
										<textarea name="showDesc" class="showDesc ui-loxia-default ui-corner-all" style="width: 600px; height: 300px;" readonly="readonly" aria-disabled="false"></textarea>
									</div>
								</div>
								<div class="border-grey ui-loxia-simple-table" id="unsortedTable" caption="<spring:message code='itemcategorysort.unsorted.list'/>"></div>
							</div>
						</div>
					</form>
				</div>
				<div class="button-line">
					<input type="button" value="<spring:message code='itemcategorysort.action.add'/>" title="<spring:message code='itemcategorysort.action.add'/>" class="button orange addsort" />
					<input type="button" style="display: none;" value="<spring:message code='itemcategorysort.action.del'/>" title="<spring:message code='itemcategorysort.action.del'/>" class="button orange delsort" />
				</div>
			</div>
		</div>
	</div>
	<div id="categoryContent" class="menuContent" style="z-index: 999; display: none; position: absolute; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
		<ul id="categoryDemo" class="ztree" style="margin-top: 0; width: 180px; height: 100%;"></ul>
	</div>
</body>
</html>
