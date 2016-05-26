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
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";
</script>
<script type="text/javascript" src="${base}/scripts/product/item/add-item.js"></script>
<SCRIPT type="text/javascript">
var pdValidCode = "${pdValidCode}";
var zNodes =
[
	{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
	<c:if test="${industry.isShow}">
		{
			id:${industry.id}, 
			pId:${industry.pId},
			name: "${industry.indu_name}",
			open:${industry.open}
			<c:if test="${industry.noCheck}">
				,nocheck:true
			</c:if>
		}
		<c:if test="${!status.last}">,</c:if>
		</c:if>
	</c:forEach>
];

var categoryzNodes  = [
			{id:0, pId:-1, name:"ROOT",	  code:"ROOT", sortNo:1,	  open:true, lifecycle:1},  
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	
              	{id:${category.id}, pId:${category.parentId}, 
              		name:"${category.name}",
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		drag:false, open:false,
              		lifecycle:${category.lifecycle} } 
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];
          var baseUrl='${base}'; 
</SCRIPT>
<script type="text/javascript">
		
		<c:if test="${param.imageUpload==1}">
		editor1.on( 'pluginsLoaded', function(ev)
			{
				if ( !CKEDITOR.dialog.exists( 'myDialog' ) )
				{
					CKEDITOR.dialog.add( 'myDialog', function( editor )
							{
								return {
									title : '图片上传',
									minWidth : 450,
									minHeight : 200,
									contents : [
										{
											id : 'tab1',
											label : 'First Tab',
											title : 'First Tab',
											elements :
											[
												{
													type:'vbox',
													height:'250px',
													children:[
																{	type:'html',
																	style:'width:95%;',
																	html:'<iframe id ="uploadIfr" frameborder="0" name="uploadIfr" width="300" height="100" src="/common/upload.jsp"></iframe>'
																}
															]
												}
											]
										}
									]
								};
							} );
				}
				editor1.addCommand( 'myDialogCmd', new CKEDITOR.dialogCommand( 'myDialog' ) );
				editor1.ui.addButton( 'MyButton',
					{
						label : '图片上传',
						title : '图片上传',
						command : 'myDialogCmd'
					} );
			});
		</c:if>
</script>
<style type="text/css">
.i18n-lang {
	display: none;
}

.cke_button_myDialogCmd .cke_icon {
	display: none !important;
}

.cke_button_myDialogCmd .cke_label {
	display: inline !important;
}
</style>
</head>
<body>

<div class="content-box width-percent100">
    
   <form id="itemForm" name="itemForm" action="/i18n/item/saveItem.json" method="post">
    <input type="hidden" id="industryId" name="itemCommand.industryId"  value=""/>
    <input type="hidden" id="jsonSku" name="itemCommand.jsonSku"  value=""/>
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="item.add.manage"/></div>
		<div class="ui-block">
			<div id="first">
				<div class="ui-block border-grey">
				   <div class="ui-block-title1"><spring:message code="item.add.industry"/></div>
				   <div class="ui-block ui-block-fleft w240">
				        <div class="ui-block-content ui-block-content-lb">
				            <div class="tree-control">
				                <input type="text" id="key" loxiatype="input" placeholder="<spring:message code='item.add.keyword'/>" />
				                <div><span id="search_result"></span></div>
				            </div>
				            <ul id="industrytreeDemo" class="ztree"></ul>
				        </div>
			      </div>
				  <div class="button-line">
			         <input type="button" value="<spring:message code='system.property.next'/>" class="button orange next" title="<spring:message code='system.property.next'/>"/>
			         <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
			         
			      </div>
				</div>
			</div>
			<div id="second" style="display: none">
				<div class="ui-block">
			        <div class="ui-block-content ui-block-content-lb" style="padding-bottom: 10px;">
			            <table>
			                <tr>
			                    <td><label><spring:message code="item.add.selectIndustry"/></label></td>
			                    <td><span id="chooseIndustry"></span></td>
			                    
			                </tr>
			            </table>
			        </div>
			    </div>
				<div class="ui-block ">
					 <div class="ui-block-title1"><spring:message code="item.add.addItem"/></div>
				     <%-- 基本信息 --%>
				     <%@include file="/pages/product/item/add-item-baseInfo.jsp"%>
					 <div style="margin-top: 10px"></div>
					 <%-- 商品价格 --%>
					 <%@include file="/pages/product/item/add-item-price.jsp"%>
				   	 <div style="margin-top: 10px"></div>
				   	 <%-- 一般属性信息 销售属性信息 --%>
					 <%@include file="/pages/product/item/add-item-propertyInfo.jsp"%>
				     <div style="margin-top: 10px"></div>
					 <%-- SEO --%>
				     <%@include file="/pages/product/item/add-item-seo.jsp"%>
				     <div style="margin-top: 10px"></div>
				     <%-- 商品描述 --%>
					 <%@include file="/pages/product/item/add-item-description.jsp"%>
				     <div style="margin-top: 10px"></div>
					 <div class="button-line">
				         <input type="button" value="<spring:message code='btn.save'/>" class="button orange submit" title="<spring:message code='btn.save'/>"/>
				         <input type="button" value="<spring:message code='item.add.previous'/>" class="button back"  title="<spring:message code='item.add.previous'/>" />
					</div>
					<div style="margin-top: 20px"></div>
				</div>
			</div>
		</div>
   </form>
</div>

<div id="menuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:auto; height: 100%;"></ul>
</div>
<div id="defaultMenuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="defaultCategoryTree" class="ztree" style="margin-top:0; width:auto; height: 100%;"></ul>
</div>
</body>
</html>
