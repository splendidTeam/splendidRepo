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
<script type="text/javascript" src="${base}/scripts/cms/module/cmsModuleInstanceVersion.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">CMS模块实例版本管理
	 <input type="button"   value="返回" class="button butch return"  title="返回"/>
	  <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	  <input type="button" value="新增模块版本" class="button orange add" title="新增模块版本"/>
	</div>

	<div class="ui-block">
		<div class="ui-block-content-lb" style="padding-bottom: 10px;" >
			<table style="font-size: 14px;">
				<tr><td>所属实例:&nbsp;&nbsp;</td><td>${cmi.name}</td></tr>
			</table>
		</div>
	</div>
	<form id="searchForm">
		<input type="hidden" id="instanceId" name="q_long_instanceId" value="${cmi.id}" />
		<input type="hidden" id="templateId" name="q_long_templateId" value="${cmi.templateId}" />
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
			<td><label>模块版本名称</label></td>
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
   	 	<div class="table-border0 border-grey" id="tableList" caption="模块实例版本列表"></div>   
    </div>
    
    <div class="button-line">
		<input type="button" value="新增模块版本" class="button orange add" title="新增模块版本"/>
		<input type="button"   value="<spring:message code='btn.all.delete'/>" class="button butch delete"  title="<spring:message code='btn.all.delete'/>"/>
		<input type="button"   value="返回" class="button butch return"  title="返回"/>
    </div>
</div>

<div class="proto-dialog cms-publish-dialog" wid="">
    <h5>页面模块版本发布</h5>
    <div class="proto-dialog-content p10">
	<div class="ui-block">
		<div class="ui-block-line mt5 ui-block-content-lb">
			<span>页面模块必须要是发布的状态</span><br>
			<span>开始时间和结束时间为必填项</span><br>
<!-- 			<span>如果都设置:生效时间为开始时间,失效世界为结束时间</span><br> -->
<!-- 			<span>如果只设置开始时间,不设置结束时间:发布后到开始时间才生效,且不会失效</span><br> -->
<!-- 			<span>如果只设置结束时间,不设置开始时间:发布后就生效,到结束时间就失效</span> -->
		</div>
        <div class="ui-block-line mt5"  >
			<label title="">开始时间</label>
			<input loxiaType="date" class="starttime" showtime="true"/>
		  </div>
		  <div class="ui-block-line mt5" >
			<label>结束时间</label>
			<input loxiaType="date" class="endtime" showtime="true"/>
		  </div>
	  </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="发布" class="button orange mr5 confrim" />
          <input type="button" value="关闭" class="button cancel close"/>
     </div>
</div>

<div class="proto-dialog cms-copy-dialog" wid="">
    <h5>页面版本复制</h5>
    <div class="proto-dialog-content p10">
	<div class="ui-block">
		<div class="ui-block-line mt5 ui-block-content-lb">
			<span>复制页面版本的名称为必填项</span><br>
		</div>
        <div class="ui-block-line mt5"  >
			<label title="">复制页面版本名称</label>
			<input type="text" loxiaType="input" class="copyname" />
		  </div>
	  </div>
     </div>
     <div class="proto-dialog-button-line">
          <input type="button" value="复制" class="button orange mr5 confrim" />
          <input type="button" value="关闭" class="button cancel close"/>
     </div>
</div>
</body>
</html>