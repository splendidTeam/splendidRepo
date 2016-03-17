<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/cmsTree.js"></script>
<script type="text/javascript" src="${base}/scripts/cms/cms-area-list.js"></script>
<script type="text/javascript">
//新增区域默认的路径
var defaultPath = "${defaultPath}";

//
var templateDefinitionRoot="${templateDefinitionRoot}";

</script>
</head>
<body>
<div class="content-box width-percent100">
		<div class="ui-title1"><img src="../images/wmi/blacks/32x32/tag.png"><spring:message code="area.areaManagement"/>
						<input type="button" value="<spring:message code="area.invalid"/>" title="<spring:message code="area.invalid"/>"  class="button disable"/>
						<input type="button" value="<spring:message code="area.effect"/>" title="<spring:message code="area.effect"/>"  class="button enable"/>
						<input type="button" value="<spring:message code="btn.delete"/>" title="<spring:message code="btn.delete"/>"  class="button remove"/>
						
						<input type="button" value="<spring:message code="area.copy"/>" title="<spring:message code="area.copy"/>"  class="button copy"/>
		                <input type="button" value="<spring:message code="btn.add"/>" title="<spring:message code="btn.add"/>"  class="button orange add" />
		</div>

        <div class="ui-block ui-block-fleft w240" style="height:458px">
            <div class="ui-block-content ui-block-content-lb">
                <div class="tree-control">
                    <input type="text" id="key" loxiatype="input" placeholder="<spring:message code="item.search.keyword"/>" />
                	<div><span id="search_result"></span></div>
                </div>
                <ul id="tree" class="ztree"></ul>

            </div>
        </div>
        
        <div class="ui-block ml240">
		    <div class="ui-block-content" style="padding-top:0;padding-right:0;">
			
			    <div class="ui-block">
			    	
			   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code="area.areaList"/>"></div>   
			    </div>
				
		    </div>
	   	</div>
		<div class="button-line">
                        <input type="button" value="<spring:message code="btn.add"/>" title="<spring:message code="btn.add"/>"  class="button orange add" />
						<input type="button" value="<spring:message code="area.copy"/>" title="<spring:message code="area.copy"/>"  class="button copy"/>
						<input type="button" value="<spring:message code="btn.delete"/>" title="<spring:message code="btn.delete"/>"  class="button remove"/>
						<input type="button" value="<spring:message code="area.effect"/>" title="<spring:message code="area.effect"/>"  class="button enable"/>
						<input type="button" value="<spring:message code="area.invalid"/>" title="<spring:message code="area.invalid"/>"  class="button disable"/>
        	</div>	   	
    </div>
    <div id="categoryContent" class="menuContent" style="z-index:999;display:none; position: absolute; background-color:#f0f6e4;border: 1px solid #617775;padding:3px;">
	   <ul id="categoryDemo" class="ztree" style="margin-top:0; width:180px; height: 100%;"></ul>
    </div>
	
	<div id="copy-dialog" class="proto-dialog">
		<h5><spring:message code="area.copyInfo"/></h5>

		 <div class="proto-dialog-content">
			 <div class="ui-block-line">
			 		 <label><spring:message code='area.sourcePath'/></label>
					<div>    
					  <input type="text" readonly id="copy-source-path" loxiaType="input" value="" mandatory="false"/>
					</div>
					
					<label><spring:message code="area.copyTO"/>：</label>
					<div> 
					<input type="text" id="copy-to-path" loxiaType="input" value="" mandatory="false"/>
					</div>
			 </div>
		 </div>
		 <div class="proto-dialog-button-line">
		 	  <input type="button" value="<spring:message code="btn.confirm"/>" class="button orange copyok"/>
		 	  
		 	  <input type="button" value="<spring:message code="btn.cancel"/>" class="button orange copycancel"/>
		 </div>
		
	</div>
</body>
</html>
