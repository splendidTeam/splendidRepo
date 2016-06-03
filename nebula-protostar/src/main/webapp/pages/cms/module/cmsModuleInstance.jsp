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
<script type="text/javascript" src="${base}/scripts/cms/module/cmsModuleInstance.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">CMS模块实例管理
	 <input type="button"   value="返回" class="button butch return"  title="返回"/>
	  <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	  <input type="button" value="新增模块" class="button orange add" title="新增模块"/>
	</div>

	<div class="ui-block">
		<div class="ui-block-content-lb" style="padding-bottom: 10px;" >
			<table style="font-size: 14px;">
				<tr><td>所属模块:&nbsp;&nbsp;</td><td>${cmt.name}</td></tr>
			</table>
		</div>
	</div>
	<form id="searchForm">
		<input type="hidden" id="templateId" name="q_long_templateId" value="${cmt.id}" />
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
      		<td><label>模块编码</label></td>
			<td>
				<input name="q_string_code" type="text" loxiaType="input" ></input>
			</td>
			<td><label>模块名称</label></td>
			<td>
				<input name="q_sl_nameForLike" type="text" loxiaType="input" ></input>
			</td>
			<td><label>状态</label></td>
			<td>
				<span id="searchkeytext">
					<select id="lifecycle" name="q_int_ispublished" class="ui-loxia-default ui-corner-all" aria-disabled="false">
						<option value="">不限</option>
						<option value="1">已发布</option>
						<option value="0">未发布</option>
					</select>
				</span>
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
   	 	<div class="table-border0 border-grey" id="tableList" caption="模块实例列表"></div>   
    </div>
    
    <div class="button-line">
		<input type="button" value="新增模块" class="button orange add" title="新增模块"/>
		<input type="button"   value="<spring:message code='btn.all.delete'/>" class="button butch delete"  title="<spring:message code='btn.all.delete'/>"/>
		<input type="button"   value="返回" class="button butch return"  title="返回"/>
    </div>
</div>
</body>
</html>