<%@include file="/pages/commons/common.jsp"%>
<%@taglib prefix="opt" uri="http://www.baozun.cn/option"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="product.property.lable.manager"/></title>
<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<script type="text/javascript" src="${base }/scripts/freight/distribution/distribution-mode.js"></script>
</head>
<body>
<div class="content-box" style="float: left; width: 100%;">
	<div class="ui-block">
		 <div class="border-grey ui-loxia-simple-table" style="overflow:hidden">
		 	  <div class="ui-loxia-nav"><span class="ui-loxia-table-title">物流方式</span></div>
		 	 <div class="ui-block-title1">新增或修改物流</div>
		 	  <div class="ui-block-line p5">
					<label>物流名称</label>
					<div class="wl-right">
				        <input type="hidden" id="mode_id"  value="${distributionMode.id }" />
						<input type="text" id="new_name" value="${distributionMode.name }" mandatory="true"/>
					</div>
				</div>	
		 </div>
		 <div class="p10 right clear">
				   <input type="button" value="<spring:message code='btn.save'/>" class="button orange btn-save"   title="<spring:message code='btn.save'/>" />
				   <input type="button" value="<spring:message code='btn.return'/>" class="button black btn-return"   title="<spring:message code='btn.return'/>" />
			  </div>
	
	</div>
</div>


</body>
</html>
