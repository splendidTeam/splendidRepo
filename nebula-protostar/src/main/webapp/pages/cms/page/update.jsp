<%@include file="/pages/commons/common.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
}
.span-center {
	text-align: center;
	padding-top: 5px;
}

</style>
<script type="text/javascript">
var $j = jQuery.noConflict();
</script>

<SCRIPT type="text/javascript" src="${base}/scripts/cms/page/update.js"></SCRIPT>


</head>

<body>
<div class="content-box width-percent100">
    
   
	<div class="ui-title1">
		<img src="../images/wmi/blacks/32x32/users.png">
	<spring:message code="cms.page.manage"/>
	 <input type="button" value='<spring:message code="btn.return"/>' class="button return" title='<spring:message code="btn.return"/>'/>
	 <input type="button" value="<spring:message code='cms.page.add.preview'/>" class="button preview" title="<spring:message code='cms.page.add.preview'/>"/>
	<input type="button" value='<spring:message code="cms.page.add.viewtemplate"/>' title='<spring:message code="cms.page.add.viewtemplate"/>'  class="button viewTemplate"/>
	</div>
	
	
	
	
	<div class="ui-tag-change">
    <ul class="tag-change-ul">
       <li ><spring:message code="item.update.info"/></li>
       <li ><spring:message code="cms.page.add.editarea"/></li>
       <li ><spring:message code="cms.page.add.editprop"/></li>
    </ul>
              
    <div class="tag-change-content">
    
    	
    	<div class="tag-change-in">
    	 <form name="pageForm" action="/cms/updatePage.json" method="post">
        	<div class="ui-block">  
			   <div class="ui-block-title1"><spring:message code="item.update.info"/></div>
			    <div class="ui-block-content ">
			    
			    				 <div class="ui-block-line">
							         <label><spring:message code="cms.page.path"/></label>
							         <div >   
							         		<input type="hidden" id="parentPath" name="parentPath" value="${parentPath}"/> 
							    			<span class="fLeft span-center">${parentPath }</span><input  type="text" id="name" class="fLeft" name="name" id="pathname" loxiaType="input" value="${page.name }" readonly="readonly" mandatory="true" style="width:100px;"/>
							    			<!--  span>/*path不能修改*/</span-->
							    			
							    			<div id="loxiaTip-r" class="loxiaTip-r" style="display:none">
											 	  <div class="arrow"></div>
											      <div class="inner ui-corner-all" id="parentPathMessage" style="padding: .3em .7em; width: auto;"></div>
										     </div>
							         </div>
							    </div>
							     <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.backupNodeCount"/></label>
							         <div >   
							         		<input type="text" name="backupNodeCount" loxiaType="input" checkmaster="checkBackupNodCount" placeholder="<spring:message code='cms.page.add.backupNodeCountinfo'/>" value="${page.backupNodeCount }" mandatory="false" style="width:400px;"/>
							    			
							         </div>
							    </div>
			    
        					    <div class="ui-block-line">
							         <label><spring:message code="cms.page.name"/></label>
							         <div >
							              <input type="text" name="elementName" loxiaType="input" value="${page.elementName }" mandatory="true"  style="width:400px;"/>
							         </div>
							    </div>
							    
							
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.seltemp"/></label>
							         <div>  
										<c:if test="${!empty page.definitionPath }"> 
							    			<input type="input" loxiaType="input" class="fLeft NoEdit" name="definitionPath" id="definitionPath" checkmaster="checkTemplatePath" value="${page.definitionPath }" readonly="readonly" mandatory="false" style="width:400px;"/>
										 </c:if>
										 <c:if test="${empty page.definitionPath }">
										 	<input type="input" loxiaType="input"  name="definitionPath"  id="definitionPath" checkmaster="checkTemplatePath" value="${page.definitionPath }"  mandatory="false" style="width:400px;"/>
										 	<span class="common-ic sel-dialog zoom-ic"></span>
										 </c:if>										 
							    		<div id="loxiaTip-r1" class="loxiaTip-r" style="display:none">
											 	  <div class="arrow"></div>
											      <div class="inner ui-corner-all" id="definitionPathMessge" style="padding: .3em .7em; width: auto;"></div>
										</div>
							         </div>
							    </div>
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.starttime"/></label>
							         <div >    
							    			<input type="text" name="startTime" id="startTime" loxiaType="date" showtime="true" value="${startTime }" mandatory="false" style="width:400px;"/>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.endtime"/></label>
							         <div >    
							    			<input type="text" name="endTime"  id="endTime" loxiaType="date" showtime="true" value="${endTime }" mandatory="false"  style="width:400px;"/>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.title"/></label>
							         <div >    
							    			<input type="text" name="title" loxiaType="input" value="${page.title }" mandatory="false" style="width:400px;"/>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.keywords"/></label>
							         <div >    
							    			<textarea type="text" name="keywords" loxiaType="input"  mandatory="false" rows="2" style="width:800px;">${page.keywords }</textarea>
							    			
							         </div>
							    </div>
							    
							    <div class="ui-block-line">
							         <label><spring:message code="cms.page.add.description"/></label>
							         <div >    
							    			<textarea type="text" name="desc" loxiaType="input"  mandatory="false" rows="2"  style="width:800px;">${page.desc }</textarea>
							    			
							         </div>
							    </div>
							    
							   
        
        		</div>
			</div>
			
			<div class="button-line">
         		<input type="button" id="baseInfoSave" value="<spring:message code='btn.save'/>" class="button orange save" title="<spring:message code='btn.save'/>"/>


         	</div>
         	</form>
        </div>
    

	    <div class="tag-change-in" >
		
 <form name="pageContentForm" action="/cms/updatePageContent.json" method="post">
 		<input type="hidden" id="parentPath" name="path" value="${parentPath}${page.name}"> 
		<!-- 编辑页面 start -->
		<div class="ui-block">  
		   <div class="ui-block-title1"><spring:message code="cms.page.add.editarea"/></div>
		    <div class="ui-block-content " id="editArea">
		    
		    	<div id="areaDefinitions">
								
				</div>	
		    <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.page.add.file"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" name="cssFilePaths" id="cssFilePath" rows="5" cols="100" loxiaType="input">${cssFilePaths}</textarea>		    			
		    			
		         </div>		         
		    </div>
		    
		    
		     <div class="ui-block-line">
		         <label>CSS<spring:message code="cms.page.add.edit"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;"  name="cssFileContent" id="cssFileContent"rows="5" cols="100" loxiaType="input">${css.content}</textarea>			
		    			
		         </div>		         
		    </div>
		    
		    
		     <div class="ui-block-line">
		         <label>JS<spring:message code="cms.page.add.file"/></label>
		         <div >    
		    			
		    			<textarea style="width:780px;" name="jsFilePaths" id="jsFilePath" rows="5" cols="100" loxiaType="input">${jsFilePaths}</textarea>		    			
		    			
		         </div>		         
		    </div>
		    
		    
		    <div class="ui-block-line">
		         <label>JS<spring:message code="cms.page.add.edit"/></label>
		         <div >    
		    			<textarea style="width:780px;" name="jsFileContent" id="jsFileContent" rows="5" cols="100" loxiaType="input">${script.content}</textarea>    					    			
		    			
		    			
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
        	<input type="hidden" id="parentPath" name="path" value="${parentPath}${page.name}"> 
        	<div class="ui-block">  
			   <div class="ui-block-title1"><spring:message code="cms.page.add.editprop"/></div>
			    <div class="ui-block-content " id="property">
			    		
			    		<c:if test="${!empty propertyNodes  }"> 			
			    			 <c:forEach items="${propertyNodes }" var="propertyNode">
        					    <div class="ui-block-line">
							         <label>${propertyNode.key.fieldName }</label>
							         <div >		
							         	<c:if test="${propertyNode.key.required==true }">
							              <c:if test="${propertyNode.key.type==2 || propertyNode.key.type==3}">
							               	 <input type="text" name="${propertyNode.key.code }" class="properties" loxiaType='input' value="${propertyNode.value }" placeholder='<spring:message code="cms.page.update.property"/>' onkeydown="return false;" mandatory="true"/>
							              	 <a  class="func-button ml5" href="javascript:void(0)"> 编辑</a><span id='properyPath' style='display:none'>${propertyNode.key.path }</span><span id='properyType' style='display:none'>${propertyNode.key.type }</span>
							              </c:if>
							               <c:if test="${propertyNode.key.type!=2 && propertyNode.key.type!=3}">
							               	 <input type="text" name="${propertyNode.key.code }" class="properties" loxiaType='${propertyNode.key.editer == "date" ? "date" : "input"}' value="${propertyNode.value }" placeholder='<spring:message code="cms.page.update.property"/>'  mandatory="true"/>
							              </c:if>
							              
							              
							         	</c:if>
							         	<c:if test="${propertyNode.key.required==false }">						         					         	
							              
							         		<c:if test="${propertyNode.key.type==2 || propertyNode.key.type==3}">
							         			<input type="text" name="${propertyNode.key.code }" class="properties" loxiaType='input' value="${propertyNode.value }"  onkeydown="return false;"  mandatory="false"/>
							              		<a  class="func-button ml5" href="javascript:void(0)"> 编辑</a><span id='properyPath' style='display:none'>${propertyNode.key.path }</span><span id='properyType' style='display:none'>${propertyNode.key.type }</span>
							             	 </c:if>
							             	 <c:if test="${propertyNode.key.type!=2 && propertyNode.key.type!=3}">							             	 
							         			<input type="text" name="${propertyNode.key.code }" class="properties" loxiaType='${propertyNode.key.editer == "date" ? "date" : "input"}' value="${propertyNode.value }"   mandatory="false"/>
							             	 </c:if>
							         	</c:if>
							         </div>
							    </div>							    
							</c:forEach>
						</c:if>
						<c:if test="${empty propertyNodes  }">
							<div class="ui-block-line">
							        未找到数据
							</div>
							    
						</c:if>
							    
							    
        
        		</div>
			</div>
			
			<div class="button-line">
         		<input type="button" id="propertySave" value="<spring:message code='btn.save'/>" class="button orange save" title="<spring:message code='btn.save'/>"/>

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
	 <h5><spring:message code="cms.area.add.selectAreaOrComp"/></h5>
		
		 <div class="proto-dialog-content">
		 	
		 	<ul id="areaOrComp" class="ztree"></ul>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code='btn.confirm'/>" class="button orange selok"/>
		 	  
		 	  <input type="button" value="<spring:message code='btn.cancel'/>" class="button orange selcancel"/>
		 </div>
	</div>
	
	<div id="template-dialog" class="proto-dialog">
	 <h5><spring:message code="cms.area.add.seltemp"/></h5>
		
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
		 	    	<!--内容  -->
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
				<label  class="propertyIndex">属性1</label>										            
				     <div id="top">
							<div class="ui-block-line">
								<label></label>
								<input class="firstInput" type="text" />
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
