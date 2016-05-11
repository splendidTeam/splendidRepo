<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>页面实例管理</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<style type="text/css">
*{margin:0;padding:0;list-style-type:none;}
body{font-family:Arial;font-size:12pt;color:#333;}
h1{font-size:16pt;}
h2{font-size:13pt;}
/* demo */
.demo{padding:20px;width:800px;margin:20px auto;border:solid 1px black;}
.demo h2{margin:30px 0 20px 0;color:#3366cc;}
/* dragfunction */
.dragfunction{margin:40px 0 0 0;}
.dragfunction dt{height:30px;font-weight:800;}
.dragfunction dd{line-height:22px;padding:0 0 20px 0;color:#5e5e5e;}
/* dragsort */
.dragsort-ver li{height:30px;line-height:30px;}
.dragsort{width:350px;list-style-type:none;margin:0px;}
.dragsort li{float:left;padding:5px;width:100px;height:100px;}
.dragsort div{width:90px;height:50px;border:solid 1px black;background-color:#E0E0E0;text-align:center;padding-top:40px;}
.placeHolder div{background-color:white!important;border:dashed 1px gray!important;}
</style>
<style type="text/css">
.cms-imgarticle-edit-dialog .proto-dialog-content input{
width: 400px;
}
.wl-right-auto2
{


border:1px solid #ccc;
overflow-y:scroll;

}
.slideToggle{
	display: none;
}
.zkss{
font-size: 16px;
margin-bottom: 12px;
margin-top: 4px
}
</style>
<style type="text/css">#element-list div { cursor: pointer; }</style>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/module/module-instance-add.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery.form.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">新建模块实例
		<input type="button" value="返回" class="button butch return" title="返回"/>
		<input type="button" value="预览" class="button orange preview" title="预览"/>
		<input type="button" value="发布" class="button orange publish" title="发布"/>
		<input type="button" value="保存" class="button orange save" title="保存"/>
	</div>
	<div class="ui-block">
		<input type="hidden" id="isSaved" value="${isUpdate}" />
		<div class="ui-block-content-lb" style="padding-bottom: 10px;" >
			<table style="font-size: 14px;">
				<tr><td>所属模板:&nbsp;&nbsp;</td><td>${cmt.name}</td></tr>
			</table>
		</div>
	</div>
	
	<form id="pageInstanceForm" name="pageInstanceForm" action="${base}/module/saveModuleInstance.json" method="post">
		<input type="hidden" name="templateId" id="templateId" value="${cmt.id}" />
		<input type="hidden" name="html" id="html" value="" />
		<input type="hidden" name="id" id="moduleId" value="${cmi.id}" />
		<div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
				<table>
					<tr>
						<td><label>模块编码</label></td>
						<td><input type="text" loxiaType="input" mandatory="true" id="code" name="code" placeholder="模块编码" value="${cmi.code}" checkmaster="checkPageInstanceCode" /></td>
						<td><label>模块名称</label></td>
						<td><input type="text" loxiaType="input" mandatory="true" id="name" name="name" placeholder="模块名称" value="${cmi.name}" checkmaster="checkPageName"/></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
		
    <div class="ui-block">
    	<%-- 编辑页面区域 --%>
    	<script type="text/javascript" src="${base}/scripts/cms/cms-ztree.js" ></script>
    	<script type="text/javascript" src="${base}/scripts/z-model-product.js" ></script>
		<script type="text/javascript" src="${base}/scripts/z-model.js" ></script>
		<iframe src="${base}/page/findTemplateModuleAreaByTemplateId.htm?templateId=${cmt.id}&moduleId=${cmi.id}&isEdit=true" width="100%" class="web-update" frameborder="0"></iframe>
    </div>
    <div class="button-line">
		<input type="button" value="保存" class="button orange save" title="保存"/>
		<input type="button" value="发布" class="button orange publish" title="发布"/>
		<input type="button" value="预览" class="button orange preview" title="预览"/>
		<input type="button" value="返回" class="button butch return" title="返回"/>
    </div>
</div>
    
<div class="cms-html-edit-dialog proto-dialog">
     <h5>编辑区域—html方式</h5>
     <div class="proto-dialog-content">
     <input type="hidden" value="" id="areaCode" name="areaCode">
         <div class="ui-block-line">
         	<textarea loxiaType="input" id="areaHtml" name="areaHtml" class="page-instance-add-area-html"></textarea>
         </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="确定" class="button orange mr5 confrim-html" />
          <input type="button" value="重置"  class="button orange mr5  recover"  />
          <input type="button" value="显示"  class="button orange mr5  show-area"/>
          <input type="button" value="隐藏"  class="button orange mr5  hide-area"/>
          <input type="button" value="关闭" class="button cencal"/>	
     </div>
</div>

<div class="cms-imgarticle-edit-dialog proto-dialog">
     <h5>编辑区域—表单方式</h5>
     <div class="proto-dialog-content">
    
	     <input type="hidden" value="" id="areaCode" name="areaCode">
	     <ul id="element-list">
	    	<li class="li-area-list">
			    <div class="ui-block-line area-list list-line" style="display: none;margin-bottom: 5px">
			     <div class="zkss"><a href="javascript:void(0);" class="func-button select">收起-</a></div>
			     <div class="slideToggle">
			     	  <div class="zTreeDemoBackground left">
						<ul class="ztree"></ul>
					  </div>
				      <div class="ui-block-line mt5 title" style="display: none;" >
						<label>标题</label>
						<input  class="ismandatory" placeHolder="标题" loxiaType="input" mandatory="true" />
					  </div>
					  <div class="ui-block-line mt5 href" style="display: none;">
						<label>超链接</label>
						<input  class="ismandatory" placeHolder="链接" loxiaType="input" mandatory="true"  />
					  </div>
					  
					  <div class="ui-block-line mt5 coords" style="display: none;" >
						<label>图片热点</label>
						<input  class="ismandatory" placeHolder="图片热点" loxiaType="input" mandatory="true" />
					  </div>
					  
					  <div class="ui-block-line mt5 img" style="display: none;">
						<label>图片</label>
					 	<input  class="img-url ismandatory"  placeHolder="图片" loxiaType="input" mandatory="true" />
						<a  class="func-button ml5 uploadlink color-select-line" href="javascript:void(0);"><span>上传</span>
		           			<input style="width: 40px;margin-left: 360px;" callback="fnCallback" class="imgUploadComponet fileupload"  role="" model="C" hName="templateImageUrl"  type="file" url="/demo/upload.json"/>
		           		</a>
					  </div>
					  
					  <div class="ui-block-line mt5 img-alt" style="display: none;">
						<label>图片alt</label>
					 	<input  placeHolder="图片alt" loxiaType="input" />
					  </div>
					  
					  <div class="ui-block-line mt5 desc" style="display: none;">
							<label>描述</label>
							<textarea class="ismandatory" placeholder="描述" style="width: 400px;height: 100px;resize:none;" loxiaType="input" mandatory="true"></textarea>
					  </div>
					  
					  <div class="ui-block-line mt5 " >
							<input num="" style="width: 44px;height: 33px;margin-left: 250px; margin-bottom: 10px" type="button" value="删除" class="button remove"/>	
					  </div>
				  </div>
				</div>
			</li>
		</ul>
		  <div class="ui-block-line mt5 imgarticle-info center" style="display: none;">
				<span>该区域下没有设置任何编辑项</span>
		  </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="确定" class="button orange mr5 confrim-imgarticle" />
          <input type="button" value="添加"  class="button orange mr5 add-tmp" />
          <input type="button" value="重置"  class="button orange mr5  recover"  />
          <input type="button" value="显示"  class="button orange mr5  show-area"/>
          <input type="button" value="隐藏"  class="button orange mr5  hide-area"/>
          <input type="button" value="关闭" class="button cencal"/>	
     </div>
</div>

<div class="cms-product-edit-dialog proto-dialog">
     <h5>编辑区域—商品方式</h5>
     <div class="proto-dialog-content">
     <input type="hidden" value="" id="areaCode"  class="areaCode" name="areaCode">
     <ul id="item-element-list">
	    <li class="li-area-list">
	      <div class="ui-block-line area-list list-line" style="display: none;margin-bottom: 5px">
		     <div class="zkss"><a href="javascript:void(0);" class="func-button select">收起-</a></div>
		     <div class="slideToggle">
	      	 	<div class="ui-block-line mt5"  style="margin-bottom: 20px">
					<label style="margin-left: 20px;" >商品编码</label>
					<input  class="ismandatory productCode" placeHolder="商品编码" loxiaType="input" readonly="readonly" mandatory="true" />
					<a class="func-button ml5 color-select-line product-select" href="javascript:void(0);">选择
	           		</a>
		     	</div>
		     	<div class="ui-block-line mt5 " >
					<input num="" style="width: 44px;height: 33px;margin-left: 140px; margin-bottom: 10px" type="button" value="删除" class="button item-remove"/>	
				</div>
	       </div>   
	      </div>
     	</li>
     </ul>
	 </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="确定" class="button orange mr5 product-confrim" />
          <input type="button" value="添加"  class="button orange mr5 add-item-tmp" />
          <input type="button" value="重置"  class="button orange mr5  recover"  />
          <input type="button" value="显示"  class="button orange mr5  show-area"/>
          <input type="button" value="隐藏"  class="button orange mr5  hide-area"/>
          <input type="button" value="关闭" class="button cencal"/>	
     </div>
</div>

<%-- 商品选择器 --%>
<div id="dialog-item-select" class="proto-dialog">
	<h5><spring:message code="product.item.scope.select"/></h5>
	<div class="proto-dialog-content p10">
	<form id="searchForm">
	    <div class="ui-block">
	    	<div class="ui-block-content ui-block-content-lb">
	            <table>
	                <tr>
	               	 	<td>
	                    <label>商品名称</label>
	                    </td>
	                    <td>
	                    	<input loxiatype="input" name="q_sl_title" placeholder=""/>
	                    </td>
	                    <td>
	                    <label><spring:message code="item.code"/></label>
	                    </td>
	                    <td>
	                    	<input loxiatype="input" name="q_string_code" placeholder=""/>
	                    </td>
	                       
	                    <td><label><spring:message code="item.filter.brand"/></label>
	                    	</td>
	                    <td>
	                    	<input loxiatype="input" placeholder=""/>
	                    	</td>
	                </tr>
	                <tr>
	                	<td>
	                    <label>是否主卖品</label>
	                    </td>
	                    <td>
	                    	<select loxiaType="select" mandatory="false" id="couponType"name="q_long_type">
	                    		<option value="2">全部</option>
	                    		<option value="1">主卖品</option>
	                    		<option value="0">赠品</option>
	                    	</select>
	                    </td>
	                    <td><label><spring:message code="item.filter.item.price"/></label>
	                    	</td>
	                    <td>
	                    	<input loxiatype="input" name="q_long_minPrice" placeholder=""/>
	                        </td>
	                    <td align="center"><label>——</label></td>
	                         <td>
	                    <input loxiatype="input" name="q_long_maxPrice" placeholder=""/>
	                        </td>
	                    </tr>
	                </table>
	        <div class="button-line1">
	                <a href="javascript:void(0);"
	                   class="func-button search"><span><spring:message code="product.property.lable.search"/></span></a>
	                   
	        </div>
		 </div>
		</div>
	   </form>
	<div class="table-border0 border-grey" id="table1" caption="<spring:message code='item.list.itemList'/>"></div>  
	</div>
   <div class="proto-dialog-button-line center">
		<%-- <input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" />  --%>
 </div>
</div>

<input type="hidden" id="type" value="module" />
</body>
</html>
