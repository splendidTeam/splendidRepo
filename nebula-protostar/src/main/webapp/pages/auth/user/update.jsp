<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.modify.label.modify" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<style type="text/css">
.wl-right-auto2
{


border:1px solid #ccc;
overflow-y:scroll;

}
.wl-right-auto2  input[type="checkbox"]
{
clear:both;
}

.wl-right-auto2  input[type="radio"]
{
clear:both;
}
.wl-right-auto2  p
{
width:80%;
}
</style>

<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base }/scripts/auth/user/user-role.js"></script>

<script type="text/javascript">

//初始化数据(店铺类型的组织)

var shopOrgList=<c:out value="${shopOrgList}" default="[]" escapeXml="false"></c:out>;

//初始化数据(系统类型的组织)
var sysOrgList=<c:out value="${sysOrgList}" default="[]" escapeXml="false"></c:out>;

//初始化数据(系统类型的角色)
var sysRoleList=<c:out value="${sysRoleList}" default="[]" escapeXml="false"></c:out>;

//初始化数据(系统类型的角色)
var shopRoleList=<c:out value="${shopRoleList}" default="[]" escapeXml="false"></c:out>;

//初始化数据,用户当前的角色组织对应关系，如没有设为new Array()
var urData=<c:out value="${uerRoleCommList}" default="[]" escapeXml="false"></c:out>;

var orgTypeList=<c:out value="${coList}" default="[]" escapeXml="false"></c:out>;


var pageStatue='modify';
<c:if test="${statue=='view'}">   
pageStatue='readonly';
</c:if>
</script>

<script type="text/javascript" src="${base }/scripts/auth/user/update.js"></script>

<script type="text/javascript" src="../scripts/main.js"></script>

</head>

<body>

<div class="content-box width-percent100">
    
   <form name="userForm" action="/user/update.htm" method="post">
	<div class="ui-title1">
		<img src="${base }/images/wmi/blacks/32x32/user.png">
		<spring:message code="user.list.label.title" />
	</div>
	<div class="ui-block">
    <div class="ui-block-title1">
    	<c:choose>
		<c:when test="${statue=='view'}">
			<spring:message code="user.modify.label.view" />
		</c:when>
		<c:otherwise>
			<spring:message code="user.modify.label.modify" />
		</c:otherwise>
		</c:choose>
	</div>
	
	 <div class="ui-block-content border-grey">
	
    <div class="ui-block-line">
         <label><spring:message code="user.modify.label.username" /></label>
         <div>
              <input type="text" loxiaType="input" value="${user.userName }" name="userName" mandatory="true"/>
         </div>
    </div>
    
    
    
     <div class="ui-block-line">
         <label><spring:message code="user.modify.label.email" /></label>
         <div>
              <input name="email" type="text" loxiaType="input" value="${user.email }" mandatory="true"/>
         </div>
    </div>
    
    <div class="ui-block-line">
         <label><spring:message code="user.modify.label.realname" /></label>
         <div>
              <input name="realName" type="text" loxiaType="input" value="${user.realName }" mandatory="true"/>
         </div>
    </div>
    
    <div class="ui-block-line">
         <label><spring:message code="user.modify.label.mobile" /></label>
         <div>
              <input type="text" name="mobile" loxiaType="input" value="${user.mobile }" mandatory="true"/>
         </div>
    </div>
    

    
     <div class="ui-block-line">
         <label title=""><spring:message code="user.modify.label.afforg" /></label>
         <div class="wl-right ">    
    			<select name="orgId" id="orgSelect" loxiaType="select" style="float:left;">

    				<c:forEach var="item" items="${orgList}">
    					<c:if test="${user.orgId==item.id}">
    						<option value="${item.id }">${item.name }</option>
    					</c:if>
    				</c:forEach>
    			</select>
    			
    			
         </div>
    </div>
    
     <div class="ui-block-line">
         <label title=""><spring:message code="user.modify.label.managerole" /></label>
         <div>    
    			<div id="userRoleTable" class="border-qian-grey">
    			</div>
    			<div id="noUserRoleTable" class="width-percent50" style="display:none;line-height:28px;border:1px solid #CCCCCC;"></div>
         </div>
    </div>
    
    <div class="ui-block-line addrole">
         <label title=""><spring:message code="user.modify.label.addrole" /></label>
         <div>    
    			<div id="userRoleTable">
    				<span><spring:message code="user.modify.label.orgtype" /></span>
    				
    				<span><select loxiaType="select"  checkmaster="chooseFilterOrgType" style="width:20%;" id="orgTypeSelect"> </select></span>
    				
    				<span><spring:message code="user.modify.label.role" /> </span>
    				<span><select loxiaType="select"   style="width:20%;" id="roleSelect"><option value="">请先选择组织类型</option> </select></span>
    				<br/>
    				<span><spring:message code="user.modify.label.org" /> </span>
    				<div id="addUserRoleDiv" class="border-grey wl-right-auto width-percent50 mt10" style="overflow-y:scroll;height:100px;margin:10px;width:48%;"></div>
    				
    				<div style="margin-left:10px;margin-bottom:10px;" class="width-percent50">
    					<span style="float:left;line-height:25px;"><a href="javascript:void(0)" class="func-button addrole"><spring:message code="user.modify.label.confirmadd" /></a></span>
    					<span style="float:right;line-height:25px;" id="span-allchk-role"><input type="checkbox" class="func-button allchk-role"> <spring:message code="chk.all" />  </span>
    				
    				 </div>
    			</div>
    			
         </div>
    </div>
    
    </div>

	</div>
	<input type="hidden" name="userId" value="${user.id }" >
   </form>
 
 
    <div class="button-line">
         <input type="button" value="<spring:message code="btn.save" />" title="<spring:message code="btn.save" />" class="button orange submit" />

         <input type="button" value="<spring:message code="btn.return" />" title="<spring:message code="btn.return" />" class="button return"/>
         
         <input type="hidden" name="oldUserName" value="${user.userName }" >
    </div>

</div>


</body>
</html>
