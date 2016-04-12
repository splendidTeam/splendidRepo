<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="industry.list.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<link rel="stylesheet" type="text/css" href="${base}/css/industry-property.css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/dragarraylist.js"></script>
<script type="text/javascript" src="${base}/scripts/product/property/common-property.js"></script>
<script type="text/javascript" src="${base}/scripts/product/industry/industry-property.js?v=25"></script>
<script src="${base}/scripts/layer/layer.js"></script>
<SCRIPT type="text/javascript">	
//<spring:message code="industry.list.expand"/>ROOT
var zNodes =[           
	{ id:0, name:"ROOT",state:"0", open:true,root:"true"},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
		<c:if test="${industry.lifecycle==1}">
			{id:${industry.id}, pId:${null eq industry.parentId?0:industry.parentId}, name: "${industry.name}", state:"${industry.lifecycle}",open:${0 eq industry.parentId?true:false}}<c:if test="${!status.last}">,</c:if>
		</c:if>
	</c:forEach>
];
// var category_ZNodes =${commonPropertyJSON };
</SCRIPT>		
</head>
<body>
	<div class="content-box">  
		<div class="ui-title1">
			<img src="${base}/images/wmi/blacks/32x32/spechbubble.png"><spring:message code="system.industry.property.manager"/>
		</div>
		
		<div class="ui-block-1">
			<div id="industrySelDiv-1" style="">
				<div class="ui-block" >
				    <div class="ui-block-title-1"><spring:message code="system.property.select"/></div>
				    <div class="ui-block-content-1 border-grey" id="industryDiv" >	
					        <div class="ui-block-content-1 ui-block-content-lb">
					         	<ul id="tree" class="ztree"></ul>
					        </div>
				    </div>
				</div>
			</div>
			
			<div id="propertyDiv-1" style="">
				<div class="ui-block ui-block-fleft w240">
			        
			        <div class="ui-block-content ui-block-content-lb">
			         	
						<div class="ui-block-title1" id="industryPropertyDiv"><spring:message code="system.property.list"/></div>
					
						<ul id="property-list" class="list-all">
			
						</ul>
			            
			            <div class="button-line1">
				      
				         <input type="button" value="<spring:message code="system.property.savesort"/>" class="buttonSort" title="<spring:message code="system.property.savesort"/>"/>
				         
				   		</div>
				   		
				   	</div>
			          
			    </div>
			 </div>
			 <div id="propertySelDiv-1">
			 	<div class="ui-block ui-block-fleft w240">
			        
			        <div class="ui-block-content ui-block-content-lb">
			         	
						<div class="ui-block-title1" id="PropertyDiv"><spring:message code="system.property.selectable"/></div>
						<div id="enableSelectProperty">
							<div><input name="propertyList" class="enableSelectPropertyList" type="checkbox" value="" />颜色</div>
							<div><input name="propertyList" class="enableSelectPropertyList" type="checkbox" value="" />尺寸</div>
							<div><input name="propertyList" class="enableSelectPropertyList" type="checkbox" value="" />科技</div>
							<div><input name="propertyList" class="enableSelectPropertyList" type="checkbox" value="" />面料</div>
						</div>
				   	</div>
			          
			    </div>
			 </div>
		 </div>
	</div>
</body>
</html>