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
<script type="text/javascript" src="${base }/scripts/rule/item-tag-rule-list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>
<SCRIPT type="text/javascript">	
var category_ZNodes = [
		{id:0, pId:-1, name:"ROOT",
			  code:"ROOT", sortNo:1,
			  open:true, lifecycle:1},  
              <c:forEach var="category" items="${categoryList}" varStatus="status">
              	
              	{id:${category.id}, pId:${category.parentId}, name:"${category.name}", 
              		code:"${category.code}", sortNo:${category.sortNo}, 
              		open:false, lifecycle:${category.lifecycle}}
              	<c:if test="${!status.last}">,</c:if>
              </c:forEach>
         ];
var  pdp_base_url = "${pdp_base_url}";   
</SCRIPT>
</head>
<body>
	
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">  
           <spring:message code="item.filter.title.search"/>
    <input type="button" value="<spring:message code="promotion.editorList.create"/>" class="button orange addcomboproduct" title="<spring:message code="promotion.editorList.create"/>"/>
    </div>  
	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table > 
        <tr> 
            <td><label><spring:message code="member.filter.name"/></label></td>
            <td><input type="text" id="comboName" placeHolder="<spring:message code="member.filter.name"/>" name="q_sl_name" loxiaType="input" mandatory="false"></input></td>  
                  
		   <td><label><spring:message code="member.filter.type"/></label></td>
             <td> 
             	<opt:select name="q_long_type" id="comboType" expression="chooseOption.ITEM_FILTER_TYPE" loxiaType="select" nullOption="role.list.label.unlimit" />
             </td>

        </tr>
 
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block"> 
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code ='item.filter.search.list'/>"></div>   
    </div> 
</div> 		  

<%-------------------------------------------------------- 查看浮层 --------------------------------------------------------%>
<%----------------------- 商品   ---------------------------%>
<div id="view-block-item" class="proto-dialog">
	<h5><spring:message code="item.filter.filter"/></h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block-line p5">
 	  		<label><spring:message code="member.filter.type"/></label>
	 	  	<opt:select cssClass="txt-type" expression="chooseOption.ITEM_FILTER_TYPE" loxiaType="select" />
		</div>
		<div class="ui-block-line p5">
			<label><spring:message code="member.filter.name"/></label>
			<div class="wl-right">
				<input class="txt-name" loxiaType="input" value="XXX" disabled="disabled" />
			</div>
		</div>	
		<div class="ui-block">
			 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
			 	<div class="include-list" style="float: left; width: 100%; margin-right: 20px;">
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
			 	  <table class="pt10" cellspacing="0" cellpadding="0">
			 	  		 <thead>
							<tr>
								<th><spring:message code="cms.resource.name"/></th>
								<th id="item-price"><spring:message code="item.filter.item.price"/></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
			 	  </table>
			 </div>
			 
			 </div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>

<%----------------------- 分类   ---------------------------%>
<div id="view-block-category" class="proto-dialog">
	<h5><spring:message code="item.filter.filter"/></h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block-line p5">
 	  		<label><spring:message code="member.filter.type"/></label>
	 	  	<opt:select cssClass="txt-type" expression="chooseOption.ITEM_FILTER_TYPE" loxiaType="select" />
		</div>
		<div class="ui-block-line p5">
			<label><spring:message code="member.filter.name"/></label>
			<div class="wl-right">
				<input class="txt-name" loxiaType="input" value="XXX" disabled="disabled" />
			</div>
		</div>	
		<div class="ui-block">
			 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
			 	<div class="include-list" style="float: left; width: 55%; margin-right: 20px;">
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
				
			 	  <table class="pt10" cellspacing="0" cellpadding="0">
			 	  		 <thead>
							<tr>
								<th><spring:message code="cms.resource.name"/></th>
								<th id="item-price"><spring:message code="item.filter.item.price"/></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
			 	  </table>
			 </div>
			 <div class="exclude-list" style="float: right; width: 40%;">
				<div class="bold p5"><spring:message code="member.filter.excludeList"/></div>
		 	  <table class="pt10" cellspacing="0" cellpadding="0">
		 	  		 <thead>
						<tr>
							<th><spring:message code="cms.resource.name"/></th>
							<th id="exc-item-price"><spring:message code="item.filter.item.price"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 	  </table>
		 	  </div>
			 </div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>

<%----------------------- 自定义筛选器   ---------------------------%>
<div id="view-block-custom" class="proto-dialog">
	<h5><spring:message code="item.filter.filter"/></h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block-line p5">
 	  		<label><spring:message code="member.filter.type"/></label>
	 	  	<opt:select cssClass="txt-type" expression="chooseOption.ITEM_FILTER_TYPE" loxiaType="select" />
		</div>
		<div class="ui-block-line p5">
			<label><spring:message code="member.filter.name"/></label>
			<div class="wl-right">
				<input class="txt-name" loxiaType="input" value="XXX" disabled="disabled" />
			</div>
		</div>	
		<div class="ui-block">
			<div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
				<div class="include-list" style="float: left; width: 100%; margin-right: 20px;">
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
		 		<table class="pt10" cellspacing="0" cellpadding="0">
		 			<thead>
						<tr>
							<th><spring:message code="cms.resource.name"/></th>
							<th id="item-price"><spring:message code="item.filter.item.price"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 		</table>
			</div>
			</div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>

<%----------------------- 组合   ---------------------------%>
<div id="view-block-all" class="proto-dialog">
	<h5><spring:message code="item.filter.filter"/></h5>
	<div class="proto-dialog-content p10">
		<div class="ui-block-line p5">
 	  		<label><spring:message code="member.filter.type"/></label>
	 	  	<opt:select cssClass="txt-type" expression="chooseOption.ITEM_FILTER_TYPE" loxiaType="select" />
		</div>
		<div class="ui-block-line p5">
			<label><spring:message code="member.filter.name"/></label>
			<div class="wl-right">
				<input class="txt-name" loxiaType="input" value="XXX" disabled="disabled" />
			</div>
		</div>	
		<div class="ui-block">
			<div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
				<div class="include-list" style="float: left; width: 100%; margin-right: 20px;">
				<div class="bold p5"><spring:message code="member.filter.includeList"/></div>
		 		<table class="pt10" cellspacing="0" cellpadding="0">
		 			<thead>
						<tr>
							<th><spring:message code="cms.resource.name"/></th>
							<th id="item-price"><spring:message code="item.filter.item.price"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
		 		</table>
			</div>
			</div>
		</div>
	</div>
	<div class="proto-dialog-button-line">
		<input type="button" value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
	</div>
</div>
</html>
