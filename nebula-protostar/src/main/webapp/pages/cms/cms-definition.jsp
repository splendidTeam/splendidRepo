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
<script type="text/javascript" src="${base}/scripts/cms/cms-definition.js"></script>
<script type="text/javascript">
	
	var staticType="${staticType}";
	var dynamicType="${dynamicType}";
	
</script>
</head>
<body>
<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="../images/wmi/blacks/32x32/wrench_plus_2.png"><spring:message code="cms.definition.management"/>
		</div>
		<div class="ui-block ui-block-fleft w240">
			<div class="ui-block-content ui-block-content-lb">
				<div class="tree-control">
					<input type="text" id="key" loxiatype="input"
						placeholder="<spring:message code="item.search.keyword"/>" />
				</div>
				<div>
					<span id="searchResult"></span>
				</div>
				<ul id="definitionTree" class="ztree"></ul>
			</div>
		</div>
		<div class="ui-block ml240" style="padding-left: 10px;">
		
			<div class="ui-block-title1"><spring:message code="cms.definition.currentSelectedDefinition"/></div>
			<form id="updateDefForm" name="updateDefForm"  action="/cms/updateDefinition.json">
			<div class="ui-block-content border-grey"
				style="margin-bottom: 10px;">
				<div class="ui-block-line">
					<label><spring:message code="cms.resource.name"/></label>
					<div>
						<input type="text" id="name" name="name" loxiatype="input"
							mandatory="true" placeholder="<spring:message code="cms.definition.definitionName"/>" />
						<input type="hidden" id="id" name="id"/>
						<input type="hidden" id="path" name="path"/>
					</div>
				</div>
				<div id="definitionDiv">
				<div class="ui-block-line">
					<label><spring:message code="member.group.type"/></label>
					<div>
						<span>
						<opt:select id="type" name="type" loxiaType="select" expression="chooseOption.TEMPLATE_TYPE"/>
						</span>
					</div>
				</div>
				
				<div class="ui-block-line">
					<label><spring:message code="cms.page.add.descriptioninfo"/></label>
					<div>
						<input type="text" id="description" name="description" loxiaType="input"
							placeholder="<spring:message code="cms.definition.definitionDescription"/>" />
					</div>
				</div>
				
				<div class="ui-block-line">
					<label><spring:message code="cms.definition.path"/></label>
					<div>
						<input type="text" id="templatePath" name="templatePath" loxiatype="input" checkmaster="validateDefPath" mandatory="true" placeholder="<spring:message code="cms.definition.path"/>"  aria-disabled="false">
						<span id="selectDefinitionBtn" class="common-ic template zoom-ic"></span>
					</div>
				</div>
				
				<div class="ui-block-line">
					<label><spring:message code="cms.definition.dialogPath"/></label>
					<div>
						<input type="text" id="dialogPath" name="dialogPath" loxiatype="input" checkmaster="validateDlgPath"  placeholder="<spring:message code="cms.definition.dialogPath"/>"  aria-disabled="false">
						<span id="selectDialogPathBtn" class="common-ic dialog zoom-ic"></span>
						<span class="common-ic dialog update-ic"></span>
					</div>
				</div>
				
				<div id="handlerDiv" class="ui-block-line">
					<label><spring:message code="cms.definition.handlerBean"/></label>
					<div>
						<input type="text" id="handlerBean" name="handlerBean" loxiatype="input" mandatory="true" 
							placeholder="<spring:message code="cms.definition.handlerBean"/>" />
					</div>
				</div>
				

				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button persist"
						id="updateTemplateBtn"><span><spring:message code="cms.definition.update"/></span></a> 
					<a href="javascript:void(0);" class="func-button delete"
						id="deleteTemplateBtn"><span><spring:message code="btn.delete"/></span></a>
					<a href="javascript:void(0);" class="func-button export"
						id="exportTemplateBtn"><span><spring:message code="cms.definition.export"/></span></a>
				</div>
				
				</div>
				<div id="folderDiv" class="button-line1">
					<a href="javascript:void(0);" class="func-button delete"
						id="deleteFolderBtn"><span><spring:message code="btn.delete"/></span></a>
				</div>
			</div>
			</form>
			
			<form id="addDefForm">
			<div id="addnewDefinitionDiv">
				<div class="ui-block-title1"><spring:message code="cms.definition.addSubDefinition"/></div>
			<div class="ui-block-content border-grey">
				<div class="ui-block-line">
					<label><spring:message code="cms.resource.name"/></label>
					<div>
						<input type="text" id="newDefinitionName" name="name" loxiatype="input"
							mandatory="true" placeholder="<spring:message code="cms.definition.definitionName"/>" checkmaster="checkName"/>
						<input type="hidden" id="parentPath" name="parentPath"/>
					</div>
				</div>
				<div class="ui-block-line">
					<label><spring:message code="member.group.type"/></label>
					<div>
						<span>
							<opt:select id="newDefinitionType" name="type" loxiaType="select" expression="chooseOption.TEMPLATE_TYPE"/>
						</span>
					</div>
				</div>
				
				<div class="ui-block-line">
					<label><spring:message code="cms.page.add.descriptioninfo"/></label>
					<div>
						<input type="text" id="newDefinitionDesc" loxiatype="input" name="description" placeholder="<spring:message code="cms.definition.definitionDescription"/>" />
					</div>
				</div>
				
				<div class="ui-block-line">
					<label><spring:message code="cms.definition.path"/></label>
						<input type="text" id="newDefinitionPath" name="templatePath" loxiatype="input" checkmaster="validateDefPath"   mandatory="true" placeholder="<spring:message code="cms.definition.path"/>">
						<span id="newDefinitionSelectPathBtn" class="common-ic template zoom-ic"></span>
				</div>
				
				<div class="ui-block-line">
					<label><spring:message code="cms.definition.dialogPath"/></label>
					<div>
					<input type="text" id="newDefinitionDlgPath" name="dialogPath" checkmaster="validateDlgPath" loxiatype="input" placeholder="<spring:message code="cms.definition.dialogPath"/>"  aria-disabled="false">
					<span id="newDefinitionDlgPathBtn" class="common-ic dialog zoom-ic"></span>
					</div>
				</div>
				
				<div id="newDefinitionHandlerDiv" class="ui-block-line">
					<label><spring:message code="cms.definition.handlerBean"/></label>
					<div>
						<input type="text" id="newHandlerBean" loxiatype="input" name="handlerBean" mandatory="true" 
							placeholder="<spring:message code="cms.definition.handlerBean"/>" />
					</div>
				</div>

				<div class="button-line1">
					<a id="addTemplateBtn" href="javascript:void(0);"
						class="func-button insertLeaf"><span><spring:message code="cms.definition.addSubDefinition"/></span></a>
				<a id="addFloderBtn" href="javascript:void(0);"
						class="func-button insertLeaf"><span><spring:message code="cms.definition.addSubFloder"/></span></a>
				</div>
			</div>
			</div>
		</form>
		</div>
		
		<div id="selectTemplateDialogDiv" class="proto-dialog">
		<h5><spring:message code="cms.definition.selectDefinition"/></h5>
			<div class="proto-dialog-content">

				<ul id="selectDefTree" class="ztree"></ul>
			</div>
			<div class="proto-dialog-button-line">
				<input type="button" value="<spring:message code="btn.confirm"/>" class="button orange tempselok" /> 
				<input type="button" value="<spring:message code="btn.cancel"/>" class="button orange tempselcancel" />
			</div>
		</div>
		
		<div id="selectDlgDialogDiv" class="proto-dialog">
		<h5><spring:message code="cms.definition.selectDef"/></h5>
			<div class="proto-dialog-content">
				<ul id="dlgTree" class="ztree"></ul>
			</div>
			<div class="proto-dialog-button-line">
				<input type="button" value="<spring:message code="btn.confirm"/>" class="button orange dlgselok" />
				<input type="button" value="<spring:message code="btn.cancel"/>" class="button orange dlgselcancel" />
			</div>
		</div>
		
		
		<div id="createFloderDialog" class="proto-dialog">
		<h5><spring:message code="cms.definition.createSubFloder"/></h5>

		 <div class="proto-dialog-content">
			 <div class="ui-block-line">
			 		 <label><spring:message code="cms.definition.floderName"/>:</label>
					<div>    
					  <input type="text" id="floderName" loxiaType="input" value="" mandatory="true"/>
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange createFloderOK"/>
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange createFloderCancel"/>
		 </div>
		
	</div>
		<div class="button-line">
							
						</div>
	</div>
</body>
</html>
