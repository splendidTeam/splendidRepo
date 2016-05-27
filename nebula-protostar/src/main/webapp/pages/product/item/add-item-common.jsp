<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";
</script>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-golbal.js"></script>
<SCRIPT type="text/javascript">
var pdValidCode = "${pdValidCode}";
var zNodes =
[
	{id:0, name:"ROOT",state:"0", open:true,root:"true",nocheck:true},
	<c:forEach var="industry" items="${industryList}" varStatus="status">
	<c:if test="${industry.isShow}">
		{
			id:${industry.id}, 
			pId:${industry.pId},
			name: "${industry.indu_name}",
			open:${industry.open}
			<c:if test="${industry.noCheck}">
				,nocheck:true
			</c:if>
		}
		<c:if test="${!status.last}">,</c:if>
		</c:if>
	</c:forEach>
];


var baseUrl='${base}'; 
</SCRIPT>

<style type="text/css">
.i18n-lang {
	display: none;
}

.cke_button_myDialogCmd .cke_icon {
	display: none !important;
}

.cke_button_myDialogCmd .cke_label {
	display: inline !important;
}
</style>