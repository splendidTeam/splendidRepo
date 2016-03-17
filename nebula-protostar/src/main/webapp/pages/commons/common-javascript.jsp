<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<meta http-equiv="Pragma" content="No-cache" />
<meta http-equiv="Cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<script type="text/javascript" src="${base}/scripts/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${base}/scripts/ajax-extend.js"></script>
<script type="text/javascript">
	var $j = jQuery.noConflict();
<%--用于js 拼接连接 contextPath 应用程序名--%>
	var base = "${base}";
	var pagebase = "${pagebase}";
	var staticbase = "${staticbase}";
	var imgbase="${imgbase}";
	var i18nFlag="${i18nOnOff}";
	var i18nOnOff = false;
	if(i18nFlag=="true"){
		i18nOnOff= true;
	}
	var defaultlang = "${defaultlang}";
	
</script>


<script type="text/javascript" src="${base}/scripts/main.js"></script>

<script type="text/javascript" src="${base}/scripts/jquery/jquery-migrate-1.2.0.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/jqueryui/jquery-ui-1.10.2.custom.js"></script>

<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiacore-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiainput-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiaselect-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxiatable-2.js"></script>
<script type="text/javascript" src="${base}/scripts/loxia2/jquery.loxia.locale-zh-CN.js"></script>

<script type="text/javascript" src="${base}/scripts/jquery/jqueryui/jquery.ui.datepicker-zh-CN.js"></script>
<script type="text/javascript" src="${base}/scripts/nebula/jquery.nebula.protostar-1.js"></script>
<script type="text/javascript" src="${base}/scripts/jquery/jqueryui/jquery-ui-timepicker-addon.js"></script>


<script type="text/javascript" src="${base}/scripts/i18n/common.locale-zh-CN.js"></script>

<script type="text/javascript">
	$j(function() {

		loxia.init({
			debug : true,
			region : 'zh-CN'
		});

		nps.init();
	});
	var i18nLangs = [
				<c:forEach var="i18nLang" items="${i18nLangs}" varStatus="status">
	                 {'key':"${i18nLang.key}","value":"${i18nLang.value}","default":"${i18nLang.value}"}
	                 <c:if test="${!status.last}">,</c:if>
	                 </c:forEach>
	                ];
</script>
