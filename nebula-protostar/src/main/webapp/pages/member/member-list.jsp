<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<title>${title}</title>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/member/member-list.js"></script>
</head>
<body>

<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/users.png"><spring:message code="member.list.manage"/>
		
		<input class="button orange batchEnable" type="button" value="<spring:message code='member.list.butchable'/>" title="<spring:message code='member.list.butchable'/>">
		<input class="button orange batchDisable" type="button" value="<spring:message code='member.list.butchenable'/>" title="<spring:message code='member.list.butchenable'/>">
    </div>
    <form action="/member/memberList.json" id="searchForm">
    <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
        <table>
            <tr>
                <td><label><spring:message code="member.group.membername"/></label></td>
                <td>
                    <span><input type="text" loxiaType="input" mandatory="false" placeholder="<spring:message code='member.group.loginname'/>" id="loginName" name="q_sl_loginName"></input></span>
                </td>
                <td><label><spring:message code="member.list.email"/></label></td>
                <td>
                   <span> <input type="text" loxiaType="input" mandatory="false" placeholder="member@abc.com" id="loginEmail" name="q_sl_loginEmail"></input></span>
                </td>
                <td><label><spring:message code="member.list.mobile"/></label></td>
                <td>
                   <span><input type="text" loxiaType="input" mandatory="false" placeholder="13xxxxxxxxx" id="loginMobile" name="q_sl_loginMobile"></input></span>
                </td>
            </tr>
            <tr>
                <td><label><spring:message code="member.group.source"/></label></td>
          
                <td>
					<span>
					<opt:select name="q_long_Source" id="Source" loxiaType="select" expression="chooseOption.MEMBER_SOURCE" nullOption="role.list.label.unlimit"/>
					</span>
				</td>
				
				
				
				
                <td><label><spring:message code="member.list.group"/></label></td>
           
                <td>
            	<select loxiaType="select" mandatory="false" id="groupId" name="q_long_groupId">
                   	<option value=""><spring:message code="member.group.label.unlimit"/> </option>
          	      	<c:forEach var="item" items="${memberList}" >
            	    <option value="${item.id}">${item.name}</option>
                	</c:forEach>
                </select>
            </td>
          
            <td><label><spring:message code="member.group.type"/></label></td>
       
             <td>
					<span>
					<opt:select name="q_long_Type" id="Type" loxiaType="select" expression="chooseOption.MEMBER_TYPE" nullOption="role.list.label.unlimit"/>
					</span>
			</td>
            </tr><tr>
            <td><label><spring:message code="itemcategory.list.filter.createtime"/></label></td>
            <td>
               <span><input type="text" id="startTime" name="q_date_startTime"  value="" loxiaType="date" mandatory="false"  /></span>
            </td>
            <td><label>——</label></td>
            <td>
                <span><input type="text" id="endTime" name="q_date_endTime"  value="" loxiaType="date" mandatory="false"/></span>
            </td>
            <td><label><spring:message code="role.list.label.status"/></label></td>
          
            <td>
			      <span>
			         <opt:select name="q_int_lifeCycle" id="lifeCycle" loxiaType="select" expression="chooseOption.IS_AVAILABLE" nullOption="role.list.label.unlimit"/>
			       </span>
			 </td>
        </tr>
        </table>
        <div class="button-line1">
        <a href="javascript:void(0);" class="func-button search" title="<spring:message code='user.list.filter.btn'/>"><span><spring:message code="user.list.filter.btn"/></span></a>
        </div>
    </div>
    </div>
     </form>

    
    <div class="ui-block">
   	 	<div class="border-grey" id="table1" caption="<spring:message code='member.group.member.list'/>"></div>
    </div>
   
    
    
	
    <div class="button-line">
		<input class="button orange batchDisable" type="button" value="<spring:message code='member.list.butchenable'/>" title="<spring:message code='member.list.butchenable'/>">
		<input class="button orange batchEnable"  type="button" value="<spring:message code='member.list.butchable'/>" title="<spring:message code='member.list.butchable'/>">

         
    </div>
</div>


</body>
</html>
