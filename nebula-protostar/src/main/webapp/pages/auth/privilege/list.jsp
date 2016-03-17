<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<script type="text/javascript" src="${base}/scripts/auth/privilege/auth.js"></script>
<style type="text/css">
#dialog-auth-dialog label {
	margin-right: 5px;
	margin-bottom: 20px;
}
.input_add{
 width: 200px
}
.interval-h{
 height: 15px;
}

.interval-w{
 width: 15px;
}
.edit-select{
	position: absolute;
	width: 180;
	height: 26px;
}
</style>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png">权限管理
	 <input type="button" value="<spring:message code="product.property.button.delete"/>"	class="button delete batch" title="<spring:message code="product.property.button.delete"/>"/>
	 <input type="button" value="<spring:message code="btn.add" />" class="button orange addPrivilege" title="<spring:message code="btn.add" />"/>
	</div>

	<form id="searchForm">
	  <div class="ui-block">
    <div class="ui-block-content ui-block-content-lb">
    <table >
        <tr>
       			<td><label>权限编码</label></td>
				<td>
					<input name="q_sl_acl" type="text" loxiaType="input" ></input>
				</td>
       			<td><label>权限名称</label></td>
				<td>
					<input name="q_sl_name" type="text" loxiaType="input" ></input>
				</td>
				<td><label>分组名称</label></td>
				<td>
					<input name="q_sl_groupName" type="text" loxiaType="input" ></input>
				</td>
       			<td><label>组织类型</label></td>
				<td>
					<opt:select name="orgTypeId" loxiaType="select" cssClass="org-type"  nullOption="role.list.label.unlimit"  expression="commonOption.orgType"/>
				</td>
					<td><label>状态</label></td>
				<td>
					<opt:select name="q_int_lifecycle" loxiaType="select" expression="chooseOption.IS_AVAILABLE" nullOption="role.list.label.unlimit"/>
				</td>
        </tr> 
    </table>
   	<div class="button-line1">
   		<a href="javascript:void(0);" class="func-button reset" title="<spring:message code ='user.list.filter.btn'/>">重置</a>
       	<a href="javascript:void(0);" class="func-button search" title="<spring:message code ='user.list.filter.btn'/>"><spring:message code ='user.list.filter.btn'/></a>
       </div>
    </div>
    </div>
    </form> 
    
    <div class="ui-block">
   	 	<div class="table-border0 border-grey" id="tableList" caption="权限列表"></div>   
    </div>
</div>

<div id="dialog-auth-dialog" class="proto-dialog">
		<h5>权限编辑</h5>
		<div class="proto-dialog-content p10">
    	  <table class="edit" parentId="" >
		        <tr>
					<td><label><spring:message code="menu.edit.acl"/></label></td>
					<td>
						<input class="input_add acl"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
					<td  class="interval-w"></td>
					<td><label><spring:message code="menu.edit.auth.name"/></label></td>
					<td>
						<input class="input_add name"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr> 
		         <tr char="interval-h" ></tr>
		         <tr>
					<td><label>权限类型</label></td>
					<td>
						 <select  loxiaType="select"  mandatory="true" class="orgtype input_add" > 
							<c:forEach items="${menuType}" var="type">
								<option value="${type.id}">${type.name}</option>
							</c:forEach>
						</select>
					</td>
					<td  class="interval-w"></td>
					<td><label><spring:message code="shop.property.lable.groupname"/></label></td>
					<td>
						<input class="input_add gname edit-select"  type="text" loxiaType="input"  mandatory="true"  >
						<select style="width: 200px"  class="gname-select"  loxiaType="select"   > 
							<c:forEach items="${privileges}" var="pri" varStatus="status">
								<option value="${pri}">${pri}</option>
							</c:forEach>
						</select>
					</td>
		        </tr> 
		         <tr char="interval-h" ></tr>
		         <tr>
					<td><label><spring:message code="menu.edit.desc"/></label></td>
					<td >
						<textarea class="desc" style="width:200px; resize:none"  rows="" cols="" loxiaType="input" > </textarea>
					</td>
		        </tr> 
		    </table>
		  <br>
			<div class="ui-block function-edit">
	           <div class="ui-block-title1"><spring:message code="menu.edit.fun.info"/></div>
	           <div id="currentNodeDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
		         <div class="ui-block">
	   	 			<div class="table-border0 border-grey" id="table1" ></div>   
	    		</div>
		        <div class="button-line1">
		             <a href="javascript:void(0);" class="func-button addUrl"  title="添加">添加</a>
		        </div>
	          </div>
	      </div>
		</div>
        <div class="proto-dialog-button-line center">
			<input type="button"   value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
        </div>
</div>

<div id="dialog-function-dialog" class="proto-dialog" parentid="" fid="" >
		<h5><spring:message code="menu.edit.fun.edit"/></h5>
		<div class="proto-dialog-content p10">
    	  <table class="edit" parentId="" >
		        <tr>
		      		<td><label><spring:message code="menu.edit.fun"/>url</label></td>
					<td>
						<input class="input_add url edit-select" style="width: 380px"  type="text" loxiaType="input"  mandatory="true"  >
						<select style="width: 400px"  class="gname-select"  loxiaType="select"> 
							<c:forEach items="${urls}" var="url">
								<option value="${url}">${url}</option>
							</c:forEach>
						</select>
					</td>
		        </tr> 
		          <tr char="interval-h" ></tr>
		        <tr>
		      		<td><label><spring:message code="menu.edit.fun.name"/></label></td>
					<td>
						<input class="input_add desc"  style="width: 400px" type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr> 
		       
		    </table>
		</div>
        <div class="proto-dialog-button-line center">
			<input type="button"  value="<spring:message code='btn.confirm'/>" class="button orange btn-ok" /> 
        </div>
</div>

<div id="dialog-sql-dialog" class="proto-dialog">
		<h5><spring:message code="menu.edit.fun.edit"/></h5>
		<div class="proto-dialog-content p10">
    		<textarea class="sql" style="width: 940px;height: 250px;resize:none"  rows="" cols="" loxiaType="input" ></textarea>
		</div>
        <div class="proto-dialog-button-line center">
			<input type="button"  value="关闭" class="button orange btn-close" /> 
        </div>
</div>
</body>
</html>