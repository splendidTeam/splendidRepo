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
<script type="text/javascript" src="${base }/scripts/product/item/itemSolrSetting.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>
</head>
<body>
	
<div class="content-box width-percent100">
	<input type="hidden" id="pid" value="${searchConditionVo.id}"  mandatory="false"/>
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
    <spring:message code='item.solrSetting.manager'/>
	    
	    <input type="button" id="deleteSelect" effect='changeAll' value="<spring:message code='item.solrSetting.deleteAll'/>" methodType="delete" class="button butch backCondition" title="<spring:message code='item.solrSetting.deleteAll'/>"/>
	    <input type="button" id="updateSelect" effect='changeAll' value="<spring:message code='item.solrSetting.updateAll'/>" methodType="update" class="button butch backCondition" title="<spring:message code='item.solrSetting.updateAll'/>"/>
	    <input type="button" id="deleteAll" effect='changeSelect' value="<spring:message code='item.solrSetting.deleteSelect'/>" methodType="delete" class="button orange backCondition" title="<spring:message code='item.solrSetting.deleteSelect'/>"/>
	    <input type="button" id="updateAll" effect='changeSelect' value="<spring:message code='item.solrSetting.updateSelect'/>" methodType="update" class="button orange backCondition" title="<spring:message code='item.solrSetting.updateSelect'/>"/>
	    <input type="button" id="batchOeration" effect='batchOeration' value="<spring:message code='item.solrSetting.batch.operation'/>" methodType="update" class="button orange backCondition" title="<spring:message code='item.solrSetting.batch.operation'/>"/>
    </div>
	<form id="searchForm">
	  <div class="ui-block">
	
    <div class="ui-block-content ui-block-content-lb">
	    <table >
	        <tr>
	        	<td><label><spring:message code='item.solrSetting.keyWords'/></label></td>
	            <td><input type="text" id="name" placeHolder="<spring:message code='item.solrSetting.keyWords'/>" name="q_string_keyWords" loxiaType="input" mandatory="false"></input></td>
	        </tr>
	 
	    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" id="search" class="func-button search" title="<spring:message code='item.searchCodition.search'/>"><spring:message code='item.searchCodition.search'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="索引列表"></div>
    </div> 
</div>

<div id="batch-operation-dialog" class="proto-dialog">
	<h5><spring:message code="item.solrSetting.batch.operation"/></h5>
	<div class="proto-dialog-content">
		<div class="ui-block-line">
			<label style="line-height: 150px; margin-left: 20px;"><spring:message code="item.code"/>:</label>
			<div><textarea loxiaType="input" mandatory="true" selectonfocus="true" id="itemCodes" style="width: 300px; height: 150px;"></textarea></div>
			<div style="float: right; margin: -140px 10px 0 0;">
				<br>
				每条编码换行填写<br>
				格式如下：<br>
				Nb_Sample_Code_001<br>
				Nb_Sample_Code_002<br>
				Nb_Sample_Code_003<br>
				Nb_Sample_Code_004<br>
				Nb_Sample_Code_005
			</div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		  <input type="button" value="<spring:message code="item.solrSetting.updateSelect"/>" class="button orange" id="batch-update"/>
		  <input type="button" value="<spring:message code="item.solrSetting.deleteSelect"/>" class="button orange" id="batch-detele"/>
		  <input type="button" value="<spring:message code="btn.cancel" />" class="button orange" id="cancel-dialog"/>
	</div>
</div>
