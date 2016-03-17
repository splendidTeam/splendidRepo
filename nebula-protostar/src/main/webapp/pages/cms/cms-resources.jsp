<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet"
	href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript"
	src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/cmsTree.js"></script>
<script type="text/javascript"
	src="${base}/scripts/cms/cms-resources.js"></script>
<%-- <script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script> --%>
<script type="text/javascript">
	
	var fileType="${fileType}";
	var folderType="${folderType}";
	
</script>
</head>
<body>
	<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="../images/wmi/blacks/32x32/wrench_plus_2.png"><spring:message code="cms.resource.management"/>
		</div>
		<div class="ui-block ui-block-fleft w240">
			<div class="ui-block-content ui-block-content-lb">
				<div class="tree-control">
					<input type="text" id="key" loxiatype="input"
						placeholder="<spring:message code="item.search.keyword"/>" />
					<div>
						<span id="search_result"></span>
					</div>
				</div>
				<ul id="tree" class="ztree"></ul>

			</div>
		</div>
		<div class="ui-block ml240" style="padding-left: 10px;">
			<div class="ui-block-title1"><spring:message code="cms.resource.selectedResource"/></div>
			<div class="ui-block-content border-grey"
				style="margin-bottom: 10px;">
				<div class="ui-block-line">
					<label><spring:message code="cms.resource.name"/></label>
					<div>
						<input type="text" id="tree_fid" loxiatype="input"
							mandatory="true" placeholder="<spring:message code="cms.resource.resourceName"/>" />
					</div>
				</div>
				<div class="ui-block-line">
					<label><spring:message code="member.group.type"/></label>
					<div>
					<span>
						<opt:select id="tree_state" loxiaType="select" expression="chooseOption.RESOURCE_TYPE"/>
					</span>
					</div>
				</div>
				<form name="updateFileContentForm" method="post"
						action="/cms/updateFileContent.htm" enctype="multipart/form-data">
						<input type="hidden" id="filePath" name="filePath"  />
				<div id="updateFileDiv" class="ui-block-line">
					<label><spring:message code="cms.resource.overlayFolder"/></label>
					<div>
						<span><input id="updateFile" name="fileToUpdate" type="file" /> </span>
					</div>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button persist"
						id="saveResource"><span><spring:message code="btn.save"/></span> </a> <a
						href="javascript:void(0);" class="func-button delete"
						id="remove_element"><span><spring:message code="btn.delete"/></span> </a>
						
						<a  class="func-button view"  target="_blank"> <spring:message code="btn.view"/></a>
						<a
						href="javascript:void(0);" class="func-button delete"
						id="sync_element"><span><spring:message code="cms.resource.public"/> </span> </a>
				</div>
				</form>
			</div>
			<div id="addNewResourceDiv">
				<div class="ui-block-title1"><spring:message code="cms.resource.addResource"/></div>
				<div class="ui-block-content border-grey">
					<form name="addResourceForm" method="post"
						action="/cms/addResource.htm" enctype="multipart/form-data">
						<input type="hidden" id="parentPath" name="parentPath" />
						<div class="ui-block-line">
							<label><spring:message code="cms.resource.name"/></label>
							<div>
								<input type="text" id="name" name="name" loxiatype="input" placeholder="<spring:message code="cms.resource.resourceName"/>" />
							</div>
						</div>
						<div class="ui-block-line">
							<label><spring:message code="member.group.type"/></label>
							<div>
							<span>
								<opt:select id="add_resourceType" name="resourceType" loxiaType="select" expression="chooseOption.RESOURCE_TYPE"/>
							</span>
							</div>
						</div>
						<div id="addFileDiv">
							<div class="ui-block-line">
								<label><spring:message code="cms.resource.resourceLocation"/></label>
								<div>
									<span>
<!-- 									<input id="fileToUpload" name="fileToUpload" type="file" />  -->
										<input id="fileToUpload" beforeSend="mybeforeSend"  class="imgUploadComponet" name="fileToUpload" type="file" url="/cms/uploadResource.json"/>
									</span>
								</div>
							</div>
						</div>

						<div class="button-line1">
							<a id="addLeaf" href="javascript:void(0);"
								class="func-button insertLeaf"><span><spring:message code="cms.resource.addSubResource"/></span> </a>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
