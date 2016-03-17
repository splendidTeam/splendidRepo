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
<style type="text/css">
.button-line1 {
width: 100%;
padding: 10px;
text-align: right;
overflow: hidden;
}
</style>
<script type="text/javascript" src="${base}/scripts/cms/dialog/create.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/cms-click-tip.js"></script>
</head>
<body id="dialogBody">
	<div class="content-box width-percent100">

			<div class="ui-title1">
				<img src="${base}/images/wmi/blacks/32x32/users.png"> <spring:message code="dialog.manage"/>
				<input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
	 		</div>
			<div class="ui-tag-change">
				<ul class="tag-change-ul">
					 <li ><spring:message code="item.update.info"/></li>
       				 <li ><spring:message code="dialog.manage.dynamic.prop.definitions"/></li>
				</ul>
				<div class="tag-change-content">
					<div class="tag-change-in">
					<form name="baseInfoForm" action="/cms/createDialog.json" method="post">
					    <!-- 第一页面 start -->
				        	<div class="ui-block">  
							    <div class="ui-block-title1"><spring:message code="item.update.info"/></div>
							    <div class="ui-block-content ">
							    
							    				 <div class="ui-block-line">
											         <label>path</label>
											         <div >    
											    			<input type="hidden" id="parentPath" name="parentPath" value="${parentPath}">${parentPath }
											    			<input type="text" id="path" name="path" loxiaType="input" value="" 
											    			placeholder="<spring:message code='cms.page.pathinfo'/>" checkmaster="checkPathisExists" 
											    			mandatory="true" style="width:100px;"/>
											         </div>
											    </div>
							    
				        					    <div class="ui-block-line">
											         <label><spring:message code="dialog.manage.extend"/></label>
											         <div >
											              <input type="input" loxiaType="input" name="extend" readonly id="extend"
											                mandatory="false" style="width:400px;"/>
											    		  <span class="common-ic sel-dialog zoom-ic"></span>
											         </div>
											    </div>
											
											
											    <div class="ui-block-line">
											         <label><spring:message code="cms.page.add.descriptioninfo"/></label>
											         <div >    
											    			<textarea type="text" id="description" name="description"  rows="2" placeholder="<spring:message code="cms.page.add.descriptioninfo"/>" style="width:800px;" class="ui-loxia-default ui-corner-all" aria-disabled="false"></textarea>
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
				         
				            <form name="propForm" action="/cms/updateDialog.json" method="post">
				                <input type="hidden" id="updateDialogPath" name="updateDialogPath" value="">
								<!-- 第二页面 start -->
								<div class="ui-block">  
								   <div class="ui-block-title1"><spring:message code="cms.page.add.editprop"/></div>
								    <div class="ui-block-content ">
								    
								    
								     <!-- 父属性开始 -->
							    		<div class="ui-block-content border-grey" style="margin-bottom:10px;">
									    <div class="ui-block-line ">
										             <label class=""><spring:message code="system.property"/>1</label>
										            
													 <div id="top">
													       <div class="ui-block-line">
															    <label><spring:message code="dialog.manage.code"/>　</label>
															    <input class="fLeft" type="text" id="input1" name="fields[0].code" loxiaType="input" value="" mandatory="true" size="50" placeHolder=""/>
														        <span class="new-list-add delete fRight display-inline width-auto clear-none mt0 ml5"><spring:message code="btn.delete"/></span>
								                                <span class="new-list-add hideOrShow fRight display-inline width-auto clear-none mt0"><spring:message code="dialog.manage.show"/>/<spring:message code="dialog.manage.hide"/></span>
														   </div>
							                          </div>
						
						                             <div id="content">
						                             	                             
													   <div class="ui-block-line">
													      <div class="display-inline fLeft attr-name-ds ml0">
														    <label><spring:message code="product.property.lable.name"/>　</label>
															<input type="text"  id="input2" name="fields[0].fieldName"  loxiaType="input" value="" mandatory="true" size="50" placeHolder=""/>
													      </div>
													   </div>
						                             
													   <div class="ui-block-line">
														   <label><spring:message code="dialog.manage.type"/>　</label>
														   <opt:select name="fields[0].type" cssClass="proType" loxiaType="select" expression="chooseOption.PROP_TYPE" />
														   <a id="add_pro" href="javascript:void(0)" class="func-button ml5 none"><spring:message code="cms.area.add.edit"/></a>
														   <input type="hidden" id="propSortNO" class="propSortNO"  name="propSortNO" value="0" />
													   </div>
						
													   <div class="ui-block-line">
														   <label><spring:message code="dialog.manage.editor"/> </label>
														   <opt:select  name="fields[0].editer" loxiaType="select" expression="chooseOption.PROP_EDITER" />
													   </div>
													  
													   <div class="ui-block-line">
														   <label><spring:message code="dialog.manage.required"/>  </label>
														   <opt:select  name="fields[0].required" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
													   </div>
													   
														<div class="ui-block-line">
														   <label><spring:message code="dialog.manage.group"/>  </label>
														   <input  name="fields[0].group" type="text"  loxiaType='number'  value="1" size="50" placeHolder=""/>
													   </div>
													   <div class="ui-block-line">
														   <label><spring:message code="chooseoption.group.add.sortno"/>  </label>
														   <input name="fields[0].sortNo" type="text"   loxiaType='number'  value="1" size="50" placeHolder=""/>
													   </div>
													   
													   <div class="ui-block-line">
														   <label><spring:message code="dialog.manage.desc"/></label>
														   <textarea  name="fields[0].desc" type="text"  rows="2" placeholder="<spring:message code="cms.page.add.descriptioninfo"/>" style="width:500px;" class="ui-loxia-default ui-corner-all" aria-disabled="false"></textarea>
													   </div>
													 	
										             </div>
										        
										    </div>
										    <!-- 父属性结束 -->
										     <!-- 子属性内容 -->
										     <div id="childrenProp_0" class="proto-dialog">
											       <div class="new-list-add addChildrenProp" style="width:100%;">
											       <input type="hidden" id="isWhatType_0"  name="isWhatType_0" value="" />
									                    <span >+<spring:message code="btn.add"/></span>
			                                       </div>
											 </div>
										    
							   			</div>
								    	<div class="new-list-add addProp" style="width:100%;">
														<span >+<spring:message code="btn.add"/></span>
										</div>
						 	            <div class="button-line">
							         		<input type="button" value="<spring:message code="btn.save"/>" class="button orange save" title="<spring:message code="btn.save"/>"/>
							            </div>
								    </div>
								  
									</div>
						       </form>
				         
					</div>
				</div>
			</div>
		<div class="button-line1">
               <input type="button" value="<spring:message code="btn.return"/>" class="button return" title="<spring:message code="btn.return"/>"/>
		</div>
	</div>
	<!-- 用于增加select字段 start -->
	<div id="editerChildSelectHtml" style="display:none;">
		<opt:select name="fields[0].children[0].editer" loxiaType="select" expression="chooseOption.PROP_EDITER" />
	</div>
	<div id="requiredChildSelectHtml" style="display:none;">
		 <opt:select  name="fields[0].children[0].required" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
	</div>
     
	
	<div id="editerSelectHtml" style="display:none;">
		<opt:select  name="fields[0].editer" loxiaType="select" expression="chooseOption.PROP_EDITER" />
	</div>
	<div id="requiredSelectHtml" style="display:none;">
		 <opt:select  name="fields[0].required" loxiaType="select" expression="chooseOption.TRUE_OR_FALSE" />
	</div>
	<div id="typeSelectHtml" style="display:none;">
		 <opt:select  name="fields[0].type" cssClass="proType" loxiaType="select" expression="chooseOption.PROP_TYPE" />
	</div>
	<!-- end -->
<!--悬浮框 -->
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


<!-- 子属性开始 -->
<div id="childrenProp-dialog" class="proto-dialog">
	<h5><spring:message code="dialog.manage.edit.prop"/></h5>
	
	  
	 <div class="proto-dialog-content">		 	
	 	  
	 	<div class="ui-block"> 
	 	    <div class="ui-block-content childrenPropDialogContent">
	 	         <!-- 子灰色框开始 -->
		<!-- to be add -->
                 <!-- 子灰色框结束 -->
		   </div>
	 	  </div>

	 	  
	 </div>
	 <input type="hidden" id="currentNO"  name="currentNO" value="" />
	 <div class="proto-dialog-button-line">
	 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange chiProSelok"/>
	 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange chiProSelCancel"/>
	 </div>
</div>
<!-- 子属性结束 -->
</body>
</html>
