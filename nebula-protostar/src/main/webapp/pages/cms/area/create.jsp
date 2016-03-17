<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" type="text/css" href="${base}/css/demo.css" />
<script type="text/javascript"
	src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/cmsTree.js"></script>
<style type="text/css">
.wl-right-auto2 {
	border: 1px solid #ccc;
	overflow-y: scroll;
}

.wl-right-auto2  input[type="checkbox"] {
	clear: both;
}

.wl-right-auto2  input[type="radio"] {
	clear: both;
}

.wl-right-auto2  p {
	width: 80%;
}
.span-center {
	text-align: center;
	padding-top: 5px;
}
.button-line1 {
width: 100%;
padding: 10px;
text-align: right;
overflow: hidden;
}
</style>
<script type="text/javascript" src="${base}/scripts/cms/area/create.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/cms-click-tip.js"></script>
</head>
<body>
 
	<div class="content-box width-percent100">

			<div class="ui-title1">
				<img src="../images/wmi/blacks/32x32/users.png"> <spring:message code="cms.area.manage"/>
				<input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
			<input type="button" value="<spring:message code="cms.area.add.viewtemplate"/>" title="<spring:message code="cms.area.add.viewtemplate"/>"  class="button viewTemplate"/>
	 		<input type="button" value="<spring:message code="cms.area.add.preview"/>" class="button preview" title="<spring:message code="cms.area.add.preview"/>"/>
			</div>
			<div class="ui-tag-change">
				<ul class="tag-change-ul">
					 <li ><spring:message code="item.update.info"/></li>
       				 <li ><spring:message code="cms.area.add.editarea"/></li>
       				<li ><spring:message code="cms.area.add.editprop"/></li>
				</ul>
				<div class="tag-change-content">
					<div class="tag-change-in">
					<form name="userForm" action="/cms/createArea.json" method="post">
						<div class="ui-block">
							<div class="ui-block-title1"><spring:message code="item.update.info"/></div>
							<div class="ui-block-content ">
								<div class="ui-block-line">
									<label>path</label>
									<div>
									<input type="hidden" name="path"
											value="" id="path" /> 
										<input type="hidden" name="parentNodePath"
											value="/instance/area${parentNodePath}" id="parentNodePath"  /> ${parentNodePath}
											 <input
											type="text" loxiaType="input" value="" name="name"
											mandatory="true" style="width: 100px;" id="name" checkmaster="checkName"/> 
									</div>
								</div>
								<div class="ui-block-line">
									<label><spring:message code="cms.area.add.name"/></label>
									<div>
										<input type="text" loxiaType="input" value="" mandatory="true"
											name="elementName"  id="elementName" />
									</div>
								</div>
								<div class="ui-block-line">
									<label><spring:message code="cms.area.add.seltemp"/></label>
									<div>
										<input type="input" loxiaType="input" name="definitionPath" id="definitionPath"  checkmaster="validateTemplatePath" value="" mandatory="false"
											/> <span class="common-ic sel-dialog zoom-ic"></span>
									</div>
								</div>
							</div>
						</div>
						<div class="button-line">
							<input type="button" value="<spring:message code="btn.save"/>" class="button orange save"
								title="<spring:message code="btn.save"/>" /> 
						</div>
						</form>
					</div>
					<div class="tag-change-in">
				<form name="areaContentForm" action="/cms/updateAreaContent.json" method="post">
						<!-- 编辑页面 start -->
						<div class="ui-block">
							<div class="ui-block-title1"><spring:message code="cms.area.add.editarea"/></div>
							<div class="ui-block-content " id="editArea">
					<input type="hidden" name="path"
											value="" id="areaPath" /> 
											
			<div id="areaDefinitions">

			</div>
			  <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.area.add.file"/></label>
		         <div >    
		    			
		    			<textarea  style="width:780px;" rows="5" cols="100" loxiaType="input" name="cssFilePaths" id="cssFilePaths">
		    			
		    			</textarea>
		         </div>
		    </div>
		    
		    <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.area.add.edit"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" name="cssFileContent" id="cssFileContent">
		    			
		    			</textarea>
		         </div>
		    </div>
		    
		    
		    <div class="ui-block-line">
		         <label>JS<spring:message code="cms.area.add.file"/></label>
		         <div >    
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" name="jsFilePaths" id="jsFilePaths">
		    			
		    			</textarea>
		    			
		         </div>
		    </div>
		    
		    <div class="ui-block-line">
		         <label>JS<spring:message code="cms.area.add.edit"/></label>
		         <div >    
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" id="jsFileContent" name="jsFileContent">
		    			
		    			</textarea>
		    			
		         </div>
		    </div>
		    


							</div>
						</div>
						<!-- 编码页面 end -->

						<div class="button-line">

							<input type="button" value="<spring:message code="btn.save"/>" class="button orange save"
								title="<spring:message code="btn.save"/>" />

						</div>
					</form>
					</div>
				
					<div class="tag-change-in">
						<form name="areaDynmicPropertyForm" action="/cms/updateAreaDynamicProperty.json" method="post">
							<input type="hidden" name="path"
											value="" id="propertyPath" /> 
						<div class="ui-block">
							<div class="ui-block-title1"><spring:message code="cms.area.add.editprop"/></div>
        					   <div class="ui-block-content ">
        							<div id="dialogFields">
        
				        			</div>
								</div>
						</div>
						<div class="button-line">
							<input type="button" value="<spring:message code="btn.save"/>" class="button orange save"
								title="<spring:message code="btn.save"/>" /> 
						</div>
						</form>
					</div>
				</div>
			</div>
		<div class="button-line1">

         	<input type="button" value="<spring:message code="cms.area.add.preview"/>" class="button preview" title="<spring:message code="cms.area.add.preview"/>"/>
         	
			<input type="button" value="<spring:message code="cms.area.add.viewtemplate"/>" title="<spring:message code="cms.area.add.viewtemplate"/>"  class="button viewTemplate"/>
	 		
	 		<input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
		</div>
	</div>

	<div id="areaOrcomp-dialog" class="proto-dialog">
		<h5><spring:message code="cms.area.add.selectAreaOrComp"/></h5>
			<div class="proto-dialog-content">
				<ul id="areaOrComp" class="ztree"></ul>
			</div>
			<div class="proto-dialog-button-line">
				<input type="button" value="<spring:message code="btn.confirm"/>" class="button orange selok" />
			 <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange selcancel" />
			</div>
		</div>
		
	<div id="template-dialog" class="proto-dialog">
	<h5><spring:message code="cms.area.add.seltemp"/></h5>
		<div class="proto-dialog-content">
			<ul id="tempTree" class="ztree"></ul>
		</div>
		<div class="proto-dialog-button-line">
			<input type="button" value="<spring:message code="btn.confirm"/>" class="button orange tempselok" /> <input
				type="button" value="<spring:message code="btn.cancel"/>" class="button orange tempselcancel" />
		</div>
	 </div>
	
	
	<!-- 列表子属性开始 -->
	<div id="childrenProplist-dialog" class="proto-dialog">
		<h5>编辑子属性</h5>
		 <div class="proto-dialog-content">		 	
		 	  
		 	<div class="ui-block"> 
		 	    <div class="ui-block-content childrenPropDialogContent" id="childrenListPropDialogContent">
		 	    <table id="listTypeProperty">
		 	    	<!-- 内容 -->
		 	    </table>
			   </div>
		 	  </div>
		 	  
		 	  
		 	  <div class="new-list-add addChildrenProp" style="width:100%;">
					 <input type="hidden" id="isWhatType_1" name="isWhatType_1" value="2">
					<span>+新增</span>
			  </div>
		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange chiProSelok"/>
		 	  <input type="button" value="取消" class="button orange chiProSelCancel"/>
		 </div>
	</div>
	<!-- 子属性结束 -->
	
	<!-- 组合子属性开始 -->
	<div id="childrenPropMap-dialog" class="proto-dialog">
		<h5>编辑子属性</h5>
		 <div class="proto-dialog-content">
		 	<div class="ui-block"> 
		 	    <div class="ui-block-content childrenPropDialogContent" id="childrenMapPropDialogContent">
		 			<!-- 内容 -->
			   </div>
		 	  </div>
	
		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange chiProSelok"/>
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange chiProSelCancel"/>
		 </div>
	</div>
	<!-- 子属性结束 -->
			<!-- 隐藏域用于保存初始化新增div的数据  begin-->

		<div class="ui-block-line  ui-block-content border-grey" id="listchildPropertyDiv" name="listchildPropertyDiv"  style="display:none">
			<label  class="propertyIndex">属性1</label>										            
			     <div id="top">
						<div class="ui-block-line">
							<label></label>
							<input  class="firstInput" type="text" />
							<span class="new-list-add delete fRight display-inline width-auto clear-none mt0 ml5">删除</span>
							 <span class="new-list-add hideOrShow fRight display-inline width-auto clear-none mt0">点击展开/收起</span>
						 </div>
				</div>
					
				<div id="listContent" style="display:none" class="listContent">	
					<!-- 内容 -->											 	
				 </div>										        
      </div>
	<!-- 隐藏域用于保存初始化新增div的数据  end-->
	
		<!-- 动态模板参数开始 -->
	<div id="dynamicTemplateParam-dialog" class="proto-dialog">
		<h5>填写动态模板参数</h5>
		
		  
		 <div class="proto-dialog-content">		 	
		 	  
		 	<div class="ui-block"> 
		 	    <div class="ui-block-content dynamicTemplateParamDialogContent">
		 	    	<div class="ui-block-line">
		 	        	<textarea  id="dynamic-params" name="dynamic-params"  rows="2"  style="width:800px;" class="ui-loxia-default ui-corner-all" aria-disabled="false"></textarea>
					</div>
			   </div>
		 	  </div>
	
		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="hidden" id="dynamicPath" loxiaType="input" value="" mandatory="false"/>
		 	  <input type="button" value="确认" class="button orange dynamicTplateSelok"/>
		 	  <input type="button" value="取消" class="button orange dynamicTplateSelCancel"/>
		 </div>
	</div>
	<!-- 动态模板参数结束 -->
</body>
</html>
