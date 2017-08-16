<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/system/metaInfo/mataInfo.js"></script>

<style type="text/css">
td.col-2 span {
    display: inline-block;
    word-wrap: break-word;
    word-break:break-all;
}
</style> 
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png"><spring:message code="sys.meta.config.manage"/>
		 <!-- 列表上的三个按钮  -->
		 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
		 <input type="button" value="<spring:message code="component.enable"/>" class="button orange mataInfoActive" title="<spring:message code="component.enable"/>"/>
		 <input type="button" value="<spring:message code="btn.add" />" class="button orange addMataInfo" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
	    <div class="ui-block-content ui-block-content-lb">
		    <table >
		        <tr>
		      		<td><label><spring:message code="navigation.list.parameter"/></label></td>
					<td>
						<input name="q_string_code" type="text" loxiaType="input" ></input>
					</td>
					
					<!-- 搜索添加说明字段的搜索条件 -->
					<td><label><spring:message code="navigation.list.declare"/></label></td>
					<td>
						<input name="q_string_declare" type="text" loxiaType="input" ></input>
					</td>
					
					
		      		<td><label><spring:message code="sys.meta.config.value"/></label></td>
					<td>
						<input name="q_sl_value" type="text" loxiaType="input" ></input>
					</td>
					
		      		<td><label><spring:message code="role.list.label.status"/></label></td>
					<td>
						<opt:select id="isused" name="q_int_lifecycle" loxiaType="select"  expression="chooseOption.IS_AVAILABLE" nullOption="member.group.label.unlimit" />
					</td>
		        </tr> 
		    </table>
	    	<div class="button-line1">
	    		<!-- 搜索 重置按钮 -->
	    		<a href="javascript:void(0);" class="func-button reset" title="<spring:message code="btn.reset"/>"><spring:message code="btn.reset"/></a>
	        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
	        </div>
	    </div>
    </div>
    </form>

    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="tableList" caption="<spring:message code="sys.meta.config.manage.list"/>"></div>   
    </div>
</div>


<!-- 列表下的三个按钮  -->
<div class="ui-title1">
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="component.enable"/>" class="button orange mataInfoActive" title="<spring:message code="component.enable"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addMataInfo" title="<spring:message code="btn.add" />"/>
</div>

	<!-- 修改弹出的页面 -->
	<div id="dialog-meta-select" class="proto-dialog" wid="">
		<h5  id="wc_title" ><spring:message code="sys.warning.manager"/>-</h5>
		<div class="proto-dialog-content p10">
			<table style="width: 500px" class="edit_tbl">
				<tr>
	       			<td><label><spring:message code="navigation.list.parameter"/></label></td>
					<td>
						<input class="input_add code" type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr>
		        
		          <!-- declare字段修改弹框页面 -->
		        <tr>
	       			<td><label><spring:message code="navigation.list.declare"/></label></td>
					<td>
						<input class="input_add declare" type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr>
		        
		        <tr>
	       			<td><label><spring:message code="sys.meta.config.value"/></label></td>
					<td>
						<input class="input_add value"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr>
		        
    		</table>
		</div>
		<div  class="proto-dialog-button-line">
          	 <input type="button" id="confirm_warning" value="<spring:message code="btn.confirm"/>" class="button orange">  
             <input type="button" id="cancel" value="<spring:message code="item.searchCodition.canel"/>" class="button delete">
        </div>
	</div>
     
</body>
</html>
