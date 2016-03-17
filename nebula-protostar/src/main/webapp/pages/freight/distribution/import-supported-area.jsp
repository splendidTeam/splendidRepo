<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript">
var $ = jQuery.noConflict();
</script>

<link rel="stylesheet" type="text/css" href="${base}/scripts/uploadify3/uploadify.css" media="screen" />
<script type="text/javascript" src="${base}/scripts/uploadify3/jquery.uploadify.min.js"></script>
<script type="text/javascript" src="${base }/scripts/freight/distribution/import-supported-area.js"></script>
<script type="text/javascript">
	var distributionId = '<c:out value="${distributionId }" escapeXml="" default=""></c:out>';
</script>
</head>

<body>

<div class="content-box width-percent100">
    
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/globe_3.png">
		支持区域导入
	</div>
	
	<div class="ui-block">	
		  <div class="ui-block-title1">支持区域导入</div>
		  <div class="ui-block-content border-grey">
			  <div class="ui-block-line ">
			        <label >模板文件</label>
			        <c:if test="${existArea }">
			        	<input type="button" id="exportTmplOfAreaInfo" value="导出数据">
			        </c:if>
			        <c:if test="${!existArea }">
						<input type="button" id="downLoadTmplOfAreaInfo" value="下载模版">
			        </c:if>
					<input type="hidden" id="distributionId"  value="${distributionId }" />
			  </div>
		 </div>
			
			
			<div style="margin-top: 10px"></div>
			<div class="ui-block-content border-grey">
			   <div class="ui-block-line ">
			        <label style="">区域导入</label>
					<div id="area-upload">
					</div>
					<p style="margin-top: 10px;">
						<a id="btn-ok" class="func-button" href="javascript:void(0)">确认</a>
						<a id="btn-cancel" class="func-button" href="javascript:void(0)">取消</a>
					</p>
					<p style="margin-top: 5px; font-size: 14px; font-weight: bold;">
						<span id="upload-area-result"></span>
					</p>
					<div id="upload-area-message" class="upload-message"></div>
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
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />

    </div>
</div>


</body>
</html>
