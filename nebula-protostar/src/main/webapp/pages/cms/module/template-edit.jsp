<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${base }/scripts/cms/cms-create-template.js"></script>
<script type="text/javascript">
	var tmpId = "${templateId}";
	var repeatData ="${repeatData}";
</script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">编辑模块
		<input type="button" value="返回" class="button butch return-module" title="返回"/>
		<input type="button" value="预览" class="button orange preview-module" title="预览"/>
		<input type="button" value="保存" class="button orange save-module" title="保存"/>
	</div>
    <div class="ui-block">
    	<%-- 编辑页面区域 --%>
    	<div class="ui-block-title1">页面模块</div>
    	<script type="text/javascript" src="${base}/scripts/cms/cms-ztree.js" ></script>
    	<script type="text/javascript" src="${base}/scripts/cms/edit-template-product.js" ></script>
		<script type="text/javascript" src="${base}/scripts/cms/edit-template.js" ></script>
		<iframe src="${base}/module/findTemplateByTemplateId.htm?templateId=${templateId}" width="100%" class="web-update" frameborder="0"></iframe>
    </div>
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange save-module" />
       	 <input type="button" value="预览" class="button orange preview-module" title="预览"/>
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return-module" />

    </div>
</div>
<div class="proto-dialog cms-tmp-edit-dialog">
    <h5>编辑区域设置</h5>
    <div class="proto-dialog-content">
	     <div class="ui-block-line mt5 info center" >
	     </div>
	      <div class="ui-block-line mt5 editway"  >
			<label title="">实例编辑方式</label>
			<select class="mode" loxiaType="select" mandatory="true">
				<option value="3" >表单编辑方式</option>
				<option value="1">纯html编辑方式</option>
				<option value="2">商品模式</option>
			</select>
		  </div>
		  <div class="ui-block-line mt5" style="display: none"  >
			<label>编辑区域编码</label>
			<input  class="code" placeHolder="编码" loxiaType="input" mandatory="true"  />
		  </div>
		  <div class="ui-block-line mt5 onlycode" >
			<label>代码预览(只读)</label>
			<textarea class="html" placeholder="代码" readonly="readonly" style="width: 600px;height: 280px;resize:none;" loxiaType="input" mandatory="true"></textarea>
		  </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="确定" class="button orange mr5 confrim" />
          <input type="button" value="修改" class="button orange mr5 update" />
          <input type="button" value="关闭" class="button cancel close"/>
          <input type="button" value="扩大选区" class="button orange mr5 expand" />
          <input type="button" value="确定并设置子元素为列表" style="display: none" class="button orange mr5 subList" />
          <input type="button" value="去除子元素列表" style="display: none" class="button cancel mr5 resubList" />
          <input type="button" value="去除编辑状态" class="button cancel mr5 del" />
     </div>
</div>

<div class="proto-dialog cms-imgArticle-edit-dialog">
    <h5>编辑模板可设置选项</h5>
    <div class="proto-dialog-content">
	     <div class="ui-block-line mt5" >
			<label>预览代码(只读)</label>
			<textarea class="html" placeholder="代码" readonly="readonly" style="width: 600px;height: 280px;resize:none;" loxiaType="input" mandatory="true"></textarea>
		 	<div class="zTreeDemoBackground left">
				<ul id="zTreeForDom" class="ztree"></ul>
			</div>
		 </div>
		  <div class="ui-block-line mt5" >
		  	<label class="editSetting">设置编辑选项</label>
		  	<input type="button" value="添加标题编辑" class="button orange mr5 title" />
			<input type="button" value="删除标题编辑" class="button cancel mr5 retitle" />
			<input type="button" value="添加图片编辑" class="button orange mr5 img" />
			<input type="button" value="删除图片编辑" class="button cancel mr5 reimg" />
			<input type="button" value="添加超链接编辑" class="button orange mr5 href" />
			<input type="button" value="删除超链接编辑" class="button cancel mr5 rehref" />
			<input type="button" value="添加描述编辑" class="button orange mr5 desc" />
			<input type="button" value="删除描述编辑" class="button cancel mr5 redesc" />
			<input type="button" value="添加图片热点编辑" class="button orange mr5 coords" />
			<input type="button" value="删除图片热点编辑" class="button cancel mr5 recoords" />
		 </div>
		 <div class="ui-block-line mt5 setSame" >
			<label>同步设置其它列表元素</label>
			<input type="checkbox" class="isSelect" >
			<span>因为您当前编辑的元素属于列表元素，您可以同步设置当前列表其它元素的编辑选项</span>
		 </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="关闭" class="button cancel close"/>	
          <input type="button" value="扩大选区" class="button cancel removeEdit"/>	
     </div>
</div>

<!-- 商品模式 -->
<div class="proto-dialog cms-product-edit-dialog">
    <h5>编辑模板可设置选项</h5>
    <div class="proto-dialog-content">
	     <div class="ui-block-line mt5" >
			<label>预览代码(只读)</label>
			<textarea class="html" placeholder="代码" readonly="readonly" style="width: 600px;height: 280px;resize:none;" loxiaType="input" mandatory="true"></textarea>
		 </div>
          <div class="ui-block-line mt5 product-setSame" >
			<label>同步设置其它列表元素</label>
			<input type="checkbox" class="product-isSelect" >
			<span>因为您当前编辑的元素属于列表元素，您可以同步设置当前列表其它元素的编辑选项</span>
		 </div>
		  <div class="ui-block-line mt5 img">
				<label>图片类型</label>
                <opt:select name="itemImages.type" expression="chooseOption.IMAGE_TYPE" cssClass="img-type"  otherProperties="loxiaType=\"select\" "/>
          </div>
		 <div class="ui-block-line mt5" >
		  	<label class="editSetting">设置编辑选项</label>
			<input type="button" value="商品名称" class="button orange mr5 product-title" />
			<input type="button" value="删除商品名称" class="button cancel mr5 re-product-title" />
			<input type="button" value="商品销售价" class="button orange mr5 product-salesprice" />
			<input type="button" value="删除商品销售价" class="button cancel mr5 re-product-salesprice" />
			<input type="button" value="商品吊牌价" class="button orange mr5 product-listprice" />
			<input type="button" value="删除商品吊牌价" class="button cancel mr5 re-product-listprice" />
			<input type="button" value="商品描述" class="button orange mr5 product-desc" />
			<input type="button" value="删除商品描述" class="button cancel mr5 re-product-desc" />
			<input type="button" value="商品图片" class="button orange mr5 product-img" />
			<input type="button" value="删除商品图片" class="button cancel mr5 re-product-img" />
			<input type="button" value="图片链接" class="button orange mr5 product-href" />
			<input type="button" value="删除图片链接" class="button cancel mr5 re-product-href" />
			<input type="button" value="文本链接" class="button orange mr5 product-text-href" />
			<input type="button" value="删除文本链接" class="button cancel mr5 re-product-text-href" />
		 </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="关闭" class="button cancel close"/>	
          <input type="button" value="扩大选区" class="button cancel removeEdit"/>	
     </div>
</div>
</body>
<script type="text/javascript">
$j(window).load(function(){
	addMapOnClick();
});
</script>
</html>
