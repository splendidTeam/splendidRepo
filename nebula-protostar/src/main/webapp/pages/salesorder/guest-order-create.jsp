<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet"
	href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript"
	src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/address/address.js"></script>
<script type="text/javascript"
	src="${base}/scripts/salesorder/order-create.js"></script>
<script type="text/javascript"
	src="${base}/scripts/salesorder/cartline-common.js"></script>
<style type="text/css">
em {
	margin-right: 5px;
	color: #f60;
	font-family: sans-serif;
}

.review-tbody {
	border-bottom: 1px dashed #ddd;
	padding: 10px 0;
}

.sbox-wrap {
	padding: 0 44px;
}

.order-summary {
	padding-top: 10px;
	padding-left: 1000px;
	overflow: hidden;
	zoom: 1;
}

.order-summary .statistic {
	width: 400px;
}

.order-summary .statistic .list {
	height: 24px;
	line-height: 24px;
	overflow: hidden;
	zoom: 1;
}

.order-summary .statistic span {
	width: 180px;
	margin-right: 20px;
	float: left;
	text-align: right;
}

.order-summary .statistic em {
	color: #e4393c;
}

.order-summary .statistic .price {
	color: #333333;
	float: left;
	width: 100px;
	text-align: right;
}

.total {
	padding-right: 18px;
	line-height: 50px;
	font-size: 14px;
	font-weight: 700;
}

.total strong {
	color: #e4393c;
	font-size: 20px;
	vertical-align: bottom;
	font-weight: normal;
}

.minus:hover {
	color: #f50;
	z-index: 3;
	border-color: #f60;
}

.plus:hover {
	color: #f50;
	z-index: 3;
	border-color: #f60;
}

.propertyLines {
	width: 30px;
	padding-bottom: 5px;
	display: inline-block;
	text-align: right;
}

.salePropNameStyle {
	width: 35px;
	display: inline-block
}
</style>

</head>
<body id="orderInfoBody">

	<div class="content-box width-percent100">

		<form name="orderForm" action="/order/saveOrder.json" method="post"
			enctype="multipart/form-data">
			<input type="hidden" name="isMemOrder" id="isMemOrder" value="false"/>
			<input type="hidden" name="memberId" id="memberId"/>
			<input type="hidden" name="manualFlag" id="manualFlag" value="false"/>
			<input type="hidden" name="coupon" id="coupon"/>
			<div class="ui-title1">
				<img src="/images/wmi/blacks/32x32/cube.png">游客下单
			</div>
			<div class="ui-block">

				<div id="second" style="display: block">

					<div class="ui-block">
						<div class="ui-block-content ui-block-content-lb"
							style="padding-bottom: 10px;">
							<table>
								<tr>
									<td><label>填写并核对订单信息</label></td>
									<td><span></span></td>

								</tr>
							</table>
						</div>
					</div>


					<div class="ui-block ">


						<div class="ui-block-title1">填写并核对订单信息</div>
						<%@ include file="/pages/salesorder/order-create-common.jsp" %>
			            
					</div>

					<div class="button-line">
						<span class="total">应付总额：<strong id="payPrice">0.00</strong>元
						</span> <input type="button" value="提交订单" class="button orange submit"
							title="提交订单" /> <input type="button" value="返回"
							class="button orange return" title="返回" />

					</div>
					<div style="margin-top: 20px"></div>


				</div>
		</form>

	</div>



	<!-- dialog -->
	<div id="addShoppingCart-dialog" class="proto-dialog">
		<h5>添加购物车</h5>


		<div class="proto-dialog-content"></div>

		<div class="proto-dialog-button-line">
			<input type="button" value="加入购物车" class="button orange addbuycart" />
			<input type="button" value="取消"
				class="button orange addbuycartCancel" />
		</div>
	</div>



	<div id="seachDialog" class="proto-dialog">
		<h5>搜索商品</h5>

		<div class="proto-dialog-content">

			<form id="searchForm">
				<div class="ui-block">
					<div class="ui-block-content ui-block-content-lb">
						<table>
							<tr>
								<td><label>关键字</label></td>
								<td>
								<td><input type="text" id="name" placeHolder="关键字"
									name="q_string_keyWords" loxiaType="input" mandatory="false"></input></td>
								</td>
							</tr>

						</table>
						<div class="button-line1">
							<a href="javascript:void(0);" id="search"
								class="func-button search"><span>搜索</span></a>

						</div>
					</div>
				</div>
			</form>
			<div class="ui-block">
				<div id="table1" class="border-grey" caption="商品列表"></div>
			</div>
		</div>
		<div class="proto-dialog-button-line">
			<input type="button" value="关闭"
				class="button orange seachDialogCancel" />
		</div>
	</div>
</body>
</html>
