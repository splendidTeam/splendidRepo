<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>

<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/promotion/coupon-code-import.js"></script>
<script type="text/javascript">
var $ = jQuery.noConflict();
</script>
<link rel="stylesheet" type="text/css" href="${base}/scripts/uploadify3/uploadify.css" media="screen" />
<script type="text/javascript" src="${base}/scripts/uploadify3/jquery.uploadify.min.js"></script>
<style type="text/css">
	#coupon-upload-button{
		padding-top: 4px;
	}
</style>
</head>
<body>
<input type="hidden" id="session-id" value="${pageContext.session.id }" />
<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/calc.png">优惠券导入
		<input class="button black btn-return" type="button" title="返回" value="返回">
		<input class="button orange btn-download" type="button" title="模版下载" value="模版下载">
	</div>
	<div class="ui-block">
		<div class="ui-block-title1">优惠券信息</div>
		<div class="ui-block-content border-grey">
			<div class="ui-block-line">
				<label>优惠券类型：</label>
				<div>
					<select id="coupon-type" loxiaType="select" mandatory="true">
					<c:forEach var="item" items="${couponTypeList}">
						<option value="${item.id}">${item.couponName}</option>
					</c:forEach>
					</select>	
				</div>
			</div>
			<!-- <div class="ui-block-line mt5">
				<label>有效期：</label>
				<div> 
					<span>
						<input id="start-time" loxiaType="date" class="date-picker" showtime="true" mandatory="true" readonly="readonly" />
					</span> 
					——
					<span>
						<input id="end-time" loxiaType="date" class="date-picker" showtime="true" mandatory="true" readonly="readonly" />
					</span> 
				</div>
			</div> -->
		</div>
	</div>
	
	<div class="ui-block">
		<div style="margin-top: 10px"></div>
		<div class="ui-block-content border-grey">
			<div class="ui-block-line">
				<label>优惠券码上传</label>
				<div id="coupon-upload">
				</div>
				<p style="margin-top: 10px;">
					<a id="btn-ok" class="func-button" href="javascript:void(0)">确认</a>
					<a id="btn-cancel" class="func-button" href="javascript:void(0)">取消</a>
				</p>
				<p style="margin-top: 5px; font-size: 14px; font-weight: bold;">
					<span id="upload-sku-result"></span>
				</p>
				<div id="upload-sku-message" class="upload-message"></div>
			</div>
		</div>
		<div id="errorTip" style="display: none">
			<div style="margin-top: 10px;">错误提示：</div>
			<div class="ui-block-content border-grey">
				<div class="ui-block-line showError" style="color: red"></div>
			</div>
		</div>
	</div>
	<div class="right clear">
		<input class="button orange btn-download" type="button" title="模版下载" value="模版下载">
		<input class="button black btn-return" type="button" title="返回" value="返回">
	</div>
</div>
</body>
</html>
