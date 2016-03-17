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
<script type="text/javascript" src="${base }/scripts/cms/cms-create-template.js"></script>

</head>

<body>

<div class="content-box width-percent100">
    
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/user.png">
		新增页面模板
	</div>
	
<div class="ui-block">	
	<div class="ui-block-title1">页面模板</div>
		<form id="saveTemplate" method="post" action="${base}/cms/savePageTemplate.json" enctype="multipart/form-data">
			<div class="ui-block-content border-grey">
		   
		    <div class="ui-block-line ">
		         <label>模板名称</label>
		         <div>
		              <input type="text" loxiaType="input" value="" name="name" mandatory="true" id="template-name"/>
		         </div>
		         <div></div>
		    </div>
		    <div class="ui-block-line color-select-line">
		         <label>支持类型</label>
		         <div>
		         <select loxiaType="select" mandatory="false" name="supportType">
		         	<option value="0">综合</option>
					<option value="1">pc</option>
					<option value="2">mobile</option>
                </select>
		         </div>
		    </div>
		    <div class="ui-block-line color-select-line">
		         <label>模板截图</label>
		         <div>
           				<input loxiaType="input" name="img" mandatory="false" id="template-img"/>
           				<a class="func-button ml5 uploadlink" href="javascript:void(0);"><span>上传</span>
           					<input callback="fnCallback" class="imgUploadComponet fileupload" role="" model="C" hName="templateImageUrl"   type="file" url="/demo/upload.json"/>
           				</a>
		           </div>	
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>模板文件</label>
		         <div>
		         <%-- 
		         <textarea loxiaType="input" name="banner-dialog-text" mandatory="true" style="float: left;" id="template-text">
		         </textarea>
		         --%>
			  		<input type="file" name="templateFile" style="width:160px;" accept="text/html, text/plain"/> 
			  		<span style="margin-left: 13px;line-height: 25px; ">请上传纯文本文件, 如后续为.txt, .html的文件</span>
		         </div>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>公共头尾</label>
		         <div>
		         <%--
		         <input type="checkbox" class="inform-check" name="useCommonHeader" checked='false'/>
		          --%>
		         
		         <select loxiaType="select" mandatory="false" name="useCommonHeader">
					<option value="true">是</option>
					<option value="false">否</option>
                </select>
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
