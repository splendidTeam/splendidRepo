<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>无标题文档</title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/rule/custom-filter-update.js"></script>
</head>

<body>

<div class="content-box width-percent100">
	
    <form name="csFilterForm" action="/rule/saveCustomFilter.json" method="post">
	    <div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">自定义筛选器管理 </div> 
    
    	<div class="ui-block">	
		 	<div class="ui-block-title1">更新自定义筛选器</div>
			<div class="ui-block-content border-grey">
			
				<input name="id" id ="fiterId" value="${customizeFilterClass.id }" type="hidden"/>
		
				<div class="ui-block-line ">
			         <label>自定义筛选器名称</label>
			         <div>
			              <input type="text" name="scopeName" id="scopeName" loxiaType="input" value="${customizeFilterClass.scopeName }" mandatory="true" placeholder="自定义筛选器名称"/>
			         </div>
			    </div>
				
				<div class="ui-block-line ">
			         <label>自定义筛选器类型</label>
			         <div>
			              <opt:select name="scopeType" id="scopeType"  defaultValue="${customizeFilterClass.scopeType }" loxiaType="select" expression="chooseOption.PRODUCT_CUSTOMIZE_FILTER"/>
			         </div>
			    </div>
			    
			    <div class="ui-block-line ">
			         <label>店铺名称</label>
			         <div>
			            <select name="shopId" id="shopSelect" loxiaType="select" style="float:left;">
		
		    				<c:forEach var="shopCommand" items="${shopCommandList}">
		    					
	    							<option value="${shopCommand.shopid }" 
	    								<c:if test="${not empty customizeFilterClass.shopId && shopCommand.shopid 
	    								eq customizeFilterClass.shopId}">selected</c:if>>${shopCommand.shopname }
	    							</option>
		    					
		    				</c:forEach>
		    			</select>
			         </div>
			    </div>
			    
			    <div class="ui-block-line ">
			         <label>自定义服务名称</label>
			         <div>
			              <input type="text" name="serviceName" loxiaType="input" value="${customizeFilterClass.serviceName }" mandatory="true" placeholder="自定义服务名称" checkmaster="checkServiceName"/>
			         </div>
			    </div>
			    
			    <div class="ui-block-line ">
			         <label>缓存时间(秒)</label>
			         <div>
			              <input type="text" name="cacheSecond" loxiaType="number" value="${customizeFilterClass.cacheSecond }" mandatory="true" placeholder="0"/>&nbsp;&nbsp;<span class="red">注: "0"表示不缓存</span>
			         </div>
			    </div>
			    
			    <input name="lifecycle" id ="curLifecycle" value="${customizeFilterClass.lifecycle }" type="hidden"/>
   			</div>
   		</div>
    
    </form>
    <div class="button-line">
    	 
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit" />
         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return" />
    </div>
    
    <a href="javascript:void(0);" class="func-button search1" title="测试结果">测试结果</a>
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="测试结果"></div>   
    </div>
     
    
    
    
    
</div>

</body>
</html>
