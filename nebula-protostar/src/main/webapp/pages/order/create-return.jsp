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
	src="${base}/scripts/search-filter.js?${version_js}"></script>
<script type="text/javascript"
	src="${base}/scripts/salesorder/create-return.js?${version_js}"></script>
	
<%-- <script type="text/javascript" src="${base}/scripts/salesorder/create-return.js"></script>
<script type="text/javascript" src="${base}/js/return/return-order.js"></script>
<script type="text/javascript" src="${base}/js/myAccount/myaccount-return.js"></script> --%>
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

		<form name="searchForm"  enctype="multipart/form-data">
		<input type="hidden"
			name="isMemOrder" id="isMemOrder" value="false" /> <input
			type="hidden" name="memberId" id="memberId" value="" /> <input
			type="hidden" name="coupon" id="coupon" /> <input type="hidden"
			name="manualFlag" id="manualFlag" value="true" /> <input
			type="hidden" name="actualFreight" id="actualFreight" value="" />

			<div class="ui-title1"><img
				src="/images/wmi/blacks/32x32/cube.png">手工退单</div>
			<div class="ui-block">

				<div id="second" style="display: block">

					<div class="ui-block-line">
						<label><span class="red">*</span>订单号：</label>
						<div class="pt7">
							<input type="text" loxiaType="input" id="ads_name" mandatory="true"  class="code" maxlength="25" />
						</div>
					</div>
	      			  <input type="button" value="查询" title="手工下单"  class="search" /> 
					<div class="showReturn"></div>
					<div style="margin-top: 20px"></div>

				</div>
			</div>
			</form>

</div>




		
</body>
</html>
