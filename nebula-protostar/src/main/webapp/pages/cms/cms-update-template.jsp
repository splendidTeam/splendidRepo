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


 
.comments { 
width:100%;/*自动适应父布局宽度*/  
overflow:auto;  
word-break:break-all;  
} 

</style>


<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${base }/scripts/cms/cms-create-template.js"></script>

<script type="text/javascript">

var id = '<c:out value="${pagetemplate.id }" default="" escapeXml="false"></c:out>';
	
</script>
</head>

<body>
<div class="content-box width-percent100">
    
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/user.png">
		修改页面模板
	</div>
	
<div class="ui-block">	
	<div class="ui-block-title1">页面模板</div>
		<form id="saveTemplate" method="post" action="${base}/cms/savePageTemplate.json" enctype="multipart/form-data">
		<c:if test="${!empty pagetemplate}">
			<div class="ui-block-content border-grey">
		   
		    <div class="ui-block-line ">
		         <label>模板名称</label>
		         <div>
		         	  <input type="hidden" value="${pagetemplate.id }" class="templateId" name="id"/>
		              <input type="text" loxiaType="input" value="${pagetemplate.name}" name="name" mandatory="true" id="template-name"/>
		         </div>
		         <div></div>
		    </div>
		     <div class="ui-block-line color-select-line">
		         <label>支持类型</label>
		         <div>
		         <select loxiaType="select" mandatory="false" class="supportType" supportType="${pagetemplate.supportType}" name="supportType">
		         	<option value="0">综合</option>
					<option value="1">pc</option>
					<option value="2">mobile</option>
                </select>
		         </div>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>模板截图</label>
		         <div>
           				<input loxiaType="input" name="img" value="${pagetemplate.img}" mandatory="false" id="template-img"/>
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
			  		<a onclick="loxia.openPage('/cms/viewhtml.htm?id=${pagetemplate.id}');" href="javascript:void(0)" class="func-button ml5"><span>查看</span> </a>
			  		<span style="line-height: 25px; ">请上传纯文本文件, 如后续为.txt, .html的文件</span>
		         </div>
		    </div>
		    
		    <div class="ui-block-line color-select-line">
		         <label>公共头尾</label>
		         <div>
		         
		          <select loxiaType="select" mandatory="false" name="useCommonHeader">
		         <c:if test="${!pagetemplate.useCommonHeader}">
					<option value="false">否</option>
		         	<option value="true">是</option>
		         </c:if>
		         <c:if test="${pagetemplate.useCommonHeader}">
					<option value="true">是</option>
					<option value="false">否</option>
		         </c:if>
                </select>
		         </div>
		    </div>
		    
			</div>
			</c:if>
		</form>  
	</div>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit template-save" />
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />

    </div>
</div>


<!-- html -->
<div id="dialog-view-html" class="proto-dialog">
    <h5>模板内容</h5>
    <div class="proto-dialog-content p10">
    	
	</div>
    <div class="proto-dialog-button-line center">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
     </div>
</div>

</body>
</html>
