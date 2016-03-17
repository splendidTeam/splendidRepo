<%@include file="/pages/commons/common.jsp"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" type="text/css" href="${base}/css/demo.css" />
<script type="text/javascript"
	src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>

<script type="text/javascript" src="${base}/scripts/cmsTree.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/component/create.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/cms-click-tip.js"></script>

<style type="text/css">
.button-line1 {
width: 100%;
padding: 10px;
text-align: right;
overflow: hidden;
}
</style>
<script type="text/javascript">
	var componentRoot ='${componentRoot}';
	var defaultNodeType ='${defaultNodeType}';
	var definitionNodeType ='${definitionNodeType}';
	var tempdefinitionRoot='${tempdefinitionRoot}';
	var componentRootName ='${componentRootName}';
</script>

</head>

<body>
<div class="content">
<div class="content-box">

	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/tag.png">
	<spring:message code="cms.component.manage"/>
	<input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
	<input type="button" value="<spring:message code="cms.component.add.viewtemplate"/>" title="<spring:message code="cms.component.add.viewtemplate"/>"  class="button viewTemplate"/>
	 <input type="button" value="<spring:message code="cms.component.add.preview"/>" class="button preview" title="<spring:message code="cms.component.add.preview"/>"/>
	</div>
	
	
	
	
	<div class="ui-tag-change">
    <ul class="tag-change-ul">
       <li ><spring:message code="item.update.info"/></li>
       <li ><spring:message code="cms.component.add.editcomp"/></li>
       <li ><spring:message code="cms.component.add.editprop"/></li>
    </ul>
              
    <div class="tag-change-content">


	<div class="tag-change-in">
		<form name="userForm" action="/cms/createComponent.json" method="post">
        	<div class="ui-block">  
			   <div class="ui-block-title1"><spring:message code="item.update.info"/></div>
			    <div class="ui-block-content ">
			    
			   					 <div class="ui-block-line">
							         <label>path</label>
							         <div >    
							    			<input type="hidden" name="path"
												value="" id="path" /> 
											<input type="hidden" name="parentNodePath" id="parentNodePath"
												value="${parentNodePath}/" id="parentNodePath" /> 
												
											<span id="span_prefixPath">
												
											</span>
											<input
												type="text" loxiaType="input" value="" name="name"
												mandatory="true" style="width: 100px;" id="name" checkmaster="checkPathName" />
							    			
							         </div>
							    </div>
        					    <div class="ui-block-line">
							         <label><spring:message code="cms.component.add.name"/></label>
							         <div >
							              <input type="text" id="elementName" name="elementName" loxiaType="input" value="" mandatory="true"/>
							         </div>
							    </div>
							    
							
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.component.add.seltemp"/></label>
							         <div>  
							         		<input type="hidden" loxiaType="input" id="definitionPath" name="definitionPath"	value=""
											/>
							         		<input type="hidden" name="difinition_root"
												value="${difinition_root}" id="difinition_root" />
											<input type="input" loxiaType="input" id="tempDefinitionPath" name="tempDefinitionPath"	value="" mandatory="false" checkmaster="checkDefPath"
											/>
							    			<span class="common-ic sel-dialog zoom-ic"></span>
							         </div>
							    </div>

        
        		</div>
			</div>
			
			<div class="button-line">
			   	<input type="button" value="<spring:message code="btn.save"/>" class="button orange save" title="<spring:message code="btn.save"/>"/>
			 </div>
			 </form>
        </div>
        
	    <div class="tag-change-in">
			<form name="componentContentForm" action="/cms/updateComponentContent.json" method="post">
				<div class="ui-block">
					<div class="ui-block-title1"><spring:message code="cms.component.add.editcomp"/></div>
					<div class="ui-block-content " id="editComponent">
			<input type="hidden" name="path"
											value="" id="componentPath" /> 
			 <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.component.add.file"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" name="cssFilePaths" id="cssFilePaths">
		    			
		    			</textarea>
		         </div>
		    </div>
		    
		    <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.component.add.edit"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" name="cssFileContent" id="cssFileContent">
		    			
		    			</textarea>
		         </div>
		    </div>
		    
		    
		    <div class="ui-block-line">
		         <label>JS<spring:message code="cms.component.add.file"/></label>
		         <div >    
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" name="jsFilePaths" id="jsFilePaths">
		    			
		    			</textarea>
		    			
		         </div>
		    </div>
		    
		    <div class="ui-block-line">
		         <label>JS<spring:message code="cms.component.add.edit"/></label>
		         <div >    
		    			<textarea style="width:780px;" rows="5" cols="100" loxiaType="input" id="jsFileContent" name="jsFileContent">
		    			
		    			</textarea>
		    			
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
        	<form name="propForm" action="/cms/updateComponentDynamicProperty.json" method="post">
        	<div class="ui-block">  
			   <div class="ui-block-title1"><spring:message code="cms.component.add.editprop"/></div>
			    <div class="ui-block-content ">
        			<div id="dialogFields">       
        			</div>
				</div>
			</div>
			
			 <div class="button-line">
			 	<input type="hidden" id="propPath" name="propPath" >
			 	<input type="button" value="<spring:message code="btn.save"/>" class="button orange save" title="<spring:message code="btn.save"/>"/>

			 </div>
        	</form>
       </div>
       
    </div>

    
 	<div class="button-line1">
 	
 	 <input type="button" value="<spring:message code="cms.component.add.preview"/>" class="button preview" title="<spring:message code="cms.component.add.preview"/>"/>

         <input type="button" value="<spring:message code="cms.component.add.viewtemplate"/>" title="<spring:message code="cms.component.add.viewtemplate"/>"  class="button viewTemplate"/>
           
         <input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
    </div>
</div>

	<div id="template-dialog" class="proto-dialog">
		 <h5><spring:message code="cms.component.add.seltemp"/></h5>
			 <div class="proto-dialog-content">
			 	
			 	<ul id="tempTree" class="ztree"></ul>
			 </div>
			 <div class="proto-dialog-button-line">
			 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange tempselok"/>
			 	  
			 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange tempselcancel"/>
			 </div>
	</div>
	
	<div id="childrenProplist-dialog" class="proto-dialog">
		<h5><spring:message code="dialog.manage.edit.prop"/></h5>
		 <div class="proto-dialog-content">		 	
		 	  
		 	<div class="ui-block"> 
			 	    <div class="ui-block-content childrenPropDialogContent" id="childrenListPropDialogContent">
				   </div>
				   <div class="new-list-add addChildrenProp" style="width:100%;">
						<span>+<spring:message code="btn.add"/></span>
				  </div>
		 	 </div>		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="cms.component.add.propok"/>" class="button orange chiProSelok"/>
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange chiProSelCancel"/>
		 </div>
	</div>
	
	<div id="childrenPropMap-dialog" class="proto-dialog">
		<h5><spring:message code="dialog.manage.edit.prop"/></h5>
		 <div class="proto-dialog-content">
		 	<div class="ui-block"> 
		 	    <div class="ui-block-content childrenPropDialogContent" id="childrenMapPropDialogContent">
			   </div>
		 	  </div>
	
		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="cms.component.add.propok"/>" class="button orange chiProSelok"/>
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange chiProSelCancel"/>
		 </div>
	</div>
	
	<div class="ui-block-line  ui-block-content border-grey" id="listchildPropertyDiv" name="listchildPropertyDiv"  style="display:none">
		<label class="propertyIndex"><spring:message code="system.property"/>1</label>										            
		     <div id="top">
					<div class="ui-block-line">
						<label></label>
						<input class="firstInput" type="text" />
						<span class="new-list-add delete fRight display-inline width-auto clear-none mt0 ml5"><spring:message code="btn.delete"/></span>
						 <span class="new-list-add hideOrShow fRight display-inline width-auto clear-none mt0"><spring:message code="dialog.manage.show"/>/<spring:message code="dialog.manage.hide"/></span>
					 </div>
			</div>
				
			<div id="listContent" style="display:none" class="listContent">										 	
			 </div>										        
     </div>
	<div id="dynamicTemplateParam-dialog" class="proto-dialog">
		<h5><spring:message code="cms.component.add.inputparam"/></h5>
		
		  
		 <div class="proto-dialog-content">		 	
		 	  
		 	<div class="ui-block"> 
		 	    <div class="ui-block-content dynamicTemplateParamDialogContent">
		 	    	<div class="ui-block-line">
		 	        	<textarea  id="dynamic-params" name="dynamic-params"  rows="4"  style="width:550px;" class="ui-loxia-default ui-corner-all" aria-disabled="false"></textarea>
					</div>
			   </div>
		 	  </div>
	
		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="hidden" id="dynamicPath" loxiaType="input" value="" mandatory="false"/>
		 	  <input type="button" value="<spring:message code="cms.component.add.propok"/>" class="button orange dynamicTplateSelok"/>
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange dynamicTplateSelCancel"/>
		 </div>
	</div>

</div>
</body>
</html>
