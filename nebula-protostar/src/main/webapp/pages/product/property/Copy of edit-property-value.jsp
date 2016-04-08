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
<script type="text/javascript" src="${base}/scripts/ajaxfileupload.js"></script>
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
.td_button_heigth{
	height:30px;
}
</style>
</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/wrench.png"><spring:message code="system.property.manager"/>
    </div>
    
	<div class="ui-block-title1"><spring:message code="system.property.setvalue"/></div>
	<div class="ui-block ui-block-fleft " style="width: 400px;">
		<div class="ui-block-content ui-block-content-lb">
<!-- 		  	<form action="" id="searchGroupForm"> -->
			    <table >
			        <tr>
			            <td>
			            	<label><spring:message code='shop.property.group'/></label>
			            </td>
			            <td>
			                <span ><opt:select id="type" name="q_long_type" loxiaType="select" expression="chooseOption.MEMBER_GROUPTYPE" nullOption="member.group.label.unlimit"/></input></span>
			            </td>
			            <td>
			                <span ><a href="javascript:void(0);" class="func-button addPropertyValueGroup"   title="<spring:message code='btn.add'/>">
			                		<span><spring:message code="btn.add"/></span></a>
			                </span>
			            </td>
			            <td>
			                <span ><a href="javascript:void(0);" class="func-button updatePropertyValueGroup"   title="<spring:message code='btn.update'/>">
			                		<span><spring:message code="btn.update"/></span></a>
			                </span>
			            </td>
			        </tr>
			    </table>
<!-- 		    </form> -->
    		<div class="clear-line height10"></div>
	  	</div>
	    <div class="ui-block"> 
	   		<div class="border-grey" id="table2" caption="<spring:message code='member.group.membergroup.list'/>"></div>
	   		<%-- <div class="button-line1">
	    	 <a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='user.list.filter.btn'/>"><span><spring:message code="btn.all.delete"/></span></a>
	    	</div>  --%>
	   	</div>  
	</div>

	<div class="ui-block" style="margin-left:400px;width:auto;">
		<div class="ui-block-content" style="padding-top:0;padding-right:0;margin:15px 0px 0px 30px;">
		  	<form action="" id="searchGroupForm">
			    <table>
				    <tr colspan="2">属性值操作</tr>	
			        <tr >
			            <td >
			            	<label>属性值：</label>
			            </td>
			            <td >
			            	 <c:forEach items="${i18nLangs}" var="i18nLang" varStatus="status">
								   <div class="ui-block-line ">
							         <input type="text" id="title" name="itemCommand.title.values[${status.index}]" style='width: 200px' loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='item.update.name'/>"/>
							         <input class="i18n-lang" type="text" name="itemCommand.title.langs[${status.index}]" value="${i18nLang.key}" />
							        
							         <span>${i18nLang.value}</span>
								   </div>
						  	 </c:forEach>
			            </td>
			        </tr>
<!-- 			        <tr>
			        	<td> <input type="button" value="<spring:message code='btn.save'/>" class="button orange bind" title="<spring:message code='btn.save'/>"/></td>
			        	<td> <input type="button" value="<spring:message code='shop.property.value.save.continue'/>" class="button orange bind" title="<spring:message code='shop.property.value.save.continue'/>"/></td>
			        </tr>
			        <tr>
			        	<td> <input type="button" value="<spring:message code='shop.property.value.export'/>" class="button orange bind" title="<spring:message code='shop.property.value.export'/>"/></td>
			        	<td> 
			        	<input type="button" value="<spring:message code='shop.property.value.import'/>" class="button orange bind" title="<spring:message code='shop.property.value.import'/>"/>
			        	</td>
			        </tr>
			        <tr>
			        	<td> 
    				 		<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='user.list.filter.btn'/>">
    				 		<span><spring:message code="btn.all.delete"/></span></a>
    				 		<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='user.list.filter.btn'/>">
    				 		<span><spring:message code="btn.all.delete"/></span></a>
	        			</td>
			        </tr> -->
			    </table>
			    
		    </form>
    		
    		
    		 <div class="ui-block" style="margin:15px 0px 0px 50px;">
		   	    <table>
			        <tr border="1"  align="center">
			        	<td class="td_button_heigth"> 
    				 		<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='btn.save'/>">
    				 		<span><spring:message code="btn.save"/></span></a>
    				 	</td>
    				 	<td class="td_button_heigth"> 
    				 		<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.save.continue'/>">
    				 		<span><spring:message code="shop.property.value.save.continue"/></span></a>
	        			</td>
			        </tr>
			        <tr border="1"  align="center">
			        	<td class="td_button_heigth"> 
    				 		<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.export'/>">
    				 		<span><spring:message code="shop.property.value.export"/></span></a>
    				 	</td>
    				 	<td class="td_button_heigth"> 
    				 		<a href="javascript:void(0);" class="func-button deleteMultyGroup" title="<spring:message code='shop.property.value.import'/>">
    				 		<span><spring:message code="shop.property.value.import"/></span></a>
	        			</td>
			        </tr>
			    </table>
		    </div>
		    <div class="clear-line height10"></div>
		    
	  	</div>
	  	
   		<div class="ui-block-content" style="padding-top:0;padding-right:0;"> 
		    <div class="ui-block">
		   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code='member.group.member.list'/>"></div>   
		    </div>
	    </div>
	    
   	</div>
 

</div>



</body>
</html>
