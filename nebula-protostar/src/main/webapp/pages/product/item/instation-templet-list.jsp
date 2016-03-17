<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/product/item/instation-templet-list.js"></script>
<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">
	站内信模板管理
 	 	
        <input type="button" value="<spring:message code="btn.add" />" class="button orange add" title="<spring:message code="btn.add" />"/>
        
    </div>

	<form id="searchForm" action="/user/list.json">
	 <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label>站内信标题</label></td>
            <td>
            	<input type="text" placeHolder="站内信标题" name="q_sl_name" loxiaType="input" mandatory="false" />
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
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="站内信模板列表"></div>   
    </div>
    
    
	
    <div class="button-line">
        <input type="button" value="<spring:message code="btn.add" />"  title="<spring:message code="btn.add" />" class="button orange add"/>
    </div>
</div>


</body>
</html>