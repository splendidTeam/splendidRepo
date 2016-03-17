<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/rule/custom-filter-add.js"></script>
</head>

<body>

<div class="content-box">
    <form name="csFilterForm" action="/rule/saveCustomFilter.json" method="post">
    	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">自定义筛选器条件管理</div> 
    
    	<div class="ui-block">	
		 	<div class="ui-block-title1">添加自定义筛选器条件</div>
			<div class="ui-block-content border-grey">
		
				<div class="ui-block-line ">
			         <label>自定义筛选器条件名称</label>
			         <div>
			              <input type="text" name="scopeName" id="scopeName" loxiaType="input" value="" mandatory="true" placeholder="自定义筛选器条件名称"/>
			         </div>
			    </div>
				
				<div class="ui-block-line ">
			         <label>自定义筛选器条件类型</label>
			         <div>
			              <opt:select name="scopeType" id="scopeType" loxiaType="select" expression="chooseOption.PRODUCT_CUSTOMIZE_FILTER"/>
			         </div>
			    </div>
			    
			    <div class="ui-block-line ">
			         <label>店铺名称</label>
			         <div>
			            <select name="shopId" id="shopSelect" loxiaType="select" style="float:left;">
		
		    				<c:forEach var="shopCommand" items="${shopCommandList}">
		    					<option value="${shopCommand.shopid }">${shopCommand.shopname }</option>
		    				</c:forEach>
		    			</select>
			         </div>
			    </div>
			    
			    <div class="ui-block-line ">
			         <label>自定义服务名称</label>
			         <div>
			              <input type="text" name="serviceName" loxiaType="input" value="" mandatory="true" placeholder="自定义服务名称" checkmaster="checkServiceName"/>
			         </div>
			    </div>
			    
			    <div class="ui-block-line ">
			         <label>缓存时间(秒)</label>
			         <div>
			              <input type="text" name="cacheSecond" loxiaType="number" value="" mandatory="true" placeholder="0"/>&nbsp;&nbsp;<span class="red">注: "0"表示不缓存</span>
			         </div>
			    </div>
   			</div>
   		</div>
    
    </form>
    
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit" />
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />
    </div>
    
</div>

</body>
</html>
