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
	src="${base}/scripts/salesorder/manual-order-create.js"></script>
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
			<input type="hidden" name="isMemOrder" id="isMemOrder" value="false" />
			<input type="hidden" name="memberId" id="memberId" value="" />
			<input type="hidden" name="coupon" id="coupon"/>
            <input type="hidden" name="manualFlag" id="manualFlag" value="true"/>
            <input type="hidden" name="actualFreight" id="actualFreight" value=""/>
            
			<div class="ui-title1">
				<img src="/images/wmi/blacks/32x32/cube.png">手工下单
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


						<!--  会员验证 start-->
						<div class="ui-block-title1"
							style="background: #fff; color: #000;">会员验证</div>

						<div class="ui-block-content border-grey">

							<div class="sbox-wrap">
								<div class="ui-block-line " style="overflow:visible;">
									<label> 会员验证：</label>
									<div  class="pt7">
										<a id="memberSearch" href="javascript:void(0)"
											class="func-button ml5">点击会员查询</a>
									</div>
								</div>

								<div id="memberInfoDiv" style="display: none"></div>

							</div>
						</div>
						<!--  会员验证 end-->



						<%@ include file="/pages/salesorder/order-create-common.jsp"%>
					</div>
					<div class="button-line">
						<span class="total">应付总额：<strong id="payPrice">0.00</strong>元
						</span>
						<input type="button" value="设置运费"
							class="button setFreightFee" title="设置运费" />
						<input type="button" value="提交订单" class="button orange submit"
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


	<div id="memberSeachDialog" class="proto-dialog">
		<h5>搜索会员</h5>

		<div class="proto-dialog-content">

			<form id="memberSeachForm">
				<div class="ui-block">
					<div class="ui-block-content ui-block-content-lb">
						<table>
							<tr>
								<td><label>会员登录名</label></td>
								<td>
								<td><input type="text" loxiaType="input" mandatory="false"
									placeholder="<spring:message code='member.group.loginname'/>"
									id="loginName" name="q_sl_loginName"></input></td>
								</td>
							</tr>

						</table>
						<div class="button-line1">
							<a href="javascript:void(0);" id="memberSearchButton"
								class="func-button memberSearchButton"><span>搜索</span></a>

						</div>
					</div>
				</div>
			</form>
			<div class="ui-block">
				<div id="table2" class="border-grey" caption="会员列表"></div>
			</div>
		</div>
		<div class="proto-dialog-button-line">
			<input type="button" value="取消"
				class="button orange memberSeachDialogCancel" />
		</div>
	</div>



	<div id="addressManageDialog" class="proto-dialog">
		<h5>地址管理</h5>
		<div class="proto-dialog-content details_content" style="height: 281px;">
		<form id="addressManageForm" action="/account/address/add" enctype="multipart/form-data">
			<div class="ui-block-line">
				<label><span class="red">*</span>收货人：</label>
				<div class="pt7">
					<input type="text" loxiaType="input" id="ads_name" mandatory="true"  name="name" maxlength="25" />
				</div>
			</div>
			
			<div class="ui-block-line">
				<label><span class="red">*</span>所在地区：</label>
				<div class="pt7">
					<select id="ads_provience" name="provinceId"></select>
					<select id="ads_city" name="cityId"></select> 
					<select id="ads_area" name="areaId"></select>
				</div>
			</div>
			
			<div class="ui-block-line">
				<label><span class="red">*</span>街道地址：</label>
				<div class="pt7">
					<input type="text" loxiaType="input"  mandatory="true" id="ads_address" name="address" maxlength="120" />
				</div>
			</div>
			
			<div class="ui-block-line">
				<label>邮政编码：</label>
				<div class="pt7">
					<input type="text" loxiaType="input" checkmaster="checkPostCode" id="ads_postcode" name="postcode" maxlength="6" />
				</div>
			</div>
			
			<div class="ui-block-line">
				<label><span class="red">*</span>手机号码：</label>
				<div class="pt7">
					<input type="text" loxiaType="input"  mandatory="true" id="ads_mobile" name="mobile" checkmaster="checkMobile"  maxlength="11" />
				</div>
			</div>
			
			<div class="ui-block-line">
				<label>电话：</label>
				<div class="pt7">
					<input type="text" loxiaType="input" id="ads_telphone"  name="telphone"  checkmaster="checkTel" />　电话号码格式：区号-电话号码-分机号(如：0911-66011254-521)
				</div>
			</div>
			
			<div class="ui-block-line">
				<label></label>
				<div class="pt7">
					<input type="checkbox"  id="ads_isDefaultButton" /> 设置为默认地址　　设置后系统将在购买时自动选中该收货地址
				</div>
			</div>
			<input type="hidden" id="ads_isDefault" name ="isDefault">
			<input type="hidden" id="ads_id" name="id" />
			<input type="hidden" id="ads_memberId"  name="memberId"/>
			</form>
		</div>

		<div class="proto-dialog-button-line">
			<input type="button" value="确定"
				class="button orange addressManageDialogSubmit" />
			<input type="button" value="取消"
				class="button orange addressManageDialogCancel" />
		</div>
	</div>
	
	
	<div id="setFreightFeeDialog" class="proto-dialog">
		<h5>设置运费金额</h5>
		<div class="proto-dialog-content details_content" style="height: 281px;">
			<div class="ui-block-line">
				<label><span class="red">*</span>金额：</label>
				<div class="pt7">
					<input type="text" loxiaType="input" id="freightFee" maxlength="25" onblur="checkNum(this)"/>
				</div>
			</div>
			
			<div class="proto-dialog-button-line">
				<input type="button" value="确定"
					class="button orange setFreightFeeDialogSubmit" />
				<input type="button" value="取消"
					class="button orange setFreightFeeDialogCancel" />
			</div>
	</div>
</body>
</html>
