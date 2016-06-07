<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<div class="ui-block-title1 ui-block-title" >
	<spring:message code="item.add.bundleExtendedInfo" />
</div>

<div class="ui-block-content border-grey">
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">主卖品</div>
		<div class="ui-block-content border-grey">
			<ul class="clearfix setMainProduct">
				<li class="main-pro">
					<a class="showpic"> 
						<img src="">
						<span class="dialog-close">X</span>
					</a>
					<p class="title p10">ABCD1234</p>
					<p class="sub-title">超级舒适运动跑鞋</p>
				</li>
				<li class="main-pro pro-empty" id="selectPro">设置主卖品</li>
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
				<li class="main-pro pro-empty selectStyle">+新成员</li>
			</ul>
			<a class="user-refresh"></a>
		</div>
	</div>
	<div class="ui-block">
		<div class="ui-block-title1 ui-block-title">价格设置</div>
		<div class="ui-block-content border-grey">
	   		<label class="label-line block pb10"> <input type="radio" name="setPrice" value="subtotal" checked="checked"> 按捆绑商品总价  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="setPrice" value="fix"> 一口价（简单）  </label>
	   		<label class="label-line block pb10"> <input type="radio" name="setPrice" value="custom"> 定制    </label>
	   		<label class="label-line block pb10"> 捆绑商品总价 <input type="text" name="setPrice" readonly placeholder="900"> </label>
		</div>
	   	<table class="inform-person sku-table" >
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
					<td> <input type="text" name="setPrice" readonly placeholder="500"></td>
				</tr>
			</tbody>
		</table>
		<table class="inform-person product-table" style="display:none">
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
				 

