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
<script type="text/javascript" src="${base}/scripts/column/column-home.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript">
	var defaultNonItemImgUrl = '<c:out value="${ defaultNonItemImgUrl }" default="[]" escapeXml="false"></c:out>';
	var customBaseUrl = '<c:out value="${ customBaseUrl }" default="[]" escapeXml="false"></c:out>';
</script>
</head>
<body>
	<div class="content-box width-percent100">
	    <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png">首页版块管理</div>
		<div class="ui-block hidden">
			<div class="ui-block-title1">BANNER管理</div>
			<div class="ui-block-content">
				<form id="banner-form" method="post">
					<input name="moduleCode" type="hidden" value="BANNER"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="banner-content common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'BANNER' }">
								<c:set var="banner_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.img }" vWidth="${ columnCompCommand.imgWidth }" vHeight="${ columnCompCommand.imgHeight }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="链接:${ columnCompCommand.url }"/>
						    		<input name="columnComponents.img" type="hidden"  readonly="true" value="${customBaseUrl }${ columnCompCommand.img }"/>
						    		<input name="columnComponents.url" type="hidden"  readonly="true" value="${ columnCompCommand.url }"/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<input name="columnComponents.imgWidth" type="hidden"  readonly="true" value='${ columnCompCommand.imgWidth }'/>
						    		<input name="columnComponents.imgHeight" type="hidden"  readonly="true" value='${ columnCompCommand.imgHeight }'/>
						    		<input name="columnComponents.ext" type="hidden"  readonly="true" value='${ columnCompCommand.ext }'/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button ifc-update banner-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
					<div class="button-line">
						 <%-- <input loxiaType="date" mandatory="false" showtime="true" placeholder="发布时间" value="${banner_publish_time}" name="publishTime" class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange newBanner" title="" />
				         <input type="button" value="保存" class="button save_banner" title=""/>
				    </div>
			    </form>
			</div>
		</div>

		<div class="ui-block hidden">
			<div class="ui-block-title1">品牌专区</div>
			<div class="ui-block-content">
				<form id="brand-form" method="post">
					<input name="moduleCode" type="hidden" value="BRAND_PREFECTURE"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="brand-content common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'BRAND_PREFECTURE' }">
								<c:set var="brand_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.img }" vWidth="${ columnCompCommand.imgWidth }" vHeight="${ columnCompCommand.imgHeight }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="链接:${ columnCompCommand.url }"/>
						    		<input name="columnComponents.img" type="hidden"  readonly="true" value="${customBaseUrl }${ columnCompCommand.img }"/>
						    		<input name="columnComponents.url" type="hidden"  readonly="true" value="${ columnCompCommand.url }"/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<input name="columnComponents.imgWidth" type="hidden"  readonly="true" value='${ columnCompCommand.imgWidth }'/>
						    		<input name="columnComponents.imgHeight" type="hidden"  readonly="true" value='${ columnCompCommand.imgHeight }'/>
						    		<input name="columnComponents.ext" type="hidden"  readonly="true" value='${ columnCompCommand.ext }'/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button ifc-update banner-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
					<div class="button-line">
						 <%-- <input loxiaType="date" mandatory="false" showtime="true" placeholder="发布时间" value="${banner_publish_time}" name="publishTime" class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange new-brand" title="" />
				         <input type="button" value="保存" class="button save-brand" title=""/>
				    </div>
			    </form>
			</div>
		</div>
		
		<div class="ui-block hidden">
			<div class="ui-block-title1">新品主推活动管理</div>
			<div class="ui-block-content">
				<form id="new-push-activity-form" method="post">
					<input name="moduleCode" type="hidden" value="NEW_PUSH_ACTIVITY"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="new-push-activity-content  common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'NEW_PUSH_ACTIVITY' }">
								<c:set var="new-push-activity_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.img }" vWidth="${ columnCompCommand.imgWidth }" vHeight="${ columnCompCommand.imgHeight }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="链接:${ columnCompCommand.url }"/>
						    		<input name="columnComponents.img" type="hidden"  readonly="true" value="${customBaseUrl }${ columnCompCommand.img }"/>
						    		<input name="columnComponents.url" type="hidden"  readonly="true" value="${ columnCompCommand.url }"/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<input name="columnComponents.imgWidth" type="hidden"  readonly="true" value='${ columnCompCommand.imgWidth }'/>
						    		<input name="columnComponents.imgHeight" type="hidden"  readonly="true" value='${ columnCompCommand.imgHeight }'/>
						    		<input name="columnComponents.ext" type="hidden"  readonly="true" value='${ columnCompCommand.ext }'/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button ifc-update new-push-activity-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
					<div class="button-line">
						 <%-- <input loxiaType="date" mandatory="false" showtime="true" placeholder="发布时间" value="${new-push-activity_publish_time}" name="publishTime" class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange newnew-push-activity" title="" />
				         <input type="button" value="保存" class="button save_new-push-activity" title=""/>
				    </div>
			    </form>
			</div>
		</div>
		
		<div class="ui-block hidden">
			<div class="ui-block-title1">LEVIS商品推荐管理</div>
			<div class="ui-block-content">
 				<form id="levis-form" method="post">
 					<input name="moduleCode" type="hidden" value="LEVIS"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="levis-content common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'LEVIS' }">
								<c:set var="nhp_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="商品名称:${ columnCompCommand.itemCommand.title }"/>
						    		<input type="hidden" name="columnComponents.img" readonly="true" value="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<input name="itemTile" type="hidden" value="${ columnCompCommand.itemCommand.title }"/>
						    		<input name="itemCode" type="hidden" value="${ columnCompCommand.itemCommand.code }"/>
						    		<input name="columnComponents.targetId" type="hidden" value="${ columnCompCommand.targetId }"/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button ifc-update levis-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
				    <div class="button-line">
						 <%-- <input placeholder="发布时间"  loxiaType="date" showtime="true" value="${nhp_publish_time }" name="publishTime" mandatory="false" class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange levis-new" title="" />
				         <input type="button" value="保存" class="button levis-save" title=""/>
				    </div>
			    </form>
			</div>
		</div>

		<div class="ui-block hidden">
			<div class="ui-block-title1">CONVERSE商品推荐管理</div>
			<div class="ui-block-content">
 				<form id="converse-form" method="post">
 					<input name="moduleCode" type="hidden" value="CONVERSE"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="converse-content common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'CONVERSE' }">
								<c:set var="dip_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="商品名称:${ columnCompCommand.itemCommand.title }"/>
						    		<input loxiaType="input" type="hidden" name="sex" readonly="true" value=""/>
						    		<input name="columnComponents.img" type="hidden" readonly="true" value="${customBaseUrl }${ itemCommand.itemImageList[0].picUrl }"/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<%-- 
						    		<input name="columnComponents.title" type="hidden" value="${ columnCompCommand.title }"/>
						    		<input name="columnComponents.description" type="hidden" value="${ columnCompCommand.description }"/>
						    		--%>
						    		<input name="itemTile" type="hidden" value="${ columnCompCommand.itemCommand.title }"/>
						    		<input name="itemCode" type="hidden" value="${ columnCompCommand.itemCommand.code }"/>
						    		<input name="columnComponents.targetId" type="hidden" value="${ columnCompCommand.targetId }"/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button converse-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
				    <div class="button-line">
						 <%-- <input loxiaType="date" mandatory="false" placeholder="发布时间" value="${ dip_publish_time }" name="publishTime" showtime="true"  class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange converse-new" title="" />
				         <input type="button" value="保存" class="button converse-save" title=""/>
				    </div>
			    </form>
			</div>
		</div>

		<div class="ui-block hidden">
			<div class="ui-block-title1">NIKE商品推荐管理</div>
			<div class="ui-block-content">
 				<form id="nike-form" method="post">
 					<input name="moduleCode" type="hidden" value="NIKE"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="nike-content common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'NIKE' }">
								<c:set var="hr_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="商品名称:${ columnCompCommand.itemCommand.title }"/>
						    		<input name="columnComponents.img" type="hidden" readonly="true" value="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/>
						    		<input name="itemTile" type="hidden" value="${ columnCompCommand.itemCommand.title }"/>
						    		<input name="itemCode" type="hidden" value="${ columnCompCommand.itemCommand.code }"/>
						    		<input name="html" type="hidden" value=""/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<input name="columnComponents.targetId" type="hidden" value="${ columnCompCommand.targetId }"/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button ifc-update nike-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
				    <div class="button-line">
						 <%-- <input loxiaType="date" mandatory="false" placeholder="发布时间" value="${ hr_publish_time }" name="publishTime" showtime="true" class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange nike-new" title="" />
				         <input type="button" value="保存" class="button nike-save" title=""/>
				    </div>
			    </form>
			</div>
		</div>

		<div class="ui-block hidden">
			<div class="ui-block-title1">JORDAN商品推荐管理</div>
			<div class="ui-block-content">
 				<form id="jordan-form" method="post">
 					<input name="moduleCode" type="hidden" value="JORDAN"/>
					<input name="pageCode" type="hidden" value="HOME_PAGE"/>
					<div class="jordan-content common-line">
						<c:forEach items="${ columnCompCommandList }" var="columnCompCommand">
							<c:if test="${ columnCompCommand.moduleCode == 'JORDAN' }">
								<c:set var="hr_publish_time">
									<fmt:formatDate value='${ columnCompCommand.publishTime }' pattern='yyyy-MM-dd HH:mm'/>
								</c:set>
								<div class="img-float idis${ columnCompCommand.id }">
						    		<table cellspacing="0" cellpadding="0" border="0" class="mc-img-tb"><tr><td><img src="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/></td></tr></table>
						    		<input loxiaType="input" readonly="true" value="商品名称:${ columnCompCommand.itemCommand.title }"/>
						    		<input name="columnComponents.img" type="hidden" readonly="true" value="${customBaseUrl }${ columnCompCommand.itemCommand.itemImageList[0].picUrl }"/>
						    		<input name="itemTile" type="hidden" value="${ columnCompCommand.itemCommand.title }"/>
						    		<input name="itemCode" type="hidden" value="${ columnCompCommand.itemCommand.code }"/>
						    		<input name="html" type="hidden" value=""/>
						    		<input name="columnComponents.id" type="hidden" value="${ columnCompCommand.id }"/>
						    		<input name="columnComponents.targetId" type="hidden" value="${ columnCompCommand.targetId }"/>
						    		<div class="if-control-c">
						    			<a href="javascript:void(0)" class="ifc-button ifc-update jordan-update">编辑</a>
						    			<a href="javascript:void(0)" class="ifc-button ifc-delete">删除</a>
						    		</div>
					    		</div>
				    		</c:if>
						</c:forEach>
					</div>
				    <div class="button-line">
						 <%-- <input loxiaType="date" mandatory="false" placeholder="发布时间" value="${ hr_publish_time }" name="publishTime" showtime="true" class="fLeft mt5"/> --%>
					     <input type="button" value="新增" class="button orange jordan-new" title="" />
				         <input type="button" value="保存" class="button jordan-save" title=""/>
				    </div>
			    </form>
			</div>
		</div>
		
		<div class="button-line">
		     <input type="button" value="现在发布" class="button orange now-publish" title="" />
	         <input type="button" value="预览" class="button " title="" onclick="loxia.openPage('${columnBrowseUrl}/preview?pageCode=HOME_PAGE',null,null,[1200,450])"/>
	    </div>
	</div>    
	    
<%-- **********************************************BANNER DIALOG********************************************** --%>
	<div class="banner-dialog proto-dialog">
		 <h5>编辑信息</h5>
	 	 <input type="hidden" id="banner-columnComp-module-id" value="" />
		 <div class="proto-dialog-content">
		 	 <input type="hidden" id="banner-flag" value="" />
		 	 <input type="hidden" id="banner-columnComp-id" value="" />
		 	 <input type="hidden" name="vWidth"/>
 			 <input type="hidden" name="vHeight"/>
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }" class="image-url" name="bannerImg" width="130px" height="130px"/></div>
			 <div class="ui-block-line pc-image" style="height: 45px;">
				<label style="margin-top: 10px">PC端图片：</label>
				<div class="color-select-line" style="position: absolute;">
        			<input loxiaType="input" name="banner-pc-dialog-url"/>
         			<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
         				<%-- 如果只想上传PC端图片, 请在这里给role赋上相应的尺寸[如:120X120|65X65] 前者写平板的尺寸, 后者写手机的尺寸, PC尺寸为原图 ;--%>
         				<%-- 如果没有平板尺寸, 请写成[|65X65] --%>
         				<%-- 如果没有手机尺寸, 请写成[120X120|] --%>
         				<%-- 120X120 [X是大写的x]  --%>
         				<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="bannerPCImg" type="file" url="/demo/upload.json"/>
         			</a>
         			<a class="func-button ml5 view-img" href="javascript:void(0);">
	         			<span>查看</span>
         			</a>
         		</div>		
			 </div>	
			 <div class="ui-block-line tablet-image" style="height: 45px;">
				<label style="margin-top: 10px">平板端图片：</label>
				<div class="color-select-line" style="position: absolute;">
        			<input loxiaType="input" name="banner-tablet-dialog-url"/>
         			<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
         				<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="bannerTabletImg" type="file" url="/demo/upload.json"/>
         			</a>
         			<a class="func-button ml5 view-img" href="javascript:void(0);">
	         			<span>查看</span>
         			</a>
         		</div>		
			 </div>	
			 <div class="ui-block-line mobile-image" style="height: 45px;">
				<label style="margin-top: 10px">手机端图片：</label>
				<div class="color-select-line" style="position: absolute;">
        			<input loxiaType="input" name="banner-mobile-dialog-url"/>
         			<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
         				<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="bannerMobileImg" type="file" url="/demo/upload.json"/>
         			</a>
         			<a class="func-button ml5 view-img" href="javascript:void(0);">
	         			<span>查看</span>
         			</a>
         		</div>		
			 </div>	
			 <div class="ui-block-line">
				<label>链接地址：</label>
				<div>    
					<input loxiaType="input" value="" mandatory="false" name="url" class="banner-link"/>
				</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 banner_confirm"/>
		 	  <input type="button" value="取消" class="button cencal"/>
		 </div>
	</div>
<%---------------------------------------品牌专区 DIALOG----------------------------------------------------------%>
	<div class="brand-dialog proto-dialog">
		 <h5>编辑信息</h5>
	 	 <input type="hidden" id="brand-columnComp-module-id" value="" />
		 <div class="proto-dialog-content">
		 	 <input type="hidden" id="brand-flag" value="" />
		 	 <input type="hidden" id="brand-columnComp-id" value="" />
		 	 <input type="hidden" name="vWidth"/>
 			 <input type="hidden" name="vHeight"/>
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }" class="image-url" name="bannerImg" width="130px" height="130px"/></div>
			 <div class="ui-block-line pc-image" style="height: 45px;">
				<label style="margin-top: 10px">PC端图片：</label>
				<div class="color-select-line" style="position: absolute;">
        			<input loxiaType="input" name="brand-pc-dialog-url"/>
         			<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
         				<%-- 如果只想上传PC端图片, 请在这里给role赋上相应的尺寸[如:120X120|65X65] 前者写平板的尺寸, 后者写手机的尺寸, PC尺寸为原图 ;--%>
         				<%-- 如果没有平板尺寸, 请写成[|65X65] --%>
         				<%-- 如果没有手机尺寸, 请写成[120X120|] --%>
         				<%-- 120X120 [X是大写的x]  --%>
         				<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="brandPCImg" type="file" url="/demo/upload.json"/>
         			</a>
         			<a class="func-button ml5 view-img" href="javascript:void(0);">
	         			<span>查看</span>
         			</a>
         		</div>		
			 </div>	
			 <div class="ui-block-line tablet-image" style="height: 45px;">
				<label style="margin-top: 10px">平板端图片：</label>
				<div class="color-select-line" style="position: absolute;">
        			<input loxiaType="input" name="brand-tablet-dialog-url"/>
         			<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
         				<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="brandTabletImg" type="file" url="/demo/upload.json"/>
         			</a>
         			<a class="func-button ml5 view-img" href="javascript:void(0);">
	         			<span>查看</span>
         			</a>
         		</div>		
			 </div>	
			 <div class="ui-block-line mobile-image" style="height: 45px;">
				<label style="margin-top: 10px">手机端图片：</label>
				<div class="color-select-line" style="position: absolute;">
        			<input loxiaType="input" name="brand-mobile-dialog-url"/>
         			<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
         				<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="brandMobileImg" type="file" url="/demo/upload.json"/>
         			</a>
         			<a class="func-button ml5 view-img" href="javascript:void(0);">
	         			<span>查看</span>
         			</a>
         		</div>		
			 </div>	
			 <div class="ui-block-line">
				<label>链接地址：</label>
				<div>    
					<input loxiaType="input" value="" mandatory="false" name="url" class="brand-link"/>
				</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 brand-confirm"/>
		 	  <input type="button" value="取消" class="button cencal"/>
		 </div>
	</div>
<%-- **********************************************new_push_activity DIALOG********************************************** --%>
	<div class="new-push-activity-dialog proto-dialog">
		 <h5>编辑信息</h5>
	 	 <input type="hidden" id="new-push-activity-columnComp-module-id" value="" />
		 <div class="proto-dialog-content">
		 	 <input type="hidden" id="new-push-activity-flag" value="" />
		 	 <input type="hidden" id="new-push-activity-columnComp-id" value="" />
		 	 <input type="hidden" name="vWidth"/>
 			 <input type="hidden" name="vHeight"/>
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }" class="image-url" name="bannerImg" width="130px" height="130px"/></div>
			 <div class="ui-block-line pc-image" style="height: 45px;">
					<label style="margin-top: 10px">PC端图片：</label>
					<div class="color-select-line" style="position: absolute;">
           				<input loxiaType="input" name="banner-pc-dialog-url"/>
           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
	         				<%-- 如果只想上传PC端图片, 请在这里给role赋上相应的尺寸[如:120X120|65X65] 前者写平板的尺寸, 后者写手机的尺寸, PC尺寸为原图 ;--%>
	         				<%-- 如果没有平板尺寸, 请写成[|65X65] --%>
	         				<%-- 如果没有手机尺寸, 请写成[120X120|] --%>
	         				<%-- 120X120 [X是大写的x]  --%>
           					<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="newPushPCImg" type="file" url="/demo/upload.json"/>
           				</a>
           				<a class="func-button ml5 view-img" href="javascript:void(0);">
		         			<span>查看</span>
	         			</a>
           			</div>
			 </div>	
			 <div class="ui-block-line tablet-image" style="height: 45px;">
					<label style="margin-top: 10px">平板端图片：</label>
					<div class="color-select-line" style="position: absolute;">
           				<input loxiaType="input" name="banner-tablet-dialog-url"/>
           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
           					<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="newPushTabletImg" type="file" url="/demo/upload.json"/>
           				</a>
           				<a class="func-button ml5 view-img" href="javascript:void(0);">
		         			<span>查看</span>
	         			</a>
           			</div>
     			</div>	
			 <div class="ui-block-line mobile-image" style="height: 45px;">
					<label style="margin-top: 10px">手机端图片：</label>
					<div class="color-select-line" style="position: absolute;">
           				<input loxiaType="input" name="banner-mobile-dialog-url"/>
           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span><spring:message code="item.image.browse" /></span>
           					<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="newPushMobileImg" type="file" url="/demo/upload.json"/>
           				</a>
           				<a class="func-button ml5 view-img" href="javascript:void(0);">
		         			<span>查看</span>
	         			</a>
           			</div>		
			 </div>	
			 <div class="ui-block-line">
					<label>链接地址：</label>
					<div>    
						<input loxiaType="input" value="" mandatory="false" name="url" class="new-push-activity-link"/>		    			
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 new-push-activity_confirm"/>
		 	  <input type="button" value="取消" class="button cencal"/>
		 </div>
	</div>
<%-- **********************************************LEVIS商品推荐管理 DIALOG********************************************** --%>	
	<div class="levis-dialog proto-dialog">
		 <h5>编辑信息</h5>
	 	 <input type="hidden" id="levis-columnComp-module-id" value="" />
		 <div class="proto-dialog-content">
		 	 <input type="hidden" id="levis-flag" value="" />
		 	 <input type="hidden" id="levis-columnComp-id" value="" />
		 	 <input type="hidden" name="itemId" value="" />
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }" width="150px" height="150px"/></div>
			  <div class="ui-block-line">
					<label>商品名称：</label>
					<div >    
						<span class="text"></span>
					</div>
			 </div>	
			 <div class="ui-block-line">
					<label>商品code：</label>
					<div>    
						<input loxiaType="input" value="" name="levis-dialog" class="levis-dialog-code" mandatory="false"/>
					</div>
			 </div>	
			 <div class="ui-block-line propmt" style="display:none;">
					<div style="color: red;">    
						<span>商品Code不存在!</span>
					</div>
			 </div>	
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 levis-dialog-confirm disabled"/>
		 	  <input type="button" value="取消" class="button cencal"/>	
		 </div>
	</div>

<%-- ********************************************** CONVERSE商品推荐管理 DIALOG********************************************** --%>	
	<div class="converse-dialog proto-dialog">
		 <h5>编辑信息</h5>
		 <input type="hidden" id="dip-columnComp-module-id" value="" />
		 <div class="proto-dialog-content">
 		 	 <input type="hidden" id="converse-flag" value="" />
		 	 <input type="hidden" id="converse-columnComp-id" value="" />
		 	 <input type="hidden" name="itemId" value="" />
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }" width="150px" height="150px"/></div>
			 <div class="ui-block-line">
					<label>商品名称：</label>
					<div >    
						<span class="text"></span>
					</div>
			 </div>	
			 <div class="ui-block-line">
					<label>商品code：</label>
					<div >    
						<input loxiaType="input" value="" class="converse-dialog-code" name="converse-dialog"  mandatory="false"/>		    			
					</div>
			 </div>
			 <div class="ui-block-line propmt" style="display:none;">
					<div style="color: red;">    
						<span>商品Code不存在!</span>
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 converse-confirm disabled"/>
		 	  <input type="button" value="取消" class="button cencal" />	
		 </div>
	</div>

<%-- *********************************************NIKE商品推荐管理 DIALOG********************************************** --%>	
	<div class="nike-dialog proto-dialog">
		 <h5>编辑信息</h5>
		 <div class="proto-dialog-content">
		 	 <input type="hidden" id="nike-flag" value="" />
		 	 <input type="hidden" name="columnComp-id" value="" />
		 	 <input type="hidden" name="module-id" value="" />
		 	 <input type="hidden" name="itemId" value="" />
		 	 <input type="hidden" name="itemCode" value="" />
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }"  width="150px" height="150px"/></div>
			  <div class="ui-block-line">
					<label>商品名称：</label>
					<div >    
						<span class="text"></span>
					</div>
			 </div>	
			 <div class="ui-block-line">
					<label>商品code：</label>
					<div >    
						<input loxiaType="input" value="" class="nike-dialog-code" name="nike-dialog"  mandatory="false"/>
					</div>
			 </div>	
			 <div class="ui-block-line propmt" style="display:none;">
					<div style="color: red;">    
						<span>商品Code不存在!</span>
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 nike-dialog-confirm disabled" />
		 	  <input type="button" value="取消" class="button cencal"/>	
		 </div>
	</div>


<%-- **********************************************JORDAN商品推荐管理  DIALOG********************************************** --%>	
	<div class="jordan-dialog proto-dialog">
		 <h5>编辑信息</h5>
		 <div class="proto-dialog-content">
		 	 <input type="hidden" id="jordan-flag" value="" />
		 	 <input type="hidden" name="columnComp-id" value="" />
		 	 <input type="hidden" name="module-id" value="" />
		 	 <input type="hidden" name="itemId" value="" />
		 	 <input type="hidden" name="itemCode" value="" />
		 	 <div class="width-percent100 center pt15 pb15"><img src="${ defaultNonItemImgUrl }"  width="150px" height="150px"/></div>
			  <div class="ui-block-line">
					<label>商品名称：</label>
					<div >    
						<span class="text"></span>
					</div>
			 </div>	
			 <div class="ui-block-line">
					<label>商品code：</label>
					<div >    
						<input loxiaType="input" value="" class="jordan-dialog-code" name="jordan-dialog"  mandatory="false"/>
					</div>
			 </div>	
			 <div class="ui-block-line propmt" style="display:none;">
					<div style="color: red;">    
						<span>商品Code不存在!</span>
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确定" class="button orange mr5 jordan-dialog-confirm disabled" />
		 	  <input type="button" value="取消" class="button cencal"/>	
		 </div>
	</div>



<%-- **********************************************查看图片的DIALOG BEGIN********************************************** --%>	
	<div class="image-view-dialog proto-dialog">
		<%-- <h5>图片信息</h5> --%>
		<div class="proto-dialog-content">
			<table class="mc-img-tb" style="width: 500px; height: 500px">
				<tr><td><img src="" class="image-view" /></td></tr>
			</table>
		</div>
		
		<div class="proto-dialog-button-line">
			<%-- <input type="button" value="取消" class="button cencal-view"/>	 --%>
		</div>
	</div>
<%-- **********************************************查看图片的DIALOG END********************************************** --%>

</body>
</html>
