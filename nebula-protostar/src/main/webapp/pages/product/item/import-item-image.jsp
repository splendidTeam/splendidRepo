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
<script type="text/javascript" src="${base}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/import-item-image.js"></script>

<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js" ></script>  

</head>
<body>

<div class="content-box width-percent100">
    <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">商品图片上传
    	<%-- <input type="button" value="<spring:message code='btn.return'/>" class="button return"  title="<spring:message code='btn.return'/>" /> --%>
    </div>
	<div class="ui-block">
	

		<div class="">
			<div class="ui-block-title1">商品图片上传</div>
			<div style="margin-top: 10px"></div>
			<form id="uploadImgZip"  method="post" enctype="multipart/form-data">
				<div class="ui-block-content border-grey">
					<div class="ui-block-line ">
						<label>导入类型</label>
						<select name="type" loxiaType="select">
							<option value="1" <c:if test="${'add' eq defaultUploadType or empty defaultUploadType}">selected="selected"</c:if>>添加导入</option>
							<option value="0" <c:if test="${'replace' eq defaultUploadType}">selected="selected"</c:if>>替换导入</option>
						</select>
						<span>
							注：<span class="red">添加导入</span>是直接导入商品图片；<span class="red">替换导入</span>是先删除原有的商品图片，再导入。
						</span>
					</div>
					<div class="ui-block-line">
						<label>导入文件</label>
						<input type="file" name="itemImgFile" id="itemImgFile" /> 
					</div>
				</div>
				<div class="button-line">
					<input type="button" class="button orange save" value="保存" title="保存"/>
					<input type="button" value="返回" class="button return" title="返回" />
				</div>
			</form>  
				<%-- 
				<input beforeSend="fnBeforeSend" complete="fnComplete" class="imgUploadComponet" role="" model="C" hName="file30"   type="file" url="/itemImage/uploadItemImgZip.json"/>
				--%>
			<div id="errorTip" style="">
			<div style="margin: 10px 0;" >友情提示：</div>
				<div class="ui-block-content border-grey" >
					  <div class="ui-block-line showError" style="color: red">
					       <span>商品图片批量上传成功</span>
					  </div>		
				</div>
			</div>
		</div>
	</div>

</div>
</body>
</html>
