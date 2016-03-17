<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base }/scripts/auth/user/list.js"></script>

<script type="text/javascript" src="${base }/scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base }/images/wmi/blacks/32x32/user.png">
	<spring:message code="user.list.label.title" />
 	 	
        <input type="button" value="<spring:message code="btn.disable" />" class="button disable" title="<spring:message code="btn.disable" />"/>
        <input type="button" value="<spring:message code="btn.enable" />" class="button enable" title="<spring:message code="btn.enable" />"/>
        <input type="button" value="<spring:message code="btn.add" />" class="button orange adduser" title="<spring:message code="btn.add" />"/>
        
    </div>

	<form id="searchForm" action="/user/list.json">
	 <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
            <td><label><spring:message code="user.list.filter.condition" /></label></td>
            <td>
            	<select loxiaType="select" id="searchType" name="searchType" checkmaster="chooseFilterType">
                	<option value="q_sl_userName"><spring:message code="user.list.filter.username" /></option>
                	<option value="q_sl_realName"><spring:message code="user.list.filter.realname" /></option>
            	</select>
            </td>
            
            <td><label><spring:message code="user.list.filter.key" /></label></td>
           
            <td><input type="text" id="searchKey" placeHolder="<spring:message code="user.list.filter.placeholder.key" />" name="q_sl_userName" loxiaType="input" mandatory="false"></input></td>
            
            <td><label><spring:message code="user.list.filter.orgname" /></label></td>
            
            <td>
            	<select loxiaType="select" mandatory="false" id="orgSelect" name="q_long_orgId">
                   	<option value=""><spring:message code="role.list.label.unlimit" /> </option>
          	      	<c:forEach var="item" items="${orgList}" >
            	    <option value="${item.id }">${item.name }</option>
                	</c:forEach>
                </select>
            </td>
            </tr>
            
            <tr>
            <td><label><spring:message code="user.list.filter.createtime" /></label></td>
            <td><input type="text" id="startDate" name="q_date_startDate" loxiaType="date" mandatory="false"></input>
            </td>
            
            <td><label>——</label></td>
            <td>
            	<input type="text" name="q_date_endDate" id="endDate" loxiaType="date" mandatory="false"></input>
            </td>
            
            
            <td><label><spring:message code="user.list.filter.state" /></label></td>
            
            <td>
            	<select loxiaType="select" id="available" name="q_int_lifecycle"  >
            		<option value=""><spring:message code="role.list.label.unlimit"/> </option>
	                <c:forEach var="item" items="${avaCoList}">
	                	
	                	<option value="${item.optionValue }" <c:if test="${item.optionValue==1 }">selected="selected"</c:if> >${item.optionLabel }</option>
	                </c:forEach>
            	</select>
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
    	
   	 	<div class="table-border0 border-grey" id="table1" caption="<spring:message code="user.list.label.userlist" />"></div>   
    </div>
    
    
	
    <div class="button-line">
        <input type="button" value="<spring:message code="btn.add" />"  title="<spring:message code="btn.add" />" class="button orange adduser"/>
        <input type="button" value="<spring:message code="btn.enable" />" title="<spring:message code="btn.enable" />" class="button enable" />
        <input type="button" value="<spring:message code="btn.disable" />" title="<spring:message code="btn.disable" />" class="button disable" />
    </div>
</div>


</body>
</html>