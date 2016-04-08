<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="industry.list.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/product/property/nebula-add-property.js"></script>
<script type="text/javascript" src="${base}/scripts/product/property/common-property.js"></script>
<script type="text/javascript" src="${base}/scripts/dragarraylist.js"></script>
<script src="${base}/scripts/layer/layer.js"></script>
	
</head>

<body>
<div class="content-box">      
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/spechbubble.png"><spring:message code="system.property.manager"/>
	</div>

	<div class="ui-block">

	<div id="propertySelDiv">
	    <div class="ui-block" style="padding-left: 10px;">
	    	<div class="width-percent100">
		   		<form name="userForm" action="" method="post">
				<div class="ui-block-title1"><spring:message code="shop.property.modify"/></div>
			    	 <div class="ui-block-content border-grey" style="margin-bottom: 10px;">
					   	<input name="propertyId" type="hidden" value="" id="propertyId"/>
						<input name="industryId" type="hidden" value="" id="industryId"/>
						<input name="commonPropertyId" type="hidden" value="" id="commonPropertyId"/>
					    <input name="sortNo" type="hidden" value="" id="sortNo"/>
					    <input id="idvalue"  value="" type="hidden"/>
					     <b style="font-size: 15px;">通用描述</b>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				    		<input type="radio" name="typeradio" value="choose" checked="checked" onclick="inputchange(this);">&nbsp;&nbsp;<spring:message code="shop.property.tochoose"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				         	<input type="radio" name="typeradio" value="add" onclick="inputchange(this);">&nbsp;&nbsp;<spring:message code="shop.property.toadd"/>
					    <br/>
					    <br/>
					    <div class="ui-block-line">
						         <label><spring:message code="shop.property.commonname"/></label>
						         <div class="wl-right" style="margin-top: 5px;">
						              <span id="commonname" style="display: none;"></span>
										<select id="commonnameselect" class="gname-select ui-loxia-default ui-corner-all" aria-disabled="false" style="" onchange="selectvalue(this);"> 
											<option value="" selected="selected">请选择</option>
											<c:forEach items="${commonPropertyList}" var="commonProperty">
													<option value="${commonProperty.name }" editingType="${commonProperty.editingType }"
													 valueType="${commonProperty.valueType }" isSaleProp="${commonProperty.isSaleProp }" 
													 isColorProp="${commonProperty.isColorProp }" industrylist="${commonProperty.industrylist}"
													 commonPropertyId="${commonProperty.id }"
													 >${commonProperty.name }</option>
											</c:forEach>								
										</select>
										<br/>
										<input name="commonName" id="commonnameinput"  style="width: 140px;height: 26px;position: absolute;margin-top: -26px;margin-left: -1px;z-index: 999;" class="input_add gname edit-select ui-loxia-default ui-corner-all ui-loxia-highlight" type="text" mandatory="true" aria-disabled="false" onchange="conmonnamechange();" placeholder="<spring:message code="shop.property.commonname"/>">
						         </div>
						          <select id="commonnameselectcolone" style="display: none;"> 
										<option value="" ></option>
								 </select>
						         
						</div>
					    <div class="ui-block-line">
					         <label><spring:message code="shop.property.edit.type"/></label>
					         <div class="wl-right">
					          <opt:select name="editingType" id="editingType" expression="chooseOption.EDITING_TYPE" defaultValue="1" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
							</div>
					    </div>
						 
					    <div class="ui-block-line">
					         <label><spring:message code="shop.property.valuetype"/></label>
					         <div class="wl-right">
					            <opt:select name="valueType" id="valueType" expression="chooseOption.VALUE_TYPE" defaultValue="1" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
					         </div>
					    </div>
					    
					    <div class="ui-block-line">
					         <label><spring:message code="shop.property.issale"/></label>
					         <div class="wl-right">
					         	    		<opt:select name="isSaleProp"  id="isSaleProp"  expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
					         </div>
					    </div>
						
						<div class="ui-block-line">
					         <label><spring:message code="shop.property.iscolor"/></label>
					         <div class="wl-right">		
					    			<opt:select name="isColorProp" id="isColorProp" expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
					    			</select>
					         </div>
					    </div>
					    <br/>
				    <div class="ui-block-line">
						<label><spring:message code="shop.property.otherIndustrys"/></label>
						<div class="wl-right" style="margin-top: 5px;">
								         	<b id="Industrys"></b>
						</div>
					</div>
					<br/><br/>
					<b style="font-size: 15px;">扩展描述</b>:
					<br/><br/>
					 <c:if test="${i18nOnOff == true}">
			    	<c:forEach items="${i18nLangs}" var="i18nLang">
				    <div class="ui-block-line">
				         <label><spring:message code="shop.property.name"/></label>
				         <div class="wl-right">
				              <input name="name" type="text" loxiaType="input" id="proname" class="mutl-lang-name" value="" lang="${i18nLang.key}" mandatory="true" size="50"/>
					          <span>${i18nLang.value}</span>
				         </div>
				    </div>
				    </c:forEach>
				    </c:if>
				    <c:if test="${i18nOnOff == false}">
					     <div class="ui-block-line">
					         <label><spring:message code="shop.property.showname"/></label>
					         <div class="wl-right">
					              <input name="name" type="text" loxiaType="input" id="proname" value="" mandatory="true" size="50" placeholder="<spring:message code="shop.property.name"/>"/>
<!-- 					              <input type="button" value="选择" class="choosebuuton"/> -->
					         </div>
					    </div>
					  </c:if>
						<div class="ui-block-line">
					         <label><spring:message code="shop.property.isoutput.necessary"/></label>
					         <div class="wl-right">
					            <opt:select name="required" id="required" expression="chooseOption.TRUE_OR_FALSE" defaultValue="false" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
					         </div>
					    </div>
						
						<div class="ui-block-line">
					         <label><spring:message code="shop.property.issearch"/></label>
					         <div class="wl-right">
					           <opt:select name="searchable" id="searchable" expression="chooseOption.TRUE_OR_FALSE" defaultValue="true" otherProperties="loxiaType=\"select\" style=\"width:30%;float:left;\" "/>
				
					         </div>
					    </div>
						<c:if test="${i18nOnOff == true}">
			    	  	<c:forEach items="${i18nLangs}" var="i18nLang">
					    <div class="ui-block-line">
					         <label><spring:message code="shop.property.groupname" /></label>
					         <div class="wl-right">
					             <input name="groupName"  class="mutl-lang-groupName" type="text" loxiaType="input" lang="${i18nLang.key}" value=""  size="50" placeholder="<spring:message code="shop.property.lable.groupname" />"/>
					         	 <span>${i18nLang.value}</span>
					         </div>
					    </div>
					    </c:forEach>
					    </c:if>
					    <c:if test="${i18nOnOff == false}">
					    <div class="ui-block-line">
					         <label><spring:message code="shop.property.groupname" /></label>
					         <div class="wl-right">
					             <input name="groupName" id="groupName" type="text" loxiaType="input" value=""  size="50" placeholder="<spring:message code="shop.property.lable.groupname" />"/>
					         </div>
					    </div>
					    </c:if>
					    <div class="ui-block-line">
								         <label><spring:message code="shop.property.otherIndustrys"/></label>
								         <div class="wl-right">
								        	<textarea rows="5" cols="71" style="background: #ededed;border: 1px solid #ccc;" readonly="readonly" id="Industrys"> </textarea>
								         </div>
						</div>
					    <br/>
					    <div id="categoryMenuContent" class="menuContent"
												style="display: none; background-color: #f0f6e4; border: 1px solid #617775; padding: 3px;">
												<ul id="categoryDemo" class="ztree"
													style="margin-top: 0; width: 180px; height: 100%;"></ul>
						</div>
					</div>
			    </form>
		    </div>
	    </div>
		<div class="button-line">
			<input type="button" value="<spring:message code="system.property.update"/>" class="button orange add" style="display:none"/>
	        <input type="button" value="<spring:message code="system.property.insert"/>" class="button orange add" style="display:none"/>
	        <input type="button" value="<spring:message code="shop.update.update"/>" class="button orange add" style="display:none"/>
	        <input type="button" value="<spring:message code="btn.disable"/>" class="button orange add" style="display:none"/>
			<input type="button" value="<spring:message code="system.property.setvalue"/>" class="button orange setvalue" title="<spring:message code="system.property.setvalue"/>"  id="propertyValue_button"/>
		    <input type="button" value="<spring:message code="btn.return"/>" class="button return_button-new" title="<spring:message code="btn.return"/>"/>
		 </div>
	</div>
	</div>
</div>


<div id="menuContent" class="menuContent" style="display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
</div>

</body>
</html>
