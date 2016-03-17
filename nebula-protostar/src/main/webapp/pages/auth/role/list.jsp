<%@include file="/pages/commons/common.jsp" %>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
<%@include file="/pages/commons/common-css.jsp" %>
<%@include file="/pages/commons/common-javascript.jsp" %>
<script type="text/javascript" src="${base}/scripts/auth/role/list.js"></script>
<script type="text/javascript" src="${base}/scripts/search-filter.js"></script>
</head>
<body>
<div class="content-box">
	<div class="ui-title1">
		<img src="${base}/images/wmi/blacks/32x32/user.png"><spring:message code="role.list.label.title"/>
        <input type="button" value="<spring:message code ='btn.all.delete'/>" class="button deleteRole" title="<spring:message code ='btn.all.delete'/>"/>
		<input type="button" value="<spring:message code ='btn.add'/>" class="button orange addRole" title="<spring:message code ='btn.add'/>"/>
    </div>
    
	<form id="searchForm">
    
	    <div class="ui-block">
			<div class="ui-block-content ui-block-content-lb">
			    <table>
			    	<tr>
			    		<td>
			    			<span>
			    				<spring:message code="role.list.role.name"/>
			    			</span>
						</td>
						<td>
							<span>
								<input name="q_sl_name" type="text" class="ui-loxia-default" placeholder="<spring:message code="role.list.role.short.name"/>"/>
							</span>
						</td>
						
						<td>
			               	<span>
			               		<spring:message code="role.list.dept.type"/>
			               	</span>
			    		</td>
			    		<td>
							<span>
								<opt:select name="q_long_orgTypeId" loxiaType="select" expression="commonOption.orgType" nullOption="role.list.label.unlimit"/>
							</span>
						</td>
						
			    		<td>
			                <span>
			                	<spring:message code="role.list.label.status"/>
			                </span>
			           	</td>
			           	<td>
			           		<span>
			           			<opt:select name="q_int_lifecycle" loxiaType="select" expression="chooseOption.IS_AVAILABLE" nullOption="role.list.label.unlimit"/>
			           		</span>
			           	</td>
			           	
			        </tr>
			    </table>
			    
			    <div class="button-line1">
					<a href="javascript:void(0);" class="func-button search"><span><spring:message
										code="product.property.lable.search" /></span></a>
				</div>
			    
	    	</div>
	    </div>
    </form>
    <div class="ui-block">
   	 	<div class="border-grey" id="table1" caption="<spring:message code="role.list.label.rolelist"/>"></div>   
    </div>
        <div class="button-line">
		<input type="button" value="<spring:message code ='btn.add'/>" class="button orange addRole" title="<spring:message code ='btn.add'/>"/>
        <input type="button" value="<spring:message code ='btn.all.delete'/>" class="button deleteRole" title="<spring:message code ='btn.all.delete'/>"/>
    </div>
</div>
</body>
</html>