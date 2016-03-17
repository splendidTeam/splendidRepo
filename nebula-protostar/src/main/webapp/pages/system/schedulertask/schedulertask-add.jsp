<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/system/schedulertask/schedulertask-add.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
    定时任务管理
    </div>

<div class="ui-tag-change">
<div class="tag-change-content">
            <div class="tag-change-in block">
                    <div class="ui-block">
                    <div class="ui-block-title1">新增定时任务</div>
  <form id="submitForm1" method="post" action="/system/schedulerTask/saveSchedulerTask.htm">
  <div class="ui-block-line">
    <label>任务编码</label>
    <input type="text" loxiaType="input" mandatory="true" value="" name="code" id="code" placeholder="任务编码" class="ui-loxia-default ui-corner-all" aria-disabled="false">
  	<a href="javascript:void(0);" data-val='CODE' class="func-button help" title="帮助">帮助</a>
  </div>
 <div class="ui-block-line">
    <label>任务描述</label>
    <input type="text" loxiaType="input" mandatory="true" value="" name="description" id="description" placeholder="任务描述" class="ui-loxia-default ui-corner-all" aria-disabled="false">
    <a href="javascript:void(0);" data-val='DESCRIPTION' class="func-button help" title="帮助">帮助</a>
  </div>
  <div class="ui-block-line">
    <label>Bean Name</label>
    <input type="text" loxiaType="input" mandatory="true" value="" name="beanName" id="beanName" placeholder="Bean Name" class="ui-loxia-default ui-corner-all" aria-disabled="false" checkmaster="checkBeanName">
  	<a href="javascript:void(0);" data-val='BEAN_NAME' class="func-button help" title="帮助">帮助</a>
  </div>
  <div class="ui-block-line">
    <label>方法名</label>
    <input type="text" loxiaType="input" mandatory="true" value="" name="methodName" id="methodName" placeholder="方法名" class="ui-loxia-default ui-corner-all" aria-disabled="false" checkmaster="checkMethodName">
  	<a href="javascript:void(0);" data-val='METHOD_NAME' class="func-button help" title="帮助">帮助</a>
  </div>
  <div class="ui-block-line">
    <label>时间表达式</label>
    <input type="text" loxiaType="input" checkmaster="checkTimeExp" mandatory="true" value="" name="timeExp" id="timeExp" placeholder="定时表达式" class="ui-loxia-default ui-corner-all" aria-disabled="false">
 	<a href="javascript:void(0);" data-val='TIME_EXP' class="func-button help" title="帮助">帮助</a>
  </div>
  
  <div class="button-line">
  		<input type="button" id="submitButton" title="<spring:message code='item.searchCodition.submit'/>" value="<spring:message code='item.searchCodition.submit'/>" class="button orange submit"> 
		<input type="button" id="canel" title="<spring:message code='item.searchCodition.back'/>" value="<spring:message code='item.searchCodition.back'/>" class="button goBackBtn"> 
  </div>
  
  </form>
</div>
</div>
</div>
</div>

<div id="dialog-help" class="proto-dialog" style="OVERFLOW-Y: auto; OVERFLOW-X:hidden;">
	<h5>帮助</h5>
	 <div id="dialog-text" style="height: 125px">
           
      </div>
      <div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>

</div>


</body>
</html>