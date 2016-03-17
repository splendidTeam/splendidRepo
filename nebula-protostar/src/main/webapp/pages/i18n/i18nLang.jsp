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
<script type="text/javascript" src="${base}/scripts/i18n/i18nLang.js"></script>
<style type="text/css">
.edit_tbl .lh {
      height: 5px;
}
</style>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">国际化语言管理
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addI18nLang" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
       			<td><label>编码</label></td>
				<td>
					<input name="q_string_key" type="text" loxiaType="input" ></input>
				</td>
       			<td><label>名称</label></td>
				<td>
					<input name="q_string_value" type="text" loxiaType="input" ></input>
				</td>
        </tr> 
    </table>
    	<div class="button-line1">
    		<a href="javascript:void(0);" class="func-button reset" title="<spring:message code ='user.list.filter.btn'/>">重置</a>
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="tableList" caption="国际化语言列表"></div>   
    </div>
     <div class="ui-title1">
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addI18nLang" title="<spring:message code="btn.add" />"/>
	</div>
</div>

 <div id="dialog-i18n-win" class="proto-dialog" wid="">
		<h5  id="wc_title" >国际化语言维护</h5>
		<div class="proto-dialog-content p10">
			<table style="width: 400px" class="edit_tbl">
				<tr>
	       			<td><label>编码</label></td>
					<td>
						<input class="input_add key" type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr>
		        <tr class="lh"></tr>
		        <tr>
	       			<td><label>名称</label></td>
					<td>
						<input class="input_add value"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr>
		        <tr class="lh"></tr>
		         <tr>
	       			<td><label>分词器</label></td>
					<td>
						<input class="tokenizer"  type="text" loxiaType="input">
					</td>
		        </tr>
		        <tr class="lh"></tr>
		         <tr>
	       			<td><label>排序</label></td>
					<td>
						<input class="sort" loxiaType="number">
					</td>
		        </tr>
		         <tr class="lh"></tr>
		          <tr>
	       			<td><label>默认语言</label></td>
					<td>
						<select class="defaultlang" style="width: 160px;height: 24px;" >
							<option value="0">否</option>
							<option value="1">是</option>
						</select>
					</td>
		        </tr>
    		</table>
		</div>
		<div  class="proto-dialog-button-line">
          	 <input type="button" id="confirm_warning" value="<spring:message code="btn.confirm"/>" class="button orange">  
        </div>
</div>
 
</body>
</html>