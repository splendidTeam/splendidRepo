<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="user.list.label.title" /></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
<script type="text/javascript" src="${base}/scripts/main.js"></script>
<link rel="stylesheet" href="${base}/scripts/jquery/ztree/zTreeStyle.css" type="text/css"></link>
<script type="text/javascript" src="${base}/scripts/jquery/ztree/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript" src="${base}/scripts/auth/menu/menu.js"></script>
<script type="text/javascript">

var menuList = [
          	{id:'0', pId:-1, name:"ROOT", 
           		code:"ROOT", sortNo:1, 
           		open:true, lifecycle:1,nocheck:true},
               <c:forEach var="menu" items="${menuList}" varStatus="status">
               	{	
            	  id:${menu.id}, 
            	  pId:${menu.parentId},
            	  name:"${menu.label}",
            	  murl:"${menu.url}",
            	  open:false
             	}
               	<c:if test="${!status.last}">,</c:if>
               </c:forEach>
          ];

</script>
<style type="text/css">
#dialog-menu-dialog label {
	margin-right: 5px;
	margin-bottom: 20px;
}
.input_add{
 width: 400px
}
.interval-h{
 height: 15px;
}

.interval-w{
 width: 15px;
}
.edit-select{
	position: absolute;
	width: 380;
	height: 26px;
}
.label{

}
</style>
</head>
<body>
<div class="content-box width-percent100">
	<div class="ui-title1"><img src="${base}/images/wmi/blacks/32x32/calc.png"><spring:message code="menu.edit.mng"/>
	</div>
	 <div class="ui-block ui-block-fleft w240">
             <div class="ui-block-content ui-block-content-lb">
				<div class="tree-control" style="margin-left: 30px">
                 <input type="text" id="key-left" loxiatype="input" placeholder="<spring:message code='item.search.keyword'/>" />
                 <div><span id="search_result_left"></span></div>
            	 <ul id="tree-left" class="ztree"></ul>
		     </div>
             </div>
      </div>
      
       <div class="ui-block ml240" style="padding-left: 10px;">
           <div class="ui-block-title1"><spring:message code="menu.edit.info"/></div>
           <div id="currentNodeDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
           	<div class="ui-block ui-block-content-lb">
		          <table class="detail"  mid="">
			        <tr>
			      		<td><label><spring:message code="menu.edit.name"/></label></td>
						<td>
							<input readonly="readonly" class="input_add label"  type="text" loxiaType="input"  mandatory="true"  >
						</td>
						<td  class="interval-w"></td>
			      		<td><label><spring:message code="menu.edit.menu"/>url</label></td>
						<td>
							<input readonly="readonly"  class="input_add url"  type="text" loxiaType="input"  mandatory="true"  >
						</td>
			        </tr> 
			        <tr char="interval-h" ></tr>
			        <tr>
						<td><label><spring:message code="menu.edit.type"/></label></td>
						<td>
							 <select disabled="disabled"   loxiaType="select"  mandatory="true" class="orgtype input_add" > 
								<c:forEach items="${menuType}" var="type">
									<option value="${type.id}">${type.name}</option>
								</c:forEach>
							</select>
							
							<input type="hidden" class="input_add orgtypeHid" value=""/>
							
						</td>
						<td  class="interval-w"></td>
						<td><label><spring:message code="menu.edit.auth.name"/></label></td>
						<td>
							<input readonly="readonly"  class="input_add name"  type="text" loxiaType="input"  mandatory="true"  >
						</td>
			        </tr> 
			         <tr char="interval-h" ></tr>
			         <tr>
						<td><label><spring:message code="menu.edit.acl"/></label></td>
						<td>
							<input readonly="readonly"  class="input_add acl"  type="text" loxiaType="input"  mandatory="true"  >
						</td>
						<td  class="interval-w"></td>
						<td><label><spring:message code="shop.property.lable.groupname"/></label></td>
						<td>
							<select disabled="disabled"  class="gname input_add"  loxiaType="select"  mandatory="true" > 
								<c:forEach items="${privileges}" var="pri" varStatus="status">
									<option value="${pri}">${pri}</option>
								</c:forEach>
							</select>
						</td>
			        </tr> 
			         <tr char="interval-h" ></tr>
			         <tr>
						<%-- <td><label><spring:message code="menu.edit.icon"/></label></td>
						<td>
							<input readonly="readonly"  class="input_add icon"  type="text" loxiaType="input"  mandatory="true"  >
						</td>
						<td  class="interval-w"></td> --%>
						<td><label><spring:message code="menu.edit.sortno"/></label></td>
						<td>
							<input  readonly="readonly"  class="input_add sort"  type="text" loxiaType="number"  mandatory="true"  >
						</td>
						<td  class="interval-w"></td>
						<td><label><spring:message code="menu.edit.desc"/></label></td>
						<td >
							<textarea class="desc" style="width:400px; resize:none"  rows="" cols="" loxiaType="input" > </textarea>
						</td>
			        </tr> 
			       <%--   <tr char="interval-h" ></tr>
			         <tr>
						<td><label><spring:message code="menu.edit.desc"/></label></td>
						<td colspan="4" >
							<textarea readonly="readonly"  class="desc" style="width: 690px;height: 85px;resize:none"  rows="" cols="" loxiaType="input" > </textarea>
						</td>
			        </tr>  --%>
			        
		    	</table>
		    </div>
	        <div class="button-line1">
	             <a href="javascript:void(0);" class="func-button add"  title="<spring:message code="btn.save"/>"><spring:message code="product.property.button.insert"/></a>
	             <a href="javascript:void(0);" class="func-button update"  title="<spring:message code="btn.update"/>"><spring:message code="btn.update"/></a>
                 <a href="javascript:void(0);" class="func-button delete"  title="<spring:message code="btn.delete"/>"><spring:message code="btn.delete"/></a>
                 <a href="javascript:void(0);" class="func-button exportsql"  title="导出菜单sql">导出菜单sql</a>
	        </div>

           </div>
      </div>
      
       <div class="ui-block ml240" style="padding-left: 10px;">
           <div class="ui-block-title1"><spring:message code="menu.edit.fun.info"/></div>
           <div id="currentNodeDiv" class="ui-block-content border-grey" style="margin-bottom: 10px;">
	         <div class="ui-block">
   	 			<div class="table-border0 border-grey" id="table1" ></div>   
    		</div>
	       <%--  <div class="button-line1">
	             <a href="javascript:void(0);" class="func-button addUrl"  title="<spring:message code="product.property.button.insert"/>"><spring:message code="product.property.button.insert"/></a>
	        </div> --%>
           </div>
      </div>
 	
</div>
	
<div id="dialog-menu-dialog" class="proto-dialog">
		<h5><spring:message code="menu.edit.menu.info"/></h5>
		<div class="proto-dialog-content p10">
    	  <table class="edit" parentId="" >
		        <tr>
		      		<td><label><spring:message code="menu.edit.name"/></label></td>
					<td>
						<input class="input_add label"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
					<td  class="interval-w"></td>
		      		<td><label><spring:message code="menu.edit.menu"/>url</label></td>
					<td>
						<input class="input_add url edit-select"  type="text" loxiaType="input"  mandatory="true"  >
						<select style="width: 400px"  class="gname-select"  loxiaType="select" > 
							<c:forEach items="${urls}" var="url">
								<option value="${url}">${url}</option>
							</c:forEach>
						</select> 
					</td>
		        </tr> 
		        <tr char="interval-h" ></tr>
		        <tr>
					<td><label><spring:message code="menu.edit.type"/></label></td>
					<td>
						 <select  loxiaType="select"  mandatory="true" class="orgtype input_add" > 
							<c:forEach items="${menuType}" var="type">
								<option value="${type.id}">${type.name}</option>
							</c:forEach>
						</select>
						
					</td>
					<td  class="interval-w"></td>
					<td><label><spring:message code="menu.edit.auth.name"/></label></td>
					<td>
						<input class="input_add name"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr> 
		         <tr char="interval-h" ></tr>
		         <tr>
					<td><label><spring:message code="menu.edit.acl"/></label></td>
					<td>
						<input class="input_add acl"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
					<td  class="interval-w"></td>
					<td><label><spring:message code="shop.property.lable.groupname"/></label></td>
					<td>
						<input class="input_add gname edit-select"  type="text" loxiaType="input"  mandatory="true"  >
						<select style="width: 400px"  class="gname-select"  loxiaType="select"   > 
							<c:forEach items="${privileges}" var="pri" varStatus="status">
								<option value="${pri}">${pri}</option>
							</c:forEach>
						</select>
					</td>
		        </tr> 
		         <tr char="interval-h" ></tr>
		         <tr>
					<%-- <td><label><spring:message code="menu.edit.icon"/></label></td>
					<td>
						<input class="icon"  style="width: 400px;height: 30px"  type="text" loxiaType="input"  >
					</td> --%>
				
					<td><label><spring:message code="menu.edit.sortno"/></label></td>
					<td>
						<input class="input_add sort"  type="text" loxiaType="number"  mandatory="true"  >
					</td>
						<td  class="interval-w"></td>
					<td><label><spring:message code="menu.edit.desc"/></label></td>
					<td >
						<textarea class="desc" style="width:400px; resize:none"  rows="" cols="" loxiaType="input" > </textarea>
					</td>
		        </tr> 
		        <%--  <tr char="interval-h" ></tr>
		         <tr>
					<td><label><spring:message code="menu.edit.desc"/></label></td>
					<td colspan="4" >
						<textarea class="desc" style="width: 680px;height: 85px;resize:none"  rows="" cols="" loxiaType="input" > </textarea>
					</td>
		        </tr>  --%>
		    </table>
		</div>
        <div class="proto-dialog-button-line center">
			<input type="button"   value="<spring:message code='btn.confirm'/>" class="button orange center btn-ok" /> 
        </div>
</div>
<div id="dialog-function-dialog" class="proto-dialog">
		<h5><spring:message code="menu.edit.fun.edit"/></h5>
		<div class="proto-dialog-content p10">
    	  <table class="edit" parentId="" >
		        <tr>
		      		<td><label><spring:message code="menu.edit.fun.name"/></label></td>
					<td>
						<input class="input_add desc"  type="text" loxiaType="input"  mandatory="true"  >
					</td>
		        </tr> 
		         <tr char="interval-h" ></tr>
		        <tr>
		      		<td><label><spring:message code="menu.edit.fun"/>url</label></td>
					<td>
						<input class="input_add url edit-select"  type="text" loxiaType="input"  mandatory="true"  >
						<select style="width: 400px"  class="gname-select"  loxiaType="select"> 
							<c:forEach items="${urls}" var="url">
								<option value="${url}">${url}</option>
							</c:forEach>
						</select>
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
