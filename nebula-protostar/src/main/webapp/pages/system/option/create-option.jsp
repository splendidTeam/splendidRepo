<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="choose.option.update.maintaince"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/system/option/choose-option-list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>



<body>

<div class="content-box">
	<form name="updateForm" action="/option/saveOption.json" method="post">
		<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/compass.png"><spring:message code="choose.option.update.maintaince"/>
	        <input type="button" value="<spring:message code='btn.return'/>" class="button orange returnUpdate" title="<spring:message code='btn.return'/>"/>
	        <input type="button" value="<spring:message code='btn.save'/>"   class="button orange saveopt" title="<spring:message code='btn.save'/>"/>
	        <input type="hidden" id="groupCode" name="groupCode" value="${chooseOption.groupCode}">
	        <input type="hidden" id="lifecycle" name="lifecycle" value="${chooseOption.lifecycle}">
	        <input type="hidden" id="groupDesc" name="groupDesc" value="${chooseOption.groupDesc}">
		</div>
	
		<div class="ui-block">
		
		    <div class="ui-block-title1"><spring:message code="choose.option.create.caddOption"/></div>
		   		<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
		            <div class="ui-block-line">
				        <label><spring:message code="choose.option.list.name"/></label>
				        <div class="wl-right">
							<input name="optionLabel" id="optionLabel" type="text" loxiaType="input" value="${chooseOption.optionLabel}" mandatory="true" placeholder="<spring:message code="choose.option.list.name"/>"/>
				        </div>
			    	</div>
			    
				    <div class="ui-block-line">
				        <label><spring:message code="chooseoption.group.add.optionvalue"/></label>
				        <div class="wl-right">
							<input name="optionValue" id="optionValue" type="text" loxiaType="input" value="${chooseOption.optionValue}" mandatory="true" placeholder="<spring:message code="chooseoption.group.add.optionvalue"/>"/>
				        </div>
				    </div>
				    
				    <div class="ui-block-line">
				        <label><spring:message code="chooseoption.group.add.sortno"/></label>
				        <div class="wl-right">
							<input name="sortNo" id="sortNo" type="text" loxiaType="input" value="${chooseOption.sortNo}" mandatory="true"/>
				        </div>
				    </div>
		    	</div>
		    </div>
		</form>
		<div class="button-line">
		        <input type="button" value="<spring:message code='btn.save'/>" class="button orange saveopt" title="<spring:message code='btn.save'/>"/>
		        <input type="button" value="<spring:message code='btn.return'/>" class="button orange returnUpdate" title="<spring:message code='btn.return'/>"/>
		</div>
	
	
    
</div>
</body>
</html>
