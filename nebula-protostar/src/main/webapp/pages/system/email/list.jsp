<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<style type="text/css">
</style>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/system/email/list.js"></script>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/calc.png"><spring:message code="email.list.email.mng"/>
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addItemSortScore" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label><spring:message code="member.group.name"/></label></td>
			<td>
			<input name="q_string_name" type="text" loxiaType="input" mandatory="false"></input>
			</td>
			 <td><label><spring:message code="navigation.list.type"/></label></td>
			<td>
			<opt:select  name="q_int_type" loxiaType="select" expression="chooseOption.EMAIL_TEMPLATE_TYPE" nullOption="member.group.label.unlimit" />
			</td>
            <td><label><spring:message code="email.edit.subject"/></label></td>
			<td>
			<input name="q_string_subject" type="text" loxiaType="input"  mandatory="false"></input>
			</td>
			<td><label><spring:message code="role.list.label.status"/></label></td>
			<td>
			<opt:select id="isused" name="q_int_lifecycle" loxiaType="select"  expression="chooseOption.IS_AVAILABLE" nullOption="member.group.label.unlimit" />
			</td>
            <td></td> 
            <td></td>
        </tr> 
    </table>
    	<div class="button-line1">
        	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
        </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code="email.list.email.list"/>"></div>   
    </div>
    
</div>

</body>
</html>
