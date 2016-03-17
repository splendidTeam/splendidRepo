<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" type="text/css" href="${base}/css/demo.css" />
<style type="text/css">
.marg-top {
	margin-top: 20px;
}

.template-class {
	margin-top:20px;
	border: 1px solid #dedede;
	width: 350px;
	height: auto;
	vertical-align: middle;
	padding-bottom: 24px;
	padding-left: 10px;
	padding-top: 20px;
}

input {
	vertical-align: middle;
	margin: 0;
	padding: 0
}

.txt {
	height: 22px;
	border: 1px solid #cdcdcd;
	width: 180px;
}

.btn {
	background-color: #FFF;
	border: 1px solid #CDCDCD;
	height: 24px;
	width: 70px;
}

.file {
	top: 0;
	right: 80px;
	height: 24px;
	filter: alpha(opacity :     0);
	opacity: 0;
	width: 15px
}
</style>
<script type="text/javascript" src="${base}/scripts/cms/cms-definition-install.js"></script>

</head>
<body>
	<div class="content-box width-percent100">

		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/users.png"><spring:message code="definition.install"/>
		</div>
		<div class="ui-tag-change">
			<div class="tag-change-content">
				<div class="tag-change-in">
					<form name="installForm"
						action="/cms/installDefinition.json" method="post" enctype="multipart/form-data">
						<div class="ui-block">
							<div class="ui-block-title1"><spring:message code="definition.install"/></div>
							<div align="center">
								<div class="marg-top">
									<div>
										<input type='text' name='componentFilePath'
											id='componentFilePath' class='txt' value=""/> <input type='button'
											class='btn' id="previewFile" value='<spring:message code="preview.file"/>' /> <input
											type="file" name="componentFile" id="componentFile"
											class="file" size="28"
											onchange='document.getElementById("componentFilePath").value=this.value' />
									</div>
								</div>

								<div class="marg-top">
									<input type="button"  value="<spring:message code="install"/>" class="button orange save"
										style="width: 120px;" title="<spring:message code="install"/>" />
								</div>
								<div class="template-class">
								<table cellspacing="10" >
									<tr>
										<td>
										<spring:message code="install.definition.please.wait"/>
										</td>
									</tr>
									<tr>
										<td>
										<spring:message code="install.definition.please.check"/>
										</td>
									</tr>
									<tr>
										<td>
										<spring:message code="install.definition.if.cover"/>
										</td>
									</tr>
									<tr>
										<td>
										<spring:message code="install.definition.management"/>
										</td>
									</tr>
								</table>
								</div>
							</div>
						</div>
						<div class="button-line"></div>
					</form>
				</div>
			</div>
		</div>
</div>

</body>
</html>
