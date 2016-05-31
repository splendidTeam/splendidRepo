<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/pages/commons/common.jsp"%>

<%@include file="/pages/commons/common-css.jsp"%>
<%@include file="/pages/commons/common-javascript.jsp"%>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="/scripts/ckeditor/4-4-5/ckeditor.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/add-item-golbal.js"></script>
<script type="text/javascript">	
var itemCodeValidMsg = "${itemCodeValidMsg}";
var pdValidCode = "${pdValidCode}";
var baseUrl='${base}'; 
</script>

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