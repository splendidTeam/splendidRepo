<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/system/option/choose-option-group.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


</head>
<body>
<div class="content-box">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/compass.png"><spring:message code="chooseoption.group.manage"/>
	         <input type="button" value="<spring:message code='chooseoption.group.add'/>" class="button orange addchoosegp" title="<spring:message code='chooseoption.group.add'/>"/>
    </div>
  <form id="searchForm">
	<div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
		    <table>
		        <tr>
		            <td><label><spring:message code="chooseoption.group.code"/></label></td>
		            <td>
		                <span id="searchkeytext"><input type="text" name="q_sl_groupCode" loxiaType="input" mandatory="false" placeholder="<spring:message code='chooseoption.group.code'/>"></input></span>
		            </td>
		
		      		 <td><label><spring:message code="chooseoption.group.desc"/></label></td>
		            <td>
		                <span id="searchkeytext"><input type="text" name="q_sl_groupDesc" loxiaType="input" mandatory="false" placeholder="<spring:message code='chooseoption.group.desc'/>"></input></span>
		            </td>
		        </tr>
		    </table>
		    <div class="button-line1">
		        <a href="javascript:void(0);" class="func-button search"><span><spring:message code="btn.search"/></span></a>
		    </div>
    	</div>
	</div>
	</form>
	
	<div class="ui-block">
		 <div class="border-grey" id="table1" caption="<spring:message code='chooseoption.group.list'/>"></div>
	</div>
	<div class="button-line">
        <input type="button" value="<spring:message code='chooseoption.group.add'/>" class="button orange addchoosegp" title="<spring:message code='chooseoption.group.add'/>"/>
    </div>
</div>
</body>
</html>
