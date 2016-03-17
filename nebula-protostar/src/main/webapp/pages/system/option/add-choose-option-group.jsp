<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>

<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>

<script type="text/javascript"
	src="${base}/scripts/system/option/add-choose-option-group.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>


</head>
<body>

<div class="content-box">
	<form name="createForm" action="/option/saveOptionGroup.json" method="post">
	    <div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/compass.png"><spring:message code="chooseoption.group.manage"/>
		    <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
	        <input type="button" value="<spring:message code='btn.save'/>" class="button orange saveoptgroup" title="<spring:message code='btn.save'/>"/>
		</div>
		<div class="ui-block">
	    	<div class="ui-block-title1"><spring:message code="chooseoption.group.add"/></div>
	    	<div class="ui-block-content border-grey" style="margin-bottom: 10px;">
	            <div class="ui-block-line">
					<label><spring:message code="chooseoption.group.code"/></label>
					<div >
						<input name="groupCode" id="groupCode" type="text" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='chooseoption.group.code'/>"/>
					</div>
	    		</div>
	    
			    <div class="ui-block-line">
			        <label><spring:message code="chooseoption.group.desc"/></label>
			        <div >
						<input name="groupDesc" id="groupDesc" type="text" loxiaType="input" value="" mandatory="true" placeholder="<spring:message code='chooseoption.group.desc'/>"/>
			        </div>
			    </div>
	

			    <div class="ui-block-title1" style="background:#fff;color:#000;"><spring:message code="chooseoption.group.add.info"/></div>
			    <div id="edittable" loxiaType="edittable" >
				<table operation="add,delete" append="1" width="2000" class="inform-person">
					<thead>
						<tr>
							<th width="5%"><input type="checkbox"/></th>
							<th width="35%"><spring:message code="chooseoption.group.add.optionlable"/></th>
							<th width="30%"><spring:message code="chooseoption.group.add.optionvalue"/></th>
							<th width="30%"><spring:message code="chooseoption.group.add.sortno"/></th>
						</tr>
					</thead>
				    
				    <tbody>
				    </tbody>
				    
					<tbody>
						<tr>
							<td width="5%"><input type="checkbox"/></td>
							<td width="15%"><input type="text" loxiaType="input" name="chooseOptions.optionLabel" id="input3"  style="width:95%" mandatory="true" placeholder="<spring:message code='chooseoption.group.add.optionlable'/>"/></td>
							<td width="15%"><input type="text" loxiaType="input" name="chooseOptions.optionValue" id="input2" value="" mandatory="true" placeholder="<spring:message code='chooseoption.group.add.optionvalue'/>"/></td>
							<td width="15%"><input type="text" loxiaType="number" name="chooseOptions.sortNo" value="" mandatory="true" placeholder="<spring:message code='chooseoption.group.add.sortno'/>"/></td>
						</tr>
						
					</tbody>
				    
					<tfoot>
						<tr>
							<td colspan="2" >&nbsp;</td><td>&nbsp;</td>
						</tr>
					</tfoot>
				</table>
				</div>


	    	</div>
		</div>
	</form>
    
    <div class="button-line">
        <input type="button" value="<spring:message code='btn.save'/>" class="button orange saveoptgroup" title="<spring:message code='btn.save'/>"/>
        <input type="button" value="<spring:message code='btn.return'/>" class="button return" title="<spring:message code='btn.return'/>"/>
        
    </div>
</div>
</body>
</html>
