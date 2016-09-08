<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="shop.add.shopmanager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/import-updatePrice.js"></script>
<script type="text/javascript">
var $ = jQuery.noConflict();
</script>
<link rel="stylesheet" type="text/css" href="${base}/scripts/uploadify3/uploadify.css" media="screen" />
<script type="text/javascript" src="${base}/scripts/uploadify3/jquery.uploadify.min.js"></script>
<style type="text/css">
	#sku-upload-button{
		padding-top: 4px;
	}
</style>
</head>
<body>
<input type="hidden" id="session-id" value="${pageContext.session.id }" />
<div class="content-box width-percent100">
    <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">商品价格批量修改</div>
	<div class="ui-block">
	

		<div class="">
		  <div class="ui-block-title1">商品数据下载</div>
		  <div class="ui-block-content border-grey">
			  <div class="ui-block-line ">
					<input type="button" id="downLoadTmplOfSkuInfo" value="下载全部的商品数据以及Sku数据">
			  </div>
		 </div>
			
			
			<div style="margin-top: 10px"></div>
			<div class="ui-block-content border-grey">
			   <div class="ui-block-line ">
			        <label style="">商品上传</label>
					<div id="sku-upload">
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
	      	<div style="margin-top: 10px;" >信息提示：</div>
			<div class="ui-block-content border-grey">
				  <div class="ui-block-line showError" style="color: red">
				       
				  </div>		
		      </div>
	      </div>
	
		</div>
	</div>

</div>
</body>
</html>
