<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" type="text/css"href="${base}/scripts/uploadify3/uploadify.css" media="screen" />
 <script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript"src="${base}/scripts/uploadify3/jquery.uploadify.min.js"></script>
<script type="text/javascript"src="${base }/scripts/itemcolor/itemcolor-list.js"></script>
<script type="text/javascript"src="${base}/scripts/itemcolor/itemcolor-import.js"></script>


</head>
<body>
	<div class="content-box width-percent100">
		<div class="content-box width-percent100">
			<div class="ui-title1">
				<img src="${base}/images/wmi/blacks/32x32/cube.png">商品筛选色颜色对照
			</div>
			<form id="searchForm">
				<div class="ui-block">
					<div class="ui-block-content ui-block-content-lb">
						<table>
							<tr>
								<td><label>筛选色 名称</label></td>
								<td><input type="text" loxiaType="input" mandatory="false"
									id="filterColor" name="q_string_filterColor" placeHolder="筛选色 名称" /></td>
							</tr>
						</table>
						<div class="button-line1">
							<a href="javascript:void(0);" class="func-button search"
								title="<spring:message code ='user.list.filter.btn'/>"> <spring:message code='user.list.filter.btn' /></a>
						</div>
					</div>
				</div>
			</form>


			<div class="ui-block">
				<div class="table-border0 border-grey" id="table1" caption="商品筛选色颜色对照列表"></div>
			</div>
			<div class="ui-block">
				<div style="margin-top: 10px"></div>
				<div class="ui-block-content border-grey">
					<div class="ui-block-line">
						<label>商品颜色对照上传:</label>
				<div id="coupon-upload">
				</div>
						<p style="margin-top: 10px;">
							<a id="btn-ok" class="func-button" href="javascript:void(0)">确认</a>
							<a id="btn-cancel" class="func-button" href="javascript:void(0)">取消</a>
						</p>
						<p
							style="margin-top: 10px; font-size: 14px; font-weight: bold; color: red;">
							<span id="upload-sku-result"></span>
						</p>
						<div id="upload-sku-message" class="upload-message"></div>
					</div>
					<input type="hidden" id="session-id" value="${pageContext.session.id }" />
					<div class="right clear">
						<input class="button orange btn-download" type="button"
							title="模版下载" value="模版下载">
					</div>
				</div>
				<div id="errorTip" style="display: none">
					<div style="margin-top: 10px;">错误提示：</div>
					<div class="ui-block-content border-grey">
						<div class="ui-block-line showError" style="color: red"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>