<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/product/item/add-item-common.jsp"%>
<%@include file="/pages/commons/common.jsp"%>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-bundle.js"></script>
</head>
<body>

<div class="content-box width-percent100">
   <form id="itemForm" name="itemForm" action="/i18n/item/saveBundleItem.json" method="post">
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="second">
				<div class="ui-block ">
					 <div class="ui-block-title1"><spring:message code="item.add.addItem"/></div>
					 
					 <div class="ui-block">
					   		<div class="ui-block-title1 ui-block-title">主卖品</div>
					   		<div class="ui-block-content border-grey">
					   			<ul class="clearfix">
					   				<li class="main-pro">
					   					<a class="showpic"> 
					   						<img src="">
					   						<span class="dialog-close">X</span>
					   					</a>
					   					<p class="title p10">ABCD1234</p>
					   					<p class="sub-title">超级舒适运动跑鞋</p>
					   					
					   				</li>
					   				<li class="main-pro">
					   					<a class="showpic"> 
					   						<img src="">
					   						<span class="dialog-close">X</span>
					   					</a>
					   					<p class="title p10">ABCD1234</p>
					   					<p class="sub-title">超级舒适运动跑鞋</p>
					   					
					   				</li>
					   				<li class="main-pro">
					   					<a class="showpic"> 
					   						<img src="">
					   						<span class="dialog-close">X</span>
					   					</a>
					   					<p class="title p10">ABCD1234</p>
					   					<p class="sub-title">超级舒适运动跑鞋</p>
					   					
					   				</li>
					   				<li class="main-pro pro-empty selectPro">
					   					设置主卖品
					   				</li>
					   			</ul>
					   			
					   		</div>
					   </div>
					 <div class="ui-block">
					   		<div class="ui-block-title1 ui-block-title">捆绑成员</div>
					   		<div class="ui-block-content border-grey">
					   			<ul class="clearfix">
					   				<li class="main-pro">
					   					<a class="showpic"> 
					   						<img src="">
					   						<span class="dialog-close">X</span>
					   					</a>
					   					<p class="title p10">ABCD1234</p>
					   					<p class="sub-title">超级舒适运动跑鞋</p>
					   					
					   				</li>
					   				<li class="main-pro">
					   					<a class="showpic"> 
					   						<img src="">
					   						<span class="dialog-close">X</span>
					   					</a>
					   					<p class="title p10">ABCD1234</p>
					   					<p class="sub-title">超级舒适运动跑鞋</p>
					   					
					   				</li>
					   				<li class="main-pro">
					   					<a class="showpic"> 
					   						<img src="">
					   						<span class="dialog-close">X</span>
					   					</a>
					   					<p class="title p10">ABCD1234</p>
					   					<p class="sub-title">超级舒适运动跑鞋</p>
					   					
					   				</li>
					   				<li class="main-pro pro-empty selectStyle">
					   					+新成员
					   				</li>
					   			</ul>
					   			<a class="user-refresh"></a>
					   		</div>
					   </div>
					 <div class="ui-block">
	   		<div class="ui-block-title1 ui-block-title">价格设置</div>
	   		<div class="ui-block-content border-grey">
	   			<label class="label-line block pb10"> <input type="radio" name="setPrice"> 按捆绑商品总价  </label>
	   			<label class="label-line block pb10"> <input type="radio" name="setPrice"> 一口价（简单）  </label>
	   			<label class="label-line block pb10"> <input type="radio" name="setPrice"> 定制    </label>
	   			<label class="label-line block pb10"> 捆绑商品总价 <input type="text" name="setPrice" readonly placeholder="900"> </label>
	   		</div>
	   		<table class="inform-person" >
				<thead>
					<tr>
						<th width="10%">成员序号</th>
						<th width="25%">商品</th>
						<th width="25%">原销售价 </th>
						<th width="15%">现售价</th>
					</tr>
				</thead>
				<tbody>
					<tr>
					   <td>1</td>
					   <td>ABCD1234</td>
					   <td>500</td>
					   <td> <input type="text" name="setPrice" placeholder="500"></td>
				   </tr>
				   <tr>
					   <td>2</td>
					   <td>ABCD1234</td>
					   <td>500</td>
					   <td> <input type="text" name="setPrice" placeholder="500"></td>
				   </tr>
				</tbody>
			</table>
			<table class="inform-person" >
				<thead>
					<tr>
						<th width="10%">成员序号</th>
						<th width="25%">销售单元</th>
						<th width="25%">是否参与 </th>
						<th width="25%">原销售价</th>
						<th width="15%">现售价</th>
					</tr>
				</thead>
				<tbody>
					<tr>
					   <td>1</td>
					   <td>商品</td>
					   <td>ABCD1234</td>
					   <td>500</td>
					   <td> <input type="text" name="setPrice" placeholder="500"></td>
				   </tr>
				   <tr>
					   <td>2</td>
					    <td>商品-s</td>
					   <td><input type="checkbox"></td>
					   <td>500</td>
					   <td> <input type="text" name="setPrice" placeholder="500"></td>
				   </tr>
				   <tr>
					   <td>2</td>
					    <td>商品-M</td>
					   <td><input type="checkbox"></td>
					   <td>500</td>
					   <td> <input type="text" name="setPrice" placeholder="500"></td>
				   </tr>
				</tbody>
			</table>
	   </div>
					 
				     <%-- 基本信息 --%>
				     <%@include file="/pages/product/item/add-item-baseInfo.jsp"%>
					 <div class="mt10"></div>
					 <%-- 商品价格 --%>
					 <%@include file="/pages/product/item/add-item-price.jsp"%>
				   	 <div class="mt10"></div>
				   	 <%-- bundle扩展信息 --%>
					 <%@include file="/pages/product/item/add-item-bundleExtendedInfo.jsp"%>
				     <div class="mt10"></div>
					 <%-- SEO --%>
				     <%@include file="/pages/product/item/add-item-seo.jsp"%>
				     <div class="mt10"></div>
				     <%-- 商品描述 --%>
					 <%@include file="/pages/product/item/add-item-description.jsp"%>
				     <div class="mt10"></div>
					 <div class="button-line">
				         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
				         <input type="button" value="<spring:message code='item.add.previous'/>" class="button back"  title="<spring:message code='item.add.previous'/>" />
					</div>
					<div class="mt20"></div>
				</div>
			</div>
		</div>
   </form>
</div>

<div id="menuContent" class="menuContent menulayer">
	<ul id="treeDemo" class="ztree"></ul>
</div>
<div id="defaultMenuContent" class="menuContent menulayer">
	<ul id="defaultCategoryTree" class="ztree"></ul>
</div>


<!-- 选择商品弹出层 -->
<div class="select-pro-layer proto-dialog">
	<h5>选择主卖品/成员</h5>
	<div class="proto-dialog-content">
		<div class="ui-block">
			<div class="ui-block">
				<div class="ui-block-content ui-block-content-lb">
					<form action="/recommand/findItemInfoList.json" id="mainItemDialogSearchForm">
							<div class="form-group p10">
								<label>类型</label>
								<input type="radio" name="type" value="商品" checked="checked" />商品
								<input type="radio" name="type" value="款" />款
							</div>
							<div class="form-group p10">
								<label>编码</label>
								<input type="text">
							</div>
							<div class="button-line1 right">
								<a href="javascript:void(0);" class="func-button orange"><span>搜索</span></a>
							</div>
					</form>	
				</div>
			</div>
			<div class="border-grey" id="selectProList" caption="商品列表 ">
				<table  width="2000" class="single-price" >
						<thead>
							<tr>
								<th width="5%"></th>
								<th width="25%">商品</th>
								<th width="25%">商品分类 </th>
								<th width="15%">商品种类</th>
								<th width="15%">商品价格</th>
								<th width="15%">商品状态</th>
							</tr>
						</thead>
						<tbody>
							<tr>
							   <td><input type="radio" checked="checked" /></td>
							   <td><dl><dt><img src="" alt="pic" /></dt><dd>ABCD1234超级舒适运动跑鞋</dd></dl></td>
							   <td><strong class="pro-name">鞋->男鞋</strong> <p class="pro-describe">男子,运动生活</p></td>
							   <td>普通商品</td>
							   <td>499</td>
							   <td>已上架</td>
						   </tr>
						   <tr>
							   <td><input type="radio" /></td>
							   <td><dl><dt><img src="" alt="pic" /></dt><dd>ABCD1234超级舒适运动跑鞋</dd></dl></td>
							   <td><strong class="pro-name">鞋->男鞋</strong> <p class="pro-describe">男子,运动生活</p></td>
							   <td>普通商品</td>
							   <td>499</td>
							   <td>已上架</td>
						   </tr>
						   <tr>
							   <td><input type="radio" /></td>
							   <td><dl><dt><img src="" alt="pic" /></dt><dd>ABCD1234超级舒适运动跑鞋</dd></dl></td>
							   <td><strong class="pro-name">鞋->男鞋</strong> <p class="pro-describe">男子,运动生活</p></td>
							   <td>普通商品</td>
							   <td>499</td>
							   <td>已上架</td>
						   </tr>
						</tbody>
					</table>
			</div>
		</div>
	</div>
	<div class="proto-dialog-button-line right">
		<input type="button" value="确定" class="button orange"/>
	</div>
</div>

<!-- 选择款弹出层  -->
<div class="select-style-layer proto-dialog">
	<h5>选择主卖品/成员</h5>
	<div class="proto-dialog-content">
		<div class="ui-block">
			<div class="ui-block">
				<div class="ui-block-content ui-block-content-lb">
					<form action="/recommand/findItemInfoList.json" id="mainItemDialogSearchForm">
							<div class="form-group p10">
								<label>类型</label>
								<input type="radio" name="type" value="商品" />商品
								<input type="radio" name="type" value="款" checked="checked" />款
							</div>
							<div class="form-group p10">
								<label>编码</label>
								<input type="text">
							</div>
							<div class="button-line1 right">
								<a href="javascript:void(0);" class="func-button orange"><span>搜索</span></a>
							</div>
					</form>	
				</div>
			</div>
			<div class="border-grey" id="selectProList" caption="商品列表 ">
				<table  width="2000" class="inform-person" >
						<thead>
							<tr>
								<th width="5%"></th>
								<th width="25%">款号</th>
								<th width="25%">商品</th>
								<th width="15%">商品种类</th>
								<th width="15%">商品价格</th>
								<th width="15%">商品状态</th>
							</tr>
						</thead>
						<tbody>
							<tr>
							   <td><input type="radio" checked="checked" /></td>
							   <td>111</td>
							   <td><dl><dt><img src="" alt="pic" /></dt><dd>ABCD1234超级舒适运动跑鞋</dd></dl></td>
							   <td>普通商品</td>
							   <td>499</td>
							   <td>已上架</td>
						   </tr>
						   <tr>
							   <td><input type="radio" /></td>
							   <td>111</td>
							   <td><dl><dt><img src="" alt="pic" /></dt><dd>ABCD1234超级舒适运动跑鞋</dd></dl></td>
							   <td>普通商品</td>
							   <td>499</td>
							   <td>已上架</td>
						   </tr>
						   <tr>
							   <td><input type="radio" /></td>
							   <td>111</td>
							   <td><dl><dt><img src="" alt="pic" /></dt><dd>ABCD1234超级舒适运动跑鞋</dd></dl></td>
							   <td>普通商品</td>
							   <td>499</td>
							   <td>已上架</td>
						   </tr>
						</tbody>
					</table>
			</div>
		</div>
	</div>
	<div class="proto-dialog-button-line right">
		<input type="button" value="确定" class="button orange"/>
	</div>
</div>



<script>
$j(document).ready(function(){
	$j(".selectPro").on("click",function(){
		$j('.select-pro-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});	
	});	
	
	/*列表数据加载 用插件   loxiasimpletable */
/* 	$j("#selectProList").loxiasimpletable({
		page : true,
		size : 15,
		nodatamessage : '<span>' + nps.i18n("NO_DATA") + '</span>',
		form : "mainItemDialogSearchForm",
		cols : [ {
			label : "",
			width : "5%",
			template : "checkboxTemplate",
		}, {
			name : "code",
			label : "商品编码",
			width : "10%",
			sort: ["tpi.code asc","tpi.code desc"]
		}, {
			name : "title",
			label : "商品名称",
			width : "20%",
			sort: ["tpii.title asc","tpii.title desc"]
		}, {
			name : "操作",
			label : "操作",
			width : "5%",
			template : "operateTemplate",
		}],
		dataurl : findItemInfoListJsonUrl
	}); */
	
	$j(".selectStyle").on("click",function(){
		$j('.select-style-layer').dialogff({type:'open',close:'in',width:'900px', height:'500px'});	
	});	
	
	$j(".dialog-close").on("click",function(){
		$j(this).closest("li.main-pro").remove();
	})
	
});
</script>

</body>
</html>

