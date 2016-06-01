<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<h1 class="selectPro">选择商品</h1>
<h1 class="selectStyle">选择款</h1>

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
								<input type="radio" name="type" value="product" checked="checked" />商品
								<input type="radio" name="type" value="style" />款
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
			<div class="border-grey" id="selectProList_product" caption="商品列表 ">
				<table  width="2000" class="inform-person" >
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
					</tbody>
				</table>
			</div>
			<div class="border-grey" id="selectProList_style" caption="商品列表 " style="display:none">
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
							<td>款号</td>
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
	
});
</script>
