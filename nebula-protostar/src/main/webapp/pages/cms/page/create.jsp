<%@page import="java.text.SimpleDateFormat"%>
<%@include file="/pages/commons/common.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>
<link rel="stylesheet" href="${base}/css/demo.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/cmsTree.js"></script>
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
.button-line1 {
width: 100%;
padding: 10px;
text-align: right;
overflow: hidden;

</style>
<script type="text/javascript">
var $j = jQuery.noConflict();
</script>

<SCRIPT type="text/javascript" src="${base}/scripts/cms/page/create.js"></SCRIPT>


</head>

<body>
<div class="content-box width-percent100">
    
   
	<div class="ui-title1">
		<img src="../images/wmi/blacks/32x32/users.png">
	<spring:message code="cms.page.manage"/>
	 <input type="button" value='<spring:message code="btn.return"/>' class="button return" title="<spring:message code='btn.return'/>"/>
	 <input type="button" value="<spring:message code='cms.page.add.preview'/>" class="button preview" title="<spring:message code='cms.page.add.preview'/>"/>
	<input type="button" value="<spring:message code='cms.page.add.viewtemplate'/>" title="<spring:message code='cms.page.add.viewtemplate'/>"  class="button viewTemplate"/>
	</div>
	
	
	
	
	<div class="ui-tag-change">
    <ul class="tag-change-ul">
       <li ><spring:message code="item.update.info"/></li>
       <li ><spring:message code="cms.page.add.editarea"/></li>
       <li ><spring:message code="cms.page.add.editprop"/></li>
    </ul>
              
    <div class="tag-change-content">
    
    	
    	<div class="tag-change-in">
		  <form name="pageForm" action="/cms/createPage.json" method="post">
        	<div class="ui-block">  
			   <div class="ui-block-title1"><spring:message code="item.update.info"/></div>
			    <div class="ui-block-content ">
			    
			    				 <div class="ui-block-line">
							         <label><spring:message code="cms.page.path"/></label>
							         <div >   
							         		<input type="hidden" id="parentPath" name="parentPath" value="${parentPath}">${parentPath }<input type="text" id="name" name="name" loxiaType="input" value="" placeholder="<spring:message code='cms.page.pathinfo'/>" checkmaster="checkPathisExists" mandatory="true" style="width:100px;"/>
							    			
							         </div>
							    </div>
							     <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.backupNodeCount"/></label>
							         <div >
							              <input type="text" name="backupNodeCount" checkmaster="checkBackupNodCount" placeholder="<spring:message code='cms.page.add.backupNodeCountinfo'/>" loxiaType="input" value="" mandatory="false" style="width:400px;"/>
							         </div>
							    </div>
			    
        					    <div class="ui-block-line">
							         <label><spring:message code="cms.page.name"/></label>
							         <div >
							              <input type="text" name="elementName" placeholder="<spring:message code='cms.page.name'/>" loxiaType="input" value="" mandatory="true" style="width:400px;"/>
							         </div>
							    </div>
							    
							
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.seltemp"/></label>
							         <div>    
							    			<input type="input" loxiaType="input" name="definitionPath" id="definitionPath"  mandatory="false" style="width:400px;" checkmaster="checkTemplatePath"/>
							    			<span class="common-ic sel-dialog zoom-ic"></span>
							         </div>
							    </div>
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.starttime"/></label>
							         <div >    
							    			<input type="text" name="startTime" id="startTime" loxiaType="date" showtime="true"  value='' style="width:400px;"/>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.endtime"/></label>
							         <div >    
							    			<input type="text" name="endTime" id="endTime" loxiaType="date" showtime="true" value='' style="width:400px;" />
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.title"/></label>
							         <div >    
							    			<input type="text" name="title" loxiaType="input" placeholder="<spring:message code='cms.page.add.titleinfo'/>"  value="" mandatory="false" style="width:400px;"/>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.keywords"/></label>
							         <div >    
							    			<textarea type="text"  name="keywords" loxiaType="input" mandatory="false" placeholder="<spring:message code='cms.page.add.keywordsinfo'/>" rows="2" placeholder="<spring:message code='cms.page.add.titleinfo'/>" style="width:800px;"></textarea>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.description"/></label>
							         <div >    
							    			<textarea type="text"  name="desc" loxiaType="input" mandatory="false" rows="2" placeholder="<spring:message code='cms.page.add.descriptioninfo'/>" style="width:800px;"></textarea>
							         </div>
							    </div>
							    
							   
        
        		</div>
			</div>
			
			<div class="button-line">
         		<input type="button" value="<spring:message code='btn.save'/>" id="baseInfoSave" class="button orange save" title="<spring:message code='btn.save'/>"/>

         		
         	</div>
			</form>
        </div>
    
  
	    <div class="tag-change-in" >
		
		<form name="pageContentForm" action="/cms/updatePageContent.json" method="post">
		<!-- 编辑页面 start -->
		<div class="ui-block">  
		   <div class="ui-block-title1"><spring:message code="cms.page.add.editarea"/></div>
		  	<input type="hidden" name="path" value="" id="pageContentPath" /> 
		    <div class="ui-block-content " id="editArea">
		    
		    
		        <!-- 循环动态显示区域的信息 -->
	    		<div id="areaDefinitions">
	    		
	    		</div>
	    
		    <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.page.add.file"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" name="cssFilePaths" id="cssFilePath" rows="5" cols="100" loxiaType="input"></textarea>		    			
		    			
		         </div>
		    </div>
		    
		      <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.page.add.edit"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" name="cssFileContent" id="cssFileContent" rows="5" cols="100" loxiaType="input"></textarea>		    			
		    			
		         </div>
		    </div>
		    
		    
		      <div class="ui-block-line">
		         <label>JS<spring:message code="cms.page.add.file"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" name="jsFilePaths" id="jsFilePath" rows="5" cols="100" loxiaType="input"></textarea>		    			
		    			
		         </div>
		    </div>
		    
		    
		    
		    <div class="ui-block-line">
		         <label>JS<spring:message code="cms.page.add.edit"/></label>
		         <div >    
		    			<textarea style="width:780px;" name="jsFileContent" id="jsFileContent" rows="5" cols="100" loxiaType="input"></textarea>	    			
		    			
		    			
		         </div>
		    </div>
		    

		    </div>
			</div>
			<!-- 编码页面 end -->
			
			<div class="button-line">
         		<input type="button" value="<spring:message code='btn.save'/>" id="areaSave" class="button orange save" title="<spring:message code='btn.save'/>"/>

         		
         	</div>
			 </form>
		</div>
      
      
        <div class="tag-change-in">
		 <form name="propertyForm" action="/cms/updatePageDynamicProperty.json" method="post">
		 		<input type="hidden" name="path"
											value="" id="propertyPath" /> 
        	<div class="ui-block">  
			   <div class="ui-block-title1"><spring:message code="cms.page.add.editprop"/></div>
			   
			    <div class="ui-block-content " id="property">
			    
        
        		</div>
			</div>
			
			<div class="button-line">
         		<input type="button" value="<spring:message code='btn.save'/>" id="propertySave" class="button orange save" title="<spring:message code='btn.save'/>"/>

         	</div>
			 </form>
        </div>
          
        
       </div>
    </div>
	
	
	
	
	
 
    
 	<div class="button-line1">
        <input type="button" value="<spring:message code='cms.page.add.viewtemplate'/>" title="<spring:message code='cms.page.add.viewtemplate'/>"  class="button viewTemplate"/>
         <input type="button" value="<spring:message code='cms.page.add.preview'/>" class="button preview" title="<spring:message code='cms.page.add.preview'/>"/>
         <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
    </div>
</div>

	<div id="areaOrcomp-dialog" class="proto-dialog">
	 <h5><spring:message code="cms.page.add.selectAreaOrComp"/></h5>
		
		 <div class="proto-dialog-content">
		 	
		 	<ul id="areaOrComp" class="ztree"></ul>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code='btn.confirm'/>" class="button orange selok"/>
		 	  
		 	  <input type="button" value="<spring:message code='btn.cancel'/>" class="button orange selcancel"/>
		 </div>
	</div>
	
	<div id="template-dialog" class="proto-dialog">
	<h5><spring:message code="cms.page.add.seltemp"/></h5>
		 
		 <div class="proto-dialog-content">		 	
		 	<ul id="tempTree" class="ztree"></ul>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code='btn.confirm'/>" class="button orange tempselok"/>
		 	  
		 	  <input type="button" value="<spring:message code='btn.cancel'/>" class="button orange tempselcancel"/>
		 </div>
	</div>
	
	
	<!-- 列表子属性开始 -->
	<div id="childrenProplist-dialog" class="proto-dialog">
		<h5>编辑子属性</h5>
		 <div class="proto-dialog-content">		 	
		 	  
		 	<div class="ui-block"> 
			 	    <div class="ui-block-content childrenPropDialogContent" id="childrenListPropDialogContent">
			 	    	<!-- 内容 -->
				   </div>
				   <div class="new-list-add addChildrenProp" style="width:100%;">
						<span>+新增</span>
				  </div>
		 	 </div>		 	  
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="确认" class="button orange chiProSelok"/>
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
		 	  <input type="button" value="确认" class="button orange chiProSelok"/>
		 	  <input type="button" value="取消" class="button orange chiProSelCancel"/>
		 </div>
	</div>
	<!-- 子属性结束 -->
	
	
	<!-- 隐藏域用于保存初始化新增div的数据  begin-->
	
			<div class="ui-block-line  ui-block-content border-grey" id="listchildPropertyDiv" name="listchildPropertyDiv"  style="display:none">
				<label class="propertyIndex">属性1</label>										            
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
