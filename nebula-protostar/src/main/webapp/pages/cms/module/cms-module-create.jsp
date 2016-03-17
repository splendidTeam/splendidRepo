<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<style type="text/css">
.wl-right-auto2
{
border:1px solid #ccc;
overflow-y:scroll;
}
.wl-right-auto2  input[type="checkbox"]
{
clear:both;
}

.wl-right-auto2  input[type="radio"]
{
clear:both;
}
.wl-right-auto2  p
{
width:80%;
}
</style>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${base }/scripts/cms/module/cms-module-create.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/user.png">
		新增模块
	</div>
<div class="ui-block">	
	<div class="ui-block-title1">页面模块</div>
		<form id="saveTemplate" method="post" action="${base}/cmsModuleTemplate/save.json" enctype="multipart/form-data" >
			<div class="ui-block-content border-grey">
		   
		    <div class="ui-block-line ">
		         <label>模块名称</label>
		         <div>
		        	  <input type="hidden" value="${cmt.id}"  name="id"/>
		              <input type="text" loxiaType="input" value="${cmt.name}" name="name" mandatory="true" />
		         </div>
		         <div></div>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>模块截图</label>
		         <div>
        				<input loxiaType="input" name="img" value="${cmt.img}"  readonly="readonly" mandatory="true" id="template-img"/>
        				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>上传</span>
        					<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="templateImageUrl"   type="file" url="/demo/upload.json"/>
        				</a>
		           </div>	
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>模块文件</label>
		         <div>
			  		<input type="file" name="templateFile" style="width:160px;" accept="text/html, text/plain"/>
			  		<c:if test="${id != null}">
			  			<a onclick="loxia.openPage('/cmsModuleTemplate/view.htm?id=${cmt.id}');" href="javascript:void(0)" class="func-button ml5"><span>查看</span> </a>
			  		</c:if>
			  		<span style="margin-left: 13px;line-height: 25px; ">请上传纯文本文件, 如后续为.txt, .html的文件</span>
		         </div>
		    </div>
		    
			</div>
		</form>  
	</div>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit template-save" />
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />

    </div>
</div>

</body>
</html>
