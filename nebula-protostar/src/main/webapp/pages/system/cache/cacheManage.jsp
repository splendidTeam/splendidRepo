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
<script type="text/javascript" src="${base}/scripts/system/cache/cacheManage.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
</head>
<body>
<div class="content-box">
	  <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/cube.png"><spring:message code="redis.cache.manage"/>
      </div>
	  <form id="searchForm">
		<div class="ui-block">
	    	<div class="ui-block-content ui-block-content-lb">
			    <table>
			        <tr>
			            <td><label><spring:message code="redis.cache.item"/></label></td>
			            <td>
			                <span id="searchkeytext"><input type="text" name="q_sl_name" loxiaType="input" mandatory="false" placeholder="<spring:message code='redis.cache.item'/>"></input></span>
			            </td>
			      		<td><label><spring:message code="redis.cache.desc"/></label></td>
			            <td>
			                <span id="searchkeytext"><input type="text" name="q_sl_desc" loxiaType="input" mandatory="false" placeholder="<spring:message code='redis.cache.desc'/>"></input></span>
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
		 <div class="border-grey" id="table1" caption="<spring:message code='redis.cache.list'/>"></div>
	  </div>
</div>
</body>
</html>
