<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<div class="ui-block-title1 ui-block-title" >
	<spring:message code="item.add.bundleExtendedInfo" />
</div>

<div class="ui-block-content border-grey">
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
				<li id="set_main_element" class="main-pro pro-empty selectPro">设置主卖品</li>
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
				<li id="add_bundle_element" class="main-pro pro-empty selectStyle">+新成员</li>
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
			</tbody>
		</table>
	</div>
</div>					 

<!-- 选择商品弹出层 -->
<div class="select-pro-layer proto-dialog">
	<h5 id="bundle_dialog_title">选择主卖品/成员</h5>
	<div class="proto-dialog-content">
		<div class="ui-block">
			<div class="ui-block">
				<div class="ui-block-content ui-block-content-lb">
					<form id="mainItemDialogSearchForm">
							<div class="form-group p10">
								<label>类型</label>
								<input type="radio" name="type" value="product" checked="checked" />商品
								<input type="radio" name="type" value="style" <c:if test="${isEnableStyle == false }">disable="disable"</c:if> />款
							</div>
							<div class="form-group p10">
								<label>编码</label>
								<input type="text" loxiaType="input" name="q_sl_code" mandatory="false" />
							</div>
							<div class="button-line1 right">
								<a href="javascript:void(0);" class="func-button orange" id="search_button"><span>搜索</span></a>
							</div>
					</form>	
				</div>
			</div>
			<div class="border-grey" id="selectProList_product" caption="商品列表 ">
				<table  width="2000" class="inform-person" >
					<thead>
						<tr>
							<th width="3%"></th>
							<th width="10%">商品编码</th>
							<th width="25%">商品名称</th>
							<th width="30%">商品分类 </th>
							<th width="10%">商品种类</th>
							<th width="10%">商品价格</th>
							<th width="5%">库存</th>
							<th width="5%">状态</th>
						</tr>
					</thead>
					<tbody>
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
