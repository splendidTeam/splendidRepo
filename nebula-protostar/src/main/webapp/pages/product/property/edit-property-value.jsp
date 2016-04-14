<!DOCTYPE HTML>
<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<title><spring:message code="system.propertyvalue.set"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base}/scripts/product/property/edit-property-value.js"></script>
<%-- <script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script> --%>
<script src="${base}/scripts/jquery/shapeshift/jquery.touch-punch.min.js"></script>
<script src="${base}/scripts/jquery/shapeshift/jquery.shapeshift.js"></script>

<script type="text/javascript">
var $ = jQuery.noConflict();
</script>
<link rel="stylesheet" type="text/css" href="${base}/scripts/uploadify3/uploadify.css" media="screen" />
<script type="text/javascript" src="${base}/scripts/uploadify3/jquery.uploadify.min.js"></script>

<style type="text/css">
.i18n-lang{
	display: none;
}
.lang_lable{
width: 15%;
display:block;
float: left;
text-align: left;
padding-left: 3px;
}
tbody input[type="text"]{
	float: left;
	width:85%;
}
    .p10 > .ss-placeholder-child {
      background: transparent;
      border: 1px dashed blue;
    }
</style>
</head>

<body>

<div class="content-box width-percent100">
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/wrench.png"><spring:message code="system.property.manager"/> --【${property.name }】
			
			<input type="button" value="<spring:message code='shop.property.value.sort'/>" class="button orange propertyValueSort" title="<spring:message code='shop.property.value.sort'/>"/>
		</div>
		<input type="hidden" id="propertyId" value="${property.id}" />
		<input type="hidden" value="${pageContext.session.id }" id="session-id">
		
		<div class="ui-block ui-block-fleft" style="width: 400px;">
			<div class="ui-block-content ui-block-content-lb" <c:if test="${property.editingType != 4 }">style="display:none;" </c:if>>
  				<table >
			        <tr>
			            <td>
			            	<label><spring:message code='shop.property.group'/></label>
			            </td>
			            <td>
			             <form id="searchPropertyValueForm" action="" method="">
			                <span >
			                	<select name="proValueGroupId" loxiaType="select"  mandatory="true" class="orgtype selectedGroupId" > 
			                		<option value=""> ---请选择属性值组---</option>
									<c:forEach items="${propertyValueGroupList}" var="group">
										<option value="${group.id}">${group.name}</option>
									</c:forEach>
								</select>
			                </span>
			                <input type="hidden" name="propertyId" value="${property.id}"  />
			              </form>
			            </td>
			            <td>
			                <span ><a href="javascript:void(0);" class="func-button addPropertyValueGroup"   title="<spring:message code='btn.add'/>">
			                		<span><spring:message code="btn.add"/></span></a>
			                </span>
			            </td>
			            <td>
			            	<c:if test="${not empty propertyValueGroupList }">
				                <span >
				                	<a href="javascript:void(0);" class="func-button updatePropertyValueGroup"   title="<spring:message code='btn.update'/>">
				                		<span><spring:message code="btn.update"/></span></a>
				                </span>
			                </c:if>
			            </td>
			        </tr>
			    </table>
			</div>
			<div class="table-border0 border-grey" id="table2" caption="<spring:message code='shop.property.value.list'/>"></div>
			<div class="button-line1">
				<a href="javascript:void(0);" class="func-button deleteSelected" title="删除选中">
   				 		<span>删除选中</span>
   				</a>
			</div>
		</div>
		
		
		<div class="ui-block " style="margin-left:400px;width:auto;padding-left: 10px;" >
			<div class="ui-block-title1">属性值操作</div>
			<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
				<div class="ui-block-line">
					<label>属性值：</label>
					<div>
						<form name="propertyForm" action="/i18n/property/addOrUpdatePropertyValue.json" method="POST">
							<input type="hidden" name="groupId" id="groupId" value="" />
						
							<input type="hidden" name="propertyValues.id" value=""/>
							<input type="hidden" name="propertyValues.propertyId" value="${property.id}" class="propertyValues-propertyId" />
							<input type="hidden" name="propertyValues.sortNo" value="">
							
						 <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
						   <div class="ui-block-line propertyValueInput">
					         
							<input id="input3" lang="${i18nLang.key}"
								name="propertyValues.value.values[${status.index}]" placeholder="<spring:message code='system.propertyvalue'/>"
							  value=""  style='width: 200px' mandatory="true" type="text" class="name" loxiaType="input" trim="true"/>
							
							<input name="propertyValues.value.langs[${status.index}]"  value="${i18nLang.key}" 
							 	mandatory="true" class="i18n-lang" type="text" loxiaType="input" trim="true" />
						   
						   
						   	<span>${i18nLang.value}</span>		 
						  
						   </div>
						   
						   
						 </c:forEach>
						</form>
					</div>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button refreshPropertyValue" title="<spring:message code='refresh'/>">
    				 		<span><spring:message code="refresh"/></span>
    				</a>
				</div>
				<div class="button-line1">
					<a href="javascript:void(0);" class="func-button savePropertyValue" title="<spring:message code='btn.save'/>">
    				 		<span><spring:message code="btn.save"/></span>
    				</a>
    				<a href="javascript:void(0);" class="func-button savePropertyValue continue" title="<spring:message code='shop.property.value.save.continue'/>">
    				 		<span><spring:message code="shop.property.value.save.continue"/></span>
    				</a> 		
				</div>
				<%-- <div class="button-line1">
					<a href="javascript:void(0);" id="downLoadTmplOfPropertyValue" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.export'/>"><span><spring:message code="shop.property.value.export"/></span></a>
    				<a href="javascript:void(0);" id="uploadPropertyValue" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.import'/>"><span><spring:message code="shop.property.value.import"/></span></a>    				
				</div>	 --%>			
			</div>
			
			<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
				<div class="button-line1">
					<a href="javascript:void(0);" id="downLoadTmplOfPropertyValue" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.export'/>"><span><spring:message code="shop.property.value.export"/></span></a>
	   				<%-- <a href="javascript:void(0);" id="uploadPropertyValue" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.import'/>"><span><spring:message code="shop.property.value.import"/></span></a> --%>
				</div>
				<div class="ui-block-line">
			        <label><spring:message code='shop.property.value.import'/></label>
					<div id="propertyValue-upload"></div>
					<p style="margin-top: 10px;">
						<a href="javascript:void(0)" class="func-button" id="btn-ok">确认</a>
						<a href="javascript:void(0)" class="func-button" id="btn-cancel">取消</a>
					</p>
					<p style="margin-top: 5px; font-size: 14px; font-weight: bold;"><span id="upload-sku-result"></span></p>
					<div class="upload-message" id="propertyValue-upload-message"></div>
					<div id="errorTip" style="display: none">
				      	<div style="margin-top: 10px;" >信息提示：</div>
						<div class="ui-block-content border-grey">
							  <div class="ui-block-line showError" style="color: red">
							       
							  </div>		
					      </div>
				      </div>
				  </div>
			</div>
			
			 
     <div class="button-line">
		<input type="button" value="<spring:message code='shop.property.value.sort'/>" class="button orange propertyValueSort" title="<spring:message code='shop.property.value.sort'/>"/>
    </div>
    
      <style>
	    .container {
	      border: 1px dashed #CCC;
	      position: relative;
	    }
	    .container > div {
	      position: absolute;
/* 	      width: 100px; */
	    }
	    .container > .ss-placeholder-child {
	      background: transparent;
	      border: 1px dashed blue;
	    }
	    .ui-loxia-text{
			width:100px;
			box-sizing:border-box;
			border:1px solid #ccc;
			border-radius:3px;
			padding:3px;
			color:#999;
			background:#ededed;
		}
	  </style>
	    <script>
	    $j(document).ready(function() {
	        $j(".container").shapeshift();
	       })
	  </script>
     <!--  <div class="container">
	    <div>1</div>
	    <div>2</div>
	    <div>3</div>
	    <div>4</div>
	    <div>5</div>
	    <div>6</div>
	    <div>7</div>
	    <div>8</div>
	  </div>
	
	  <div class="container">
		<div>9</div>
		<div>10</div>
		<div>11</div>
		<div>12</div>
	  </div> -->
	  
		</div>
		
	</div>
	
    <!-- 评价详细  dialog-->
    <div id="detail-dialog" class="proto-dialog">
		 <h5>属性值排序</h5>
		 <div class="proto-dialog-content p10"  style="height: 350px;">	
		 
		 	 <div class="container">
<!-- 			     <div>
					<div class="ui-loxia-text" >A</div><span>简体中文</span><br>
					<div class="ui-loxia-text" >A</div><span>英文</span><br>
				</div>
			    <div><div class="ui-loxia-text" >B</div><span>简体中文</span><br>
					<div class="ui-loxia-text">B</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">C</div><span>简体中文</span><br>
					<div class="ui-loxia-text">C</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">D</div><span>简体中文</span><br>
					<div class="ui-loxia-text">D</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">E</div><span>简体中文</span><br>
					<div class="ui-loxia-text">E</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">F</div><span>简体中文</span><br>
					<div class="ui-loxia-text">F</div><span>英文</span><br></div>
			  	<div><div class="ui-loxia-text">G</div><span>简体中文</span><br>
					<div class="ui-loxia-text">G</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">H</div><span>简体中文</span><br>
					<div class="ui-loxia-text">H</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">i</div><span>简体中文</span><br>
					<div class="ui-loxia-text">i</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">g</div><span>简体中文</span><br>
					<div class="ui-loxia-text">g</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">k</div><span>简体中文</span><br>
					<div class="ui-loxia-text">k</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">m</div><span>简体中文</span><br>
					<div class="ui-loxia-text">m</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">H</div><span>简体中文</span><br>
					<div class="ui-loxia-text">H</div><span>英文</span><br></div>
			    <div><div class="ui-loxia-text">H</div><span>简体中文</span><br>
					<div class="ui-loxia-text">H</div><span>英文</span><br></div>  -->
			  </div>
			  
			  
	<!-- 		<div class="sortable" >
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">A</div><span>简体中文</span><br>
					<div class="ui-loxia-text">A</div><span>英文</span><br>
				</div>
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">B</div><span>简体中文</span><br>
					<div class="ui-loxia-text">B</div><span>英文</span><br>
				</div>
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">C</div><span>简体中文</span><br>
					<div class="ui-loxia-text">C</div><span>英文</span><br>
				</div>
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">D</div><span>简体中文</span><br>
					<div class="ui-loxia-text">D</div><span>英文</span><br>
				</div>
			</div>
			<div class="sortable" >
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">E</div><span>简体中文</span><br>
					<div class="ui-loxia-text">E</div><span>英文</span><br>
				</div>
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">F</div><span>简体中文</span><br>
					<div class="ui-loxia-text">F</div><span>英文</span><br>
				</div>
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">G</div><span>简体中文</span><br>
					<div class="ui-loxia-text">G</div><span>英文</span><br>
				</div>
				<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
					<div class="ui-loxia-text">H</div><span>简体中文</span><br>
					<div class="ui-loxia-text">H</div><span>英文</span><br>
				</div>
			</div>	
		 
		 <div class="proto-dialog-content p10" id="sortable"  style="height: 350px;">	
			<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
				<div class="ui-loxia-text">E</div><span>简体中文</span><br>
				<div class="ui-loxia-text">E</div><span>英文</span><br>
			</div>
			<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
				<div class="ui-loxia-text">F</div><span>简体中文</span><br>
				<div class="ui-loxia-text">F</div><span>英文</span><br>
			</div>
			<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
				<div class="ui-loxia-text">G</div><span>简体中文</span><br>
				<div class="ui-loxia-text">G</div><span>英文</span><br>
			</div>
			<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
				<div class="ui-loxia-text">H</div><span>简体中文</span><br>
				<div class="ui-loxia-text">H</div><span>英文</span><br>
			</div>
			<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
				<div class="ui-loxia-text">I</div><span>简体中文</span><br>
				<div class="ui-loxia-text">I</div><span>英文</span><br>
			</div>
			<div class="ui-block-line" style="padding:5px 0 5px 0; position: absolute; float: left; width: auto;" propertyid="1" sortno="2">
				<div class="ui-loxia-text">J</div><span>简体中文</span><br>
				<div class="ui-loxia-text">J</div><span>英文</span><br>
			</div> 
		 </div>-->
		
	</div>
	 <div class="proto-dialog-button-line">
		 	  <input type="button" value="提交" class="button orange copyok"/>
		 	  <input type="button" value="取消" class="button black copycancel"/>
		 </div>
</body>
</html>
